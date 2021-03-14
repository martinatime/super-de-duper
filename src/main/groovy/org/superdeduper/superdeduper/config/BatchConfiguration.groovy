package org.superdeduper.superdeduper.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.MongoItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.superdeduper.superdeduper.batch.listener.MusicFilePopulationProcessingStepListener
import org.superdeduper.superdeduper.batch.listener.WatchedFolderProcessingJobListener
import org.superdeduper.superdeduper.batch.listener.WatchedFolderProcessingStepListener
import org.superdeduper.superdeduper.batch.processor.MusicFilePopulationProcessor
import org.superdeduper.superdeduper.batch.reader.MusicFileReader
import org.superdeduper.superdeduper.batch.writer.MusicFileWriter
import org.superdeduper.superdeduper.batch.writer.WatchedFolderWriter
import org.superdeduper.superdeduper.model.MusicFile
import org.superdeduper.superdeduper.model.WatchedFolder
import org.superdeduper.superdeduper.batch.processor.WatchedFolderProcessor
import org.superdeduper.superdeduper.batch.reader.WatchedFolderReader

@Configuration
@EnableBatchProcessing
class BatchConfiguration {
    @Autowired
    JobLauncher jobLauncher

    @Autowired
    JobBuilderFactory jobBuilderFactory

    @Autowired
    StepBuilderFactory stepBuilderFactory

    @Autowired
    WatchedFolderProcessingJobListener watchedFolderProcessingListener

    @Autowired
    WatchedFolderProcessingStepListener watchedFolderProcessingStepListener

    @Autowired
    MusicFilePopulationProcessingStepListener musicFilePopulationProcessingStepListener

    @Autowired
    MongoTemplate mongoTemplate

    @Scheduled(initialDelay = 5000L, fixedDelay = 60000L)
    void runJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("JobID", System.currentTimeMillis())
                .toJobParameters()
        jobLauncher.run(processWatchedFolders(), jobParameters)
    }

    Job processWatchedFolders() {
        jobBuilderFactory.get("processWatchedFolders")
                .incrementer(new RunIdIncrementer())
                .listener(watchedFolderProcessingListener)
                .start(processEachWatchedFolderStep())
                .next(musicFilePopulationStep())
                .build()
    }

    @Bean
    Step processEachWatchedFolderStep() {
        stepBuilderFactory.get("processEachWatchedFolderStep")
                .listener(watchedFolderProcessingStepListener)
                .<WatchedFolder, WatchedFolder> chunk(10)
                .reader(watchFolderReader())
                .processor(watchedFolderProcessor())
                .writer(watchedFolderWriter())
                .build()
    }

    @Bean
    @StepScope
    WatchedFolderReader watchFolderReader() {
        new WatchedFolderReader(mongoTemplate)
    }

    @Bean
    WatchedFolderProcessor watchedFolderProcessor() {
        new WatchedFolderProcessor()
    }

    @Bean
    WatchedFolderWriter watchedFolderWriter() {
        new WatchedFolderWriter()
    }

    @Bean
    Step musicFilePopulationStep() {
        stepBuilderFactory.get("musicFilePopulationStep")
            .listener(musicFilePopulationProcessingStepListener)
            .< MusicFile, MusicFile> chunk(50)
            .reader(musicFileReader())
            .processor(musicFilePopulationProcessor())
            .writer(musicFileWriter())
            .build()
    }

    @Bean
    @StepScope
    MongoItemReader<MusicFile> musicFileReader() {
        new MusicFileReader(mongoTemplate)
    }

    @Bean
    MusicFilePopulationProcessor musicFilePopulationProcessor() {
        new MusicFilePopulationProcessor()
    }

    @Bean
    MusicFileWriter musicFileWriter() {
        new MusicFileWriter(template: mongoTemplate);
    }
}