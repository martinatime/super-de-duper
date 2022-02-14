package org.superdeduper.superdeduper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.superdeduper.superdeduper.repository.WatchedFolderRepository

@SpringBootApplication
@EnableScheduling
class SuperDeDuperApplication {

	@Autowired
	WatchedFolderRepository watchedFolderRepository;

	static void main(String[] args) {
		SpringApplication.run(SuperDeDuperApplication, args)
	}
}
