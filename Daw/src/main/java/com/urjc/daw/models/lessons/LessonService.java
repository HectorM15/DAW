package com.urjc.daw.models.lessons;

import com.urjc.daw.models.concept.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    @Autowired
    public LessonRepository repository;

    public void deleteLessonById(Long id) {
        repository.deleteById(id);
    }

    public Page<Lesson> findAll(Pageable page) {
        return repository.findAll(page);
    }

    public Optional<Lesson> findOne(Long id) {
        return repository.findById(id);
    }

    public void addLesson(Lesson lesson) {
        repository.save(lesson);
    }

    public List<Lesson> searchLessons(String name) {
        return repository.searchByName(name);
    }

}
