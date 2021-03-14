package org.superdeduper.superdeduper.commandrunner

import groovy.cli.picocli.CliBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.superdeduper.superdeduper.service.WatchedFolderService

@Order(value=1)
@Component
class WatchFolderCommandLineRunner implements CommandLineRunner {
    @Autowired
    WatchedFolderService watchedFolderService;

    @Override
    void run(String... args) throws Exception {
        CliBuilder cli = new CliBuilder(usage: "run SpringBootApplication")
        cli.a(args:1, argName: "watchedFolder", "Adds a folder to the watches")
        cli.r(args:1, argName: "watchedFolder", "Removes a folder from the watches")

        def options = cli.parse(args)
        assert options
        if (options.a) {
            println "adding ${options.a}"
            watchedFolderService.addWatchedFolder(options.a)
        }
        if (options.r) {
            println "removing ${options.r}"
            watchedFolderService.removeWatchedFolder(options.r)
        }
    }
}
