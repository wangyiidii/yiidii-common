package cn.yiidii.boot.util;


import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@SuppressWarnings({"unused", "unchecked"})
public class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static ConfigurableListableBeanFactory beanFactory;

    @Getter
    private static ApplicationContext applicationContext;

    public SpringUtil() {
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtil.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    public static ListableBeanFactory getBeanFactory() {
        return null == beanFactory ? applicationContext : beanFactory;
    }

    public static ConfigurableListableBeanFactory getConfigurableBeanFactory() {
        ConfigurableListableBeanFactory factory;
        if (null != beanFactory) {
            factory = beanFactory;
        } else {
            if (!(applicationContext instanceof ConfigurableApplicationContext)) {
                throw new RuntimeException("No ConfigurableListableBeanFactory from context!");
            }

            factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        }

        return factory;
    }

    public static <T> T getBean(String name) {
        return (T) getBeanFactory().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getBeanFactory().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getBeanFactory().getBean(name, clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    public static String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    public static String getProperty(String key) {
        return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key);
    }

    public static String getApplicationName() {
        return getProperty("spring.application.name");
    }

    public static String[] getActiveProfiles() {
        return null == applicationContext ? null : applicationContext.getEnvironment().getActiveProfiles();
    }

    public static String getActiveProfile() {
        String[] activeProfiles = getActiveProfiles();
        return Objects.nonNull(activeProfiles) && activeProfiles.length > 0 ? activeProfiles[0] : null;
    }

    public static <T> void registerBean(String beanName, T bean) {
        ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        factory.autowireBean(bean);
        factory.registerSingleton(beanName, bean);
    }

    public static void unregisterBean(String beanName) {
        ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        if (factory instanceof DefaultSingletonBeanRegistry) {
            DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
            registry.destroySingleton(beanName);
        } else {
            throw new RuntimeException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
        }
    }

    public static void publishEvent(ApplicationEvent event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }

    }

    public static void publishEvent(Object event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }

    }
}