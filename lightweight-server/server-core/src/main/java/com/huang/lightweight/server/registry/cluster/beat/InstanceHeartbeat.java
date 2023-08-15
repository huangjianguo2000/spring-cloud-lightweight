package com.huang.lightweight.server.registry.cluster.beat;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.common.LoggerUtils;
import com.huang.lightweight.server.registry.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * 检测实例是否存活的线程池
 *
 * @Author lightweight
 * @Date 2023/5/24 15:32
 */
@Component
public class InstanceHeartbeat {

    private final Logger logger = LoggerFactory.getLogger(InstanceHeartbeat.class);

    /**
     * 用于存储每个实例相关任务的映射表
     */
    private Map<Instance, ScheduledFuture<?>> taskMap;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Autowired
    private ServiceManager serviceManager;

    /**
     * 初始化 ScheduledThreadPool 组件
     */
    @PostConstruct
    public void init() {
        taskMap = new ConcurrentHashMap<>();
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(20);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(this::undertakeEvent,
                Constants.HEART_BEAT_INTERVAL_UNDERTAKE, Constants.HEART_BEAT_INTERVAL_UNDERTAKE, MILLISECONDS);
    }

    /**
     * 执行指定实例的任务
     *
     * @param instance       要执行任务的实例
     */
    public void execute(Instance instance) {

        if (!checkTask(instance)) {
            // 实例的任务已存在
            LoggerUtils.printIfInfoEnabled(logger, "beat heart task is Existed instance = {}", instance);
            return;
        }
        LoggerUtils.printIfInfoEnabled(logger, "add beat heart task instance = {}", instance);
        Runnable task = () -> doCheckBeat(instance);
        // 提交任务
        RunnableScheduledFuture<?> scheduledFuture = (RunnableScheduledFuture<?>) scheduledThreadPoolExecutor
                .scheduleAtFixedRate(task, Constants.HEART_BEAT_INTERVAL, Constants.HEART_BEAT_INTERVAL, MILLISECONDS);
        // 保存任务
        taskMap.put(instance, scheduledFuture);
    }

    /**
     * 检查指定实例的任务是否已存在
     *
     * @param instance 要检查的实例
     * @return 若实例没有关联任务，返回 true，否则返回 false
     */
    private boolean checkTask(Instance instance) {
        for (Instance instanceTemp : taskMap.keySet()) {
            // 检查实例是否已关联任务
            if (instanceTemp == instance) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对指定实例执行心跳检查
     * 若心跳间隔超过规定时间，将实例从缓存池中移除或标记为不健康状态
     *
     * @param instance       要进行心跳检查的实例
     */
    public void doCheckBeat(Instance instance) {
        if (checkAndRemoveInstance(instance)) {
            taskMap.remove(instance);
        }
    }

    /**
     * 检测心跳过期
     *
     * @param instance
     * @return 是否需要移除实例
     */
    public boolean checkAndRemoveInstance(Instance instance) {

        // 计算最后一次心跳和当前时间之间的时间间隔
        long interval = System.currentTimeMillis() - serviceManager.getInstance(instance).getLastBeat();
        // 检查间隔是否超过心跳间隔
        if (interval > (Constants.HEART_BEAT_INTERVAL)) {
            // 若超过，则将实例标记为不健康状态
            LoggerUtils.printIfInfoEnabled(logger, "check instance beat set not healthy {}", instance);
            instance.setHealthy(false);
        }

        // 检查间隔是否超过心跳间隔的两倍
        if (interval > (Constants.HEART_BEAT_INTERVAL * 2)) {
            // 若超过，则将实例从缓存池中移除
            serviceManager.removeInstance(instance);
            LoggerUtils.printIfInfoEnabled(logger, "instance {} is remove, remove task", instance.getServiceName());
            ScheduledFuture<?> runnable = taskMap.get(instance);
            runnable.cancel(false);
            return true;
        }

        return false;
    }

    /**
     * 兜底方案， 提交任务失败
     */
    public synchronized void undertakeEvent() {
        for (Instance value : serviceManager.listAsMap().values()) {
            if (!taskMap.containsKey(value)) {
                checkAndRemoveInstance(value);
            }
        }
    }


}
