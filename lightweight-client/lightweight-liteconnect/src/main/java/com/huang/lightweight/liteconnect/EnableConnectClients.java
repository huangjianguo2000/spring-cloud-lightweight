package com.huang.lightweight.liteconnect;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ConnectClientsRegistrar.class)
public @interface EnableConnectClients {
}
