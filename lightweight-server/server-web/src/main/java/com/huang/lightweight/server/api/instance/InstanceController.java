package com.huang.lightweight.server.api.instance;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.Result;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.server.registry.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务接口： 注册，获取，心跳
 *
 * @author lightweight
 */
@RestController
@CrossOrigin
@RequestMapping("/light/v1/rc/instance")
public class InstanceController {

    /**
     * registryService
     */
    @Autowired
    private InstanceService instanceService;

    /**
     * 注册服务实例
     *
     * @param instance The service instance object to be registered.
     * @return The result of the operation.
     */
    @PostMapping
    public Result<Void> registry(@RequestBody Instance instance) throws Exception {
        instanceService.registerInstance(instance);
        return Result.success();
    }
    /**
     * 更新服务实例信息.
     *
     * @param instance The service instance object to be registered.
     * @return The result of the operation.
     */
    @PutMapping
    public Result<Void> update(@RequestBody Instance instance) throws Exception {
        instanceService.registerInstance(instance);
        return Result.success();
    }
    /**
     * 查询所有的服务
     * @return  The result of the operation.
     */
    @GetMapping
    public Result<List<InstanceWrapper>> listInstance(){
        return Result.success(instanceService.listInstances());
    }

    /**
     * beat
     * @param instance
     * @return
     * @throws LightweightException
     */
    @PostMapping("/beat")
    public Result<Void> beat(@RequestBody Instance instance) throws LightweightException {
        instanceService.beat(instance);
        return Result.success();
    }
}
