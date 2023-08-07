/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
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

package com.huang.lightweight.server.exception;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.io.IOException;

/**
 * Global exception handler.
 *
 * @author Nacos
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    
    /**
     * For NacosException.
     *
     * @throws LightweightException
     */
    @ExceptionHandler(LightweightException.class)
    public Result<String> handleLightWeightException(LightweightException ex) throws IOException {
        return  Result.failure(ex.getErrCode(), ex.getErrMsg());
    }

}
