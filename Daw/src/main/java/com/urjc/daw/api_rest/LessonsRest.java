package com.urjc.daw.api_rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.urjc.daw.models.concept.Concept;
import com.urjc.daw.models.lessons.Lesson;
import com.urjc.daw.models.lessons.LessonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lesson")
public class LessonsRest extends OperationsRest<Lesson> {

    interface LessonDetails extends Lesson.BasicInfo,Lesson.ConceptList, Concept.BasicInfo{}

    @Autowired
    LessonService lessonService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(LessonDetails.class)
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson){
        return safeCreate(lesson,lessonService.repository);
    }

    @GetMapping(value = "/{id}")
    @JsonView(LessonDetails.class)
    public ResponseEntity<Lesson> getLesson(@PathVariable long id) {
        Optional<Lesson> lesson=lessonService.findOne(id);
        return checkIfExist(lesson);
    }

    @GetMapping(value = "/pag")
    @JsonView(LessonDetails.class)
    public Page<Lesson> getPage (Pageable page){
        return lessonService.findAll(page);
    }

    @PutMapping("/{id}")
    @JsonView(LessonDetails.class)
    public Lesson updateLesson(@PathVariable long id, @RequestBody Lesson updatedLesson){
        lessonService.findOne(id).get();
        updatedLesson.setIdLesson(id);
        lessonService.addLesson(updatedLesson);
        return updatedLesson;
    }

    @DeleteMapping("/{id}")
    @JsonView(LessonDetails.class)
    public ResponseEntity<Lesson> deteleLesson(@PathVariable long id){
        Optional<Lesson> deleteLesson = lessonService.findOne(id);
        return safeDelete(deleteLesson,lessonService.repository);
    }

    @GetMapping("/lessonSearch")
    public String indexSearch(Model model, @RequestParam("searchText") String searchText) {
        List<Lesson> searchLessons = lessonService.searchLessons(searchText);
        model.addAttribute("lessons", searchLessons);
        return "MainPage";
    }
}
