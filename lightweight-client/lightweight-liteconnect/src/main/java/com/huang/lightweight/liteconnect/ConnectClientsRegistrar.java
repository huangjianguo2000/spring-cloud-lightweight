package com.huang.lightweight.liteconnect;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.Set;

/**
 * Registrar for registering Feign clients.
 *
 * @Author lightweight
 * @Date 2023/5/29 16:58
 */
public class ConnectClientsRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerConnectionClients(importingClassMetadata, registry);
    }

    private void registerConnectionClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        String basePackage = ClassUtils.getPackageName(metadata.getClassName());

        // Create a scanner for scanning candidate components
        ClassPathScanningCandidateComponentProvider scanner = getScanner();

        // Add filter to include components annotated with @ConnectClient
        TypeFilter connectClientFilter = new AnnotationTypeFilter(ConnectClient.class);
        scanner.addIncludeFilter(connectClientFilter);

        // Find candidate components in the base package
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
                Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(ConnectClient.class.getName());

                String className = candidateComponent.getBeanClassName();
                if (className != null){
                    Class<?> interfaceClass = ClassUtils.resolveClassName(className, getClass().getClassLoader());

                    // Create a BeanDefinitionBuilder for the ConnectClientFactory
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ConnectClientFactory.class);

                    // Add the interface class as a constructor argument
                    builder.addConstructorArgValue(interfaceClass);

                    AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(attributes);
                    String[] substring = className.split("\\.");
                    String beanName = lowercaseFirstLetter(substring[substring.length - 1]);
                    if (annotationAttributes != null) {
                        String name = annotationAttributes.getString("beanName");
                        if(!"".equals(name)){
                            beanName = name;
                        }
                        // If you want to set the name, you can uncomment the following line and add a setName() method in ConnectClientFactory
                        // addPropertyValue -> key value
                        boolean primary = annotationAttributes.getBoolean("primary");
                        builder.setPrimary(primary);
                    }

                    AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
                    beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);

                    if (beanName != null) {
                        registry.registerBeanDefinition(beanName, beanDefinition);
                    }
                }
            }
        }
    }

    /**
     * Convert the first letter of a string to lowercase.
     */
    public static String lowercaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Get the scanner for scanning candidate components.
     */
    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(@NonNull AnnotatedBeanDefinition beanDefinition) {
                if (!beanDefinition.getMetadata().isInterface()) {
                    Assert.isTrue(beanDefinition.getMetadata().isInterface(),
                            "@ConnectClient can only be specified on an interface");
                    return false;
                }
                if (beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation()) {
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}
