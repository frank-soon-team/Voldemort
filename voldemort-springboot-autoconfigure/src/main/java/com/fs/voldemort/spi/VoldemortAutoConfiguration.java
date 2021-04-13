package com.fs.voldemort.spi;

import com.fs.voldemort.business.BusinessFuncRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoldemortAutoConfiguration {

    @Bean
    public BusinessFuncRegistry businessFuncRegistry(ApplicationContext context) {
        return new BusinessFuncRegistry(annotationType->context.getBeansWithAnnotation(annotationType));
    }

}
