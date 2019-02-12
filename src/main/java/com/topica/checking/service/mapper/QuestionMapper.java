package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.QuestionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Question and its DTO QuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {SourceMapper.class})
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {

    @Mapping(source = "source.id", target = "sourceId")
    QuestionDTO toDto(Question question);

    @Mapping(source = "sourceId", target = "source")
    @Mapping(target = "answer", ignore = true)
    Question toEntity(QuestionDTO questionDTO);

    default Question fromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }
}
