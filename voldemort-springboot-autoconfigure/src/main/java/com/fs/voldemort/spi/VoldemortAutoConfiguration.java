package com.fs.voldemort.spi;

import com.fs.voldemort.business.BusinessFuncInitializationCaller;
import com.fs.voldemort.business.BusinessFuncRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VoldemortAutoConfiguration {

    @Bean
    public BusinessFuncRegistry businessFuncRegistry(ApplicationContext context) {
        BusinessFuncInitializationCaller.init(context::getBeansWithAnnotation);
        return null;
    }

}
