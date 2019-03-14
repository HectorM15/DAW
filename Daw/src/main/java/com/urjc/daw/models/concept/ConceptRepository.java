package com.urjc.daw.models.concept;

import com.urjc.daw.models.lessons.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ConceptRepository extends JpaRepository<Concept, Long> {
        Optional<Concept> findByIdConcept(long IdConcept);

    Page<Concept> findByIdLessonEquals(Lesson idLesson, Pageable page);
}