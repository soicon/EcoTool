package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.RunnerLogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RunnerLog and its DTO RunnerLogDTO.
 */
@Mapper(componentModel = "spring", uses = {ApiVersionMapper.class, DataVersionMapper.class, InputVersionMapper.class, SourceMapper.class, QuestionMapper.class, AnswerMapper.class})
public interface RunnerLogMapper extends EntityMapper<RunnerLogDTO, RunnerLog> {

    @Mapping(source = "apiversion.id", target = "apiversionId")
    @Mapping(source = "apiversion.version", target = "apiversionVersion")
    @Mapping(source = "dataversion.id", target = "dataversionId")
    @Mapping(source = "dataversion.version", target = "dataversionVersion")
    @Mapping(source = "inputversion.id", target = "inputversionId")
    @Mapping(source = "inputversion.version", target = "inputversionVersion")
    @Mapping(source = "source.id", target = "sourceId")
    @Mapping(source = "source.path", target = "sourcePath")
    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.question_text", target = "questionQuestion_text")
    @Mapping(source = "answer.id", target = "answerId")
    @Mapping(source = "answer.answer_text", target = "answerAnswer_text")
    RunnerLogDTO toDto(RunnerLog runnerLog);

    @Mapping(source = "apiversionId", target = "apiversion")
    @Mapping(source = "dataversionId", target = "dataversion")
    @Mapping(source = "inputversionId", target = "inputversion")
    @Mapping(source = "sourceId", target = "source")
    @Mapping(source = "questionId", target = "question")
    @Mapping(source = "answerId", target = "answer")
    RunnerLog toEntity(RunnerLogDTO runnerLogDTO);

    default RunnerLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        RunnerLog runnerLog = new RunnerLog();
        runnerLog.setId(id);
        return runnerLog;
    }
}
