package org.superdeduper.superdeduper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.superdeduper.superdeduper.model.WatchedFolder
import org.superdeduper.superdeduper.repository.WatchedFolderRepository

@SpringBootApplication
@EnableScheduling
class SuperDeDuperApplication implements  CommandLineRunner {

	@Autowired
	WatchedFolderRepository watchedFolderRepository;

	static void main(String[] args) {
		SpringApplication.run(SuperDeDuperApplication, args)
	}

	@Override
	void run(String... args) throws Exception {
		List<WatchedFolder> watchedFolders = watchedFolderRepository.findAll()
		watchedFolders.each {println it}
	}
}
