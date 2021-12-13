package com.sg.guess_the_number_tests;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "com.sg.guess_the_number", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CommandLineRunner.class))
@EnableAutoConfiguration
public class TestNumberGameConfiguration {
}
