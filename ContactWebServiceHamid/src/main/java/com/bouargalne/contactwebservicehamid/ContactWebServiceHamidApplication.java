package com.bouargalne.contactwebservicehamid;

import com.bouargalne.contactwebservicehamid.entities.Contact;
import com.bouargalne.contactwebservicehamid.repositories.ContactRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ContactWebServiceHamidApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactWebServiceHamidApplication.class, args);
	}
	@Bean
	CommandLineRunner initDatabase(ContactRepository contactRepository) {

		return args -> {
			Contact contact1= Contact.builder()
					.first_name("Hamid")
					.last_name("Bouargalne")
					.email("bouargalne.hamid@gmail.com")
					.phone("06 49 94 91 59")
					.job("Data Scientist")
					.build();
			contactRepository.save(contact1);
			Contact contact2= Contact.builder()
					.first_name("khalid")
					.last_name("el-atouani")
					.phone("06 49 94 91 59")
					.job("logistique ingenieur")
					.email("khalid.elatouani@gmail.com")
					.build();
			contactRepository.save(contact2);
			Contact contact3= Contact.builder()
					.first_name("hassan")
					.last_name("ouadouch")
					.email("khalid.elatouani@gmail.com")
					.phone("06 46 14 03 98")
					.job("logistique ingenieur")
					.build();
			contactRepository.save(contact3);
		};
	}


}
