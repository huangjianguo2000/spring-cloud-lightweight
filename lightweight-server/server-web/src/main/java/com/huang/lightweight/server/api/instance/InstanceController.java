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
 * Registry-Center Controller.
 *
 * @author lightweight
 */
@RestController
@RequestMapping("/light/v1/rc/instance")
public class InstanceController {

    /**
     * registryService
     */
    @Autowired
    private InstanceService instanceService;

    /**
     * Registers a service instance.
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
     * Registers a service instance.
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
     * list all instances
     * @return  The result of the operation.
     */
    @GetMapping
    public Result<List<InstanceWrapper>> listInstance(){
        return Result.success(instanceService.listInstances());
    }
    @PostMapping("/beat")
    public Result<Void> beat(@RequestBody Instance instance) throws LightweightException {
        instanceService.beat(instance);
        return Result.success();
    }
}
