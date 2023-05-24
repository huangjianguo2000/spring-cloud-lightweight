package com.huang.lightweight.server.registry.util;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.cache.JvmCachePool;
import com.huang.lightweight.common.util.common.ListUtil;
import com.huang.lightweight.common.util.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @Author lightweight
 * @Date 2023/5/24 15:32
 */
@Component
public class ScheduledThreadPool {

    private  Logger logger = LoggerFactory.getLogger(ScheduledThreadPool.class);

    /**
     * A map to store the tasks associated with each instance
     */
    private Map<Instance, Runnable> taskMap;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    /**
     * Initializes the ScheduledThreadPool component.
     */
    @PostConstruct
    public void init() {
        taskMap = new ConcurrentHashMap<>();
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(20);
    }

    /**
     * Executes a task for the specified instance.
     * @param instance The instance to execute the task for
     * @param cache The cache pool containing the instances
     * @return true if the task is executed successfully, false if the task already exists for the instance
     */
    public boolean execute(Instance instance, JvmCachePool<String, List<Instance>> cache) {
        if (!checkTask(instance)) {
            // Task already exists for the instance
            LoggerUtils.printIfErrorEnabled(logger, "Task is already exist");
            return false;
        }
        Runnable task = () -> doCheckBeat(instance, cache);
        // submit
        scheduledThreadPoolExecutor.scheduleAtFixedRate(task, Constants.HEART_BEAT_INTERVAL, Constants.HEART_BEAT_INTERVAL, MILLISECONDS);
        // save task
        taskMap.put(instance, task);
        return true;
    }

    /**
     * Checks if a task already exists for the specified instance.
     * @param instance The instance to check for
     * @return true if no task exists for the instance, false otherwise
     */
    private boolean checkTask(Instance instance) {
        for (Instance instanceTemp : taskMap.keySet()) {
            // Check if the instance is already associated with a task
            if (instanceTemp == instance) {
                return false;
            }
        }
        return true;
    }

    /**
     * Performs the heartbeat check for the specified instance.
     * If the heartbeat interval is exceeded, it either removes the instance from the cache or marks it as unhealthy.
     * @param instance The instance to perform the heartbeat check on
     * @param cache The cache pool containing the instances
     */
    public void doCheckBeat(Instance instance, JvmCachePool<String, List<Instance>> cache) {

        // Calculate the time interval between the last heartbeat and the current time
        long interval = System.currentTimeMillis() - instance.getLastBeat();
        // Check if the interval exceeds the heartbeat interval
        if (interval > (Constants.HEART_BEAT_INTERVAL)) {
            // If it exceeds, mark the instance as unhealthy
            instance.setHealthy(false);
        }

        // Check if the interval exceeds twice the heartbeat interval
        if (interval > (Constants.HEART_BEAT_INTERVAL * 2)) {
            // If it exceeds, remove the instance from the cache pool
            removeInstance(instance, cache);
        }

    }


    /**
     * Removes the specified instance from the cache pool.
     * @param instance The instance to be removed
     * @param cache The cache pool containing the instances
     */
    public void removeInstance(Instance instance, JvmCachePool<String, List<Instance>> cache) {
        // Get the list of instances associated with the service name
        List<Instance> instances = cache.get(instance.getServiceName());
        if (ListUtil.isEmpty(instances)) {
            return;
        }
        // Remove the specified instance from the list
        instances.remove(instance);

        if (instances.isEmpty()) {
            // If the list is empty, remove the service name from the cache
            cache.remove(instance.getServiceName());
        } else {
            // If the list still contains instances, update the cache with the modified list
            cache.put(instance.getServiceName(), instances);
        }
    }

}
