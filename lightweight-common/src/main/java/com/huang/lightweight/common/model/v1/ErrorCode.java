/*
 * Copyright 1999-2022 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huang.lightweight.common.model.v1;

/**
 * Response Error Code.
 *
 * @author lightweight
 *
 */
public enum ErrorCode {

    /**
     *  success.
     */
    SUCCESS(0, "success"),

    /**
     *  parameter missing.
     */
    PARAMETER_MISSING(10000, "parameter missing"),

    /**
     *  parameter error.
     */
    PARAMETER_ERROR(10001, "parameter error"),

    /**
     *  create naming service error.
     */
    CREATE_NAMING_SERVICE_ERROR(10002, "create naming service error"),

    /**
     *  server error.
     */
    SERVER_ERROR(30000, "server error"),

    /**
     *   instance already exist.
     */
    SERVER_INSTANCE_EXIST(30001, "instance already exist"),

    /**
     *   instance not exist.
     */
    SERVER_INSTANCE_NOT_EXIST(30002, "instance not exist"),


    /**
     * 集群配置错误
     */
    CLUSTER_ADD_ERROR(40000, "cluster add error"),
    ;


    private final Integer code;

    private final String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
