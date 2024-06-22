package com.FinalEgg.ServiChacras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.FinalEgg.ServiChacras")
public class ServiChacrasApplication {
	public static void main(String[] args) { SpringApplication.run(ServiChacrasApplication.class, args); }
}
