package ru.project.CardManagementService;

import org.springframework.boot.SpringApplication;

public class TestCardManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(CardManagementServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
