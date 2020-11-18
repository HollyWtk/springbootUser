package com.yhh.core.utils;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @sinse：0.1
 * 
 * @author：lxy 2018年1月12日 下午1:00:27
 */
@Slf4j
@Component
public class SpringBeanFactoryUtils implements ApplicationContextAware  {

    public static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanFactoryUtils.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext对象
     * 
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据bean的id来查找对象
     * 
     * @param id
     * @return
     */

    public static Object getBeanById(String id) {
        try {
            return applicationContext.getBean(id);
        } catch (Exception e) {
            log.warn("沒有找到名稱為“" + id + "”的Bean", e);
        }
        return null;
    }

    /**
     * 根据bean的class来查找对象
     * 
     * @param c
     * @return
     */
    public static <T> T getBeanByClass(Class<T> c) {
        try {
            return (T) applicationContext.getBean(c);
        } catch (Exception e) {
            log.warn("沒有找到类型為“" + c + "”的Bean,{}", e.getMessage());
        }
        return null;
    }

    public static String getMessage(String key) {
        return applicationContext.getMessage(key, null, Locale.getDefault());
    }

    public synchronized <T> T getBeanNotNull(Class<T> clazz) {
        // 获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext
                .getAutowireCapableBeanFactory();
        // 创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        // 动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition(buildBeanName(clazz),
                beanDefinitionBuilder.getBeanDefinition());
        // 获取动态注册的bean.
        T beanByClass = getBeanByClass(clazz);
        return beanByClass;
    }

    private static String buildBeanName(Class<?> clazz) {
        return clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
    }

}
