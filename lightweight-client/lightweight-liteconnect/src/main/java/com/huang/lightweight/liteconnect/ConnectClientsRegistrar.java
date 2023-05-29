//package com.huang.lightweight.liteconnect;
//
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanDefinitionHolder;
//import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
//import org.springframework.beans.factory.support.*;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.ResourceLoaderAware;
//import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
//import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
//import org.springframework.core.annotation.AnnotationAttributes;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.core.type.AnnotationMetadata;
//import org.springframework.core.type.filter.AnnotationTypeFilter;
//import org.springframework.util.Assert;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.StringUtils;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Map;
//import java.util.Set;
//import java.util.function.Supplier;
//
///**
// * @Author lightweight
// * @Date 2023/5/29 11:32
// */
//public class ConnectClientsRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {
//    private Environment environment;
//    private ResourceLoader resourceLoader;
//
//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        String basePackage = ClassUtils.getPackageName(importingClassMetadata.getClassName());
//        ClassPathScanningCandidateComponentProvider scanner = getScanner();
//        scanner.setResourceLoader(this.resourceLoader);
//        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
//                ConnectClient.class);
//        scanner.addIncludeFilter(annotationTypeFilter);
//        Set<BeanDefinition> candidateComponents = scanner
//                .findCandidateComponents(basePackage);
//        for (BeanDefinition candidateComponent : candidateComponents) {
//            if (candidateComponent instanceof AnnotatedBeanDefinition) {
//                // verify annotated class is an interface
//                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
//                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
//                Assert.isTrue(annotationMetadata.isInterface(),
//                        "@FeignClient can only be specified on an interface");
//
//                Map<String, Object> attributes = annotationMetadata
//                        .getAnnotationAttributes(
//                                ConnectClient.class.getCanonicalName());
//
//                String name = getClientName(attributes);
//
//
//                registerConnectClient(registry, annotationMetadata, attributes);
//            }
//        }
//    }
//
//    private void registerConnectClient(BeanDefinitionRegistry registry,
//                                       AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
//        String className = annotationMetadata.getClassName();
//        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(ConnectClientFactory.class);
//        Class<?> interfaceClass = ClassUtils.resolveClassName(className, getClass().getClassLoader());
//        definition.addConstructorArgValue(interfaceClass);
//        String name = getName(attributes);
//        definition.addPropertyValue("name", name);
//        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
//
//        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
//        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
//
//        // 设置为主要的Bean（如果需要）
//        boolean primary = (Boolean) attributes.getOrDefault("primary", false);
//        beanDefinition.setPrimary(primary);
//
//        // 添加其他属性设置（如果有）
//        // definition.addPropertyValue("otherProperty", otherPropertyValue);
//
//        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[]{name});
//        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
//    }
//
//
//    protected ClassPathScanningCandidateComponentProvider getScanner() {
//        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
//            @Override
//            protected boolean isCandidateComponent(
//                    AnnotatedBeanDefinition beanDefinition) {
//                boolean isCandidate = false;
//                if (beanDefinition.getMetadata().isIndependent()) {
//                    if (!beanDefinition.getMetadata().isAnnotation()) {
//                        isCandidate = true;
//                    }
//                }
//                return isCandidate;
//            }
//        };
//    }
//
//    private String getClientName(Map<String, Object> client) {
//        if (client == null) {
//            return null;
//        }
//        String value = (String) client.get("contextId");
//        if (!StringUtils.hasText(value)) {
//            value = (String) client.get("value");
//        }
//        if (!StringUtils.hasText(value)) {
//            value = (String) client.get("name");
//        }
//        if (!StringUtils.hasText(value)) {
//            value = (String) client.get("serviceId");
//        }
//        if (StringUtils.hasText(value)) {
//            return value;
//        }
//
//        throw new IllegalStateException("Either 'name' or 'value' must be provided in @"
//                + ConnectClient.class.getSimpleName());
//    }
//
//    String getName(Map<String, Object> attributes) {
//        String name = (String) attributes.get("serviceId");
//        if (!StringUtils.hasText(name)) {
//            name = (String) attributes.get("name");
//        }
//        if (!StringUtils.hasText(name)) {
//            name = (String) attributes.get("value");
//        }
//        name = resolve(name);
//        return getName(name);
//    }
//
//    static String getName(String name) {
//        if (!StringUtils.hasText(name)) {
//            return "";
//        }
//
//        String host = null;
//        try {
//            String url;
//            if (!name.startsWith("http://") && !name.startsWith("https://")) {
//                url = "http://" + name;
//            } else {
//                url = name;
//            }
//            host = new URI(url).getHost();
//
//        } catch (URISyntaxException e) {
//        }
//        Assert.state(host != null, "Service id not legal hostname (" + name + ")");
//        return name;
//    }
//
//    private String resolve(String value) {
//        if (StringUtils.hasText(value)) {
//            return this.environment.resolvePlaceholders(value);
//        }
//        return value;
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.environment = environment;
//    }
//
//    @Override
//    public void setResourceLoader(ResourceLoader resourceLoader) {
//        this.resourceLoader = resourceLoader;
//    }
//}
