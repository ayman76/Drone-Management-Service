package com.example.technicaltask.config;

import com.example.technicaltask.model.converter.IntToModelConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        //Register Integer to Model Converter
        modelMapper.addConverter(new IntToModelConverter());
        return modelMapper;
    }
}
