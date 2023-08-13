package com.huang.lightweight.server.api.instance;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.model.v1.Result;
import com.huang.lightweight.server.registry.entity.MemberRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AP架构实现
 */
@RestController
@RequestMapping(Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT + "/distro")
public class DistroController {


    @PostMapping("/beat")
    public Result<Void> receiveBeat(@RequestBody MemberRequest memberRequest){
        System.out.println(memberRequest);
        return Result.success();
    }

}
