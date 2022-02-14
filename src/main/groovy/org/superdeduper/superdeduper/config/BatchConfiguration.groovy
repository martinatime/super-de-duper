package org.superdeduper.superdeduper.config

import groovy.util.logging.Slf4j
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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.superdeduper.superdeduper.batch.processor.StageFileChecksumProcessor
import org.superdeduper.superdeduper.batch.processor.StageFileProcessor
import org.superdeduper.superdeduper.batch.reader.StageFileChecksumReader
import org.superdeduper.superdeduper.batch.reader.StageFileReader
import org.superdeduper.superdeduper.batch.writer.StageFileWriter
import org.superdeduper.superdeduper.model.TrackedFile

@Slf4j
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
    MongoTemplate mongoTemplate

    @Scheduled(initialDelay = 5000L, fixedDelay = 60000L)
    void runJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("JobID", System.currentTimeMillis())
                .toJobParameters()
        jobLauncher.run(stageFiles(), jobParameters)
    }

    Job stageFiles() {
        jobBuilderFactory.get("processStageFiles")
            .incrementer(new RunIdIncrementer())
            .start(processEachIntakeTrackedFileStep())
            .next(processEachStagedTrackedFileForChecksumStep())
            .build()
    }

    @Bean
    Step processEachIntakeTrackedFileStep() {
        stepBuilderFactory.get("processEachIntakeTrackedFileStep")
            .<TrackedFile, TrackedFile> chunk(10)
            .reader(stageFileReader())
            .processor(stageFileProcessor())
            .writer(stageFileWriter())
            .build()
    }

    @Bean
    @StepScope
    StageFileReader stageFileReader() {
        new StageFileReader(mongoTemplate)
    }

    @Bean
    StageFileProcessor stageFileProcessor() {
        new StageFileProcessor()
    }

    @Bean
    StageFileWriter stageFileWriter() {
        new StageFileWriter(template: mongoTemplate)
    }

    @Bean
    Step processEachStagedTrackedFileForChecksumStep() {
        stepBuilderFactory.get("processEachStagedTrackedFileForChecksumStep")
                .<TrackedFile, TrackedFile> chunk(10)
                .reader(stageFileChecksumReader())
                .processor(StageFileChecksumProcessor())
                .writer(stageFileWriter())
                .build()
    }

    @Bean
    @StepScope
    StageFileChecksumReader stageFileChecksumReader() {
        new StageFileChecksumReader(mongoTemplate)
    }

    @Bean
    StageFileChecksumProcessor StageFileChecksumProcessor() {
        new StageFileChecksumProcessor()
    }


    /*

    Job processWatchedFolders() {
        jobBuilderFactory.get("processWatchedFolders")
                .incrementer(new RunIdIncrementer())
                .listener(watchedFolderProcessingListener)
                .next(musicFilePopulationStep())
                .build()
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
     */
}