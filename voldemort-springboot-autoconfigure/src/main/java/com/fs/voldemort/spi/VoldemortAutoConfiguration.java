package com.fs.voldemort.spi;

import com.fs.voldemort.business.BusinessFuncInitializationCaller;
import com.fs.voldemort.business.BusinessFuncRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VoldemortAutoConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BusinessFuncInitializationCaller.init(applicationContext::getBeansWithAnnotation);
    }

}
