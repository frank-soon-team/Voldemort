package com.fs.voldemort.spi;

import com.fs.voldemort.business.BusinessFuncAvailableCaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VoldemortAutoConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BusinessFuncAvailableCaller.initByAnnotation(aClass -> applicationContext.getBeansWithAnnotation(aClass).values());
    }

}
