package com.example.Install.AOP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.example.Install.AOP.service.MyService;

@SpringBootApplication
public class InstallAopApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(InstallAopApplication.class, args);
		
		MyService myService = context.getBean(MyService.class);
		myService.doSomething();
	}

}
