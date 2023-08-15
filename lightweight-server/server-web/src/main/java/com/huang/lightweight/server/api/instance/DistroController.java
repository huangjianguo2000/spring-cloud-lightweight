package com.huang.lightweight.server.api.instance;

import com.alibaba.fastjson.JSON;
import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.model.v1.Result;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.server.registry.cluster.distributed.distro.DistroProtocol;
import com.huang.lightweight.server.registry.entity.MemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * AP架构实现
 */
@RestController
@RequestMapping(Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT + "/distro")
public class DistroController {

    @Autowired
    private DistroProtocol distroProtocol;

    @PostMapping
    public Result<Void> receiveBeat(@RequestBody MemberRequest memberRequest){
        distroProtocol.beatCheck(memberRequest);
        return Result.success();
    }

}
