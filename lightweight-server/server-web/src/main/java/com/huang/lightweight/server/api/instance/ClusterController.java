package com.huang.lightweight.server.api.instance;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.model.v1.Result;
import com.huang.lightweight.server.registry.cluster.Member;
import com.huang.lightweight.server.registry.cluster.ServerMemberManager;
import com.huang.lightweight.server.registry.cluster.distributed.distro.DistroProtocol;
import com.huang.lightweight.server.registry.entity.MemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 集群对前端接口
 * @Author lightweight
 * @Date 2023/8/14 11:28
 */
@RestController
@CrossOrigin
@RequestMapping(Constants.DEFAULT_LIGHTWEIGHT_NAMING_CONTEXT + "/cluster")
public class ClusterController {

    @Autowired
    private ServerMemberManager serverMemberManager;

    /**
     * 查询集群节点
     * @return
     */
    @GetMapping
    public Result<List<Member>> listMember(){
        return Result.success(serverMemberManager.getMemberList());
    }

}
