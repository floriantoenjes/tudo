package com.floriantoenjes.tudo;

import com.floriantoenjes.tudo.contactrequest.ContactRequestSentValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TudoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TudoApplication.class, args);
	}
}
