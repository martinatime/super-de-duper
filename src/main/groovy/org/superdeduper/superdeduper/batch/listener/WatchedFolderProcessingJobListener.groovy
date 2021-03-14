package org.superdeduper.superdeduper.batch.listener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.stereotype.Component

@Component
class WatchedFolderProcessingJobListener extends JobExecutionListenerSupport {
    static final Logger log = LoggerFactory.getLogger(WatchedFolderProcessingJobListener);

    @Override
    void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution)
        log.info("beforeJob: ${jobExecution}")
    }

    @Override
    void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution)
        log.info("afterJob: ${jobExecution}")
    }
}
