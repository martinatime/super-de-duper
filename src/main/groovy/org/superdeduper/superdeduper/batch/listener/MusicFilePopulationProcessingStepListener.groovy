package org.superdeduper.superdeduper.batch.listener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.listener.StepExecutionListenerSupport
import org.springframework.stereotype.Component

@Component
class MusicFilePopulationProcessingStepListener extends StepExecutionListenerSupport {
    static final Logger log = LoggerFactory.getLogger(MusicFilePopulationProcessingStepListener.class);

    @Override
    ExitStatus afterStep(StepExecution stepExecution) {
        ExitStatus exitStatus = super.afterStep(stepExecution)
        log.info("afterStep: ${stepExecution}")
        return exitStatus
    }

    @Override
    void beforeStep(StepExecution stepExecution) {
        super.beforeStep(stepExecution)
        log.info("beforeStep: ${stepExecution}")
    }
}
