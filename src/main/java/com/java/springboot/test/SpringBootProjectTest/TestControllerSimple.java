package com.java.springboot.test.SpringBootProjectTest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllerSimple {

	@RequestMapping("/hello")
	public String test() {
		return "Spring boot application using Spring initializer";
	}
}
