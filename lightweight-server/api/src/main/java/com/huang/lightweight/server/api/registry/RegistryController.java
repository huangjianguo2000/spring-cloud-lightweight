package com.huang.lightweight.server.api.registry;

import com.huang.lightweight.common.pojo.Instance;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.server.api.model.v1.Result;
import com.huang.lightweight.server.registry.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Registry-Center Controller.
 *
 * @author lightweight
 */
@RestController
@RequestMapping("/light/v1/rc")
public class RegistryController {

    /**
     * registryService
     */
    @Autowired
    private RegistryService registryService;

    /**
     * Registers a service instance.
     *
     * @param instance The service instance object to be registered.
     * @return The result of the operation.
     */
    @PostMapping("/instance")
    public Result<Void> registry(@RequestBody Instance instance){
        registryService.registry(instance);
        return Result.success();
    }

    @GetMapping("/instance")
    public Result<List<InstanceWrapper>> listInstance(){
        return Result.success(registryService.listInstances());
    }

}
