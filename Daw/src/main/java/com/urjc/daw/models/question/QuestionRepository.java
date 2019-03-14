package com.urjc.daw.models.question;

import com.urjc.daw.models.concept.Concept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByidConcept(Concept idConcept);
    List<Question> findAll();
    Question findByidQuestion(long idQuestion);
    //question findByConceptAndId(concept concept, long id);
}
