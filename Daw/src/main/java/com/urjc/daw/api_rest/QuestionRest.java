package com.urjc.daw.api_rest;


import com.fasterxml.jackson.annotation.JsonView;
import com.urjc.daw.models.answer.Answer;
import com.urjc.daw.models.concept.Concept;
import com.urjc.daw.models.concept.ConceptService;
import com.urjc.daw.models.item.Item;
import com.urjc.daw.models.question.Question;
import com.urjc.daw.models.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/question")
public class QuestionRest extends OperationsRest<Question> {

    interface QuestionDetails extends Question.BasicInfo,Question.AnswerList,Question.ConceptDet,Question.ItemList,
            Concept.BasicInfo, Answer.BasicInfo, Item.BasicInfo {}

    @Autowired
    QuestionService questionService;

    @Autowired
    ConceptService conceptService;

    @GetMapping(value = "/{id}")
    @JsonView(QuestionDetails.class)
    public ResponseEntity<Question> getQuestion (@PathVariable long id) {
        Optional<Question> question = questionService.findOne(id);
        return checkIfExist(question);
    }

    @GetMapping("/")
    @JsonView(QuestionDetails.class)
    public Collection<Question> getQuestions() {
        return questionService.findAll();
    }

    @PostMapping("/concept/{idConcept}")
    @JsonView(QuestionDetails.class)
    public ResponseEntity<Question> createQuestion (@PathVariable long idConcept){
        Optional<Concept> c = conceptService.findByOneId(idConcept);
        ResponseEntity<Question> responseEntity;
        if (c.isPresent()) {
            Question question = new Question();
            Concept concept = c.get();
            question.setConcept(concept);
            question.randomize(idConcept);
            responseEntity = safeCreate(question,questionService.repository);
            concept.addQuestion(question);
            conceptService.addConcept(concept);
        }else{
            responseEntity=new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @JsonView(QuestionDetails.class)
    public ResponseEntity<Question> deleteQuestion(@PathVariable long id) {
        Optional<Question> deletedBook = questionService.findOne(id);
        return safeDelete(deletedBook,questionService.repository);
    }

}
