package com.example.demo;

import com.example.demo.services.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
        // Create a Spring application context to access the MyService bean
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoApplication.class);

        // Get the MyService bean
        MyService myService = context.getBean(MyService.class);

        // Call the doSomething() method
        myService.doSomething();

        // Close the application context
        context.close();
	}

}
