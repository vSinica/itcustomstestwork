package ru.vados.itcustomtestwork.Config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"ru.vados.itcustomtestwork.Controller", "ru.vados.itcustomtestwork.Service"})
@EnableJpaRepositories(basePackages = "ru.vados.itcustomtestwork.Repository")
@EntityScan(basePackages = {"ru.vados.itcustomtestwork.Entity"})
public class ServiceConfig {

}
