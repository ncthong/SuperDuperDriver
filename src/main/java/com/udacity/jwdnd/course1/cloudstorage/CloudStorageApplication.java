package com.udacity.jwdnd.course1.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudStorageApplication {

	public static void main(String[] args) {
/*		System.setProperty("spring.devtools.restart.enabled", "false");*/
		SpringApplication.run(CloudStorageApplication.class, args);
	}

}
