package EmployeeManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJms
public class ManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}

}
