package com.axisbank.project2;

//import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//import com.axisbank.project2.service.FilesStorageService;

@SpringBootApplication
public class RecordmanagementApplication {

	/*@Resource
	 FilesStorageService storageService;*/
	
	public static void main(String[] args) {
		SpringApplication.run(RecordmanagementApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	  /*public void run(String... arg) throws Exception {
	    storageService.deleteAll();
	    storageService.init();
	  }*/

}
