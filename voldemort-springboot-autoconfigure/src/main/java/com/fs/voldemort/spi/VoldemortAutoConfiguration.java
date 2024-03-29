package com.fs.voldemort.spi;

import com.fs.voldemort.business.BFuncAvailableCaller;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoldemortAutoConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BFuncAvailableCaller.initByAnnotation(aClass -> applicationContext.getBeansWithAnnotation(aClass).values());
    }

}
