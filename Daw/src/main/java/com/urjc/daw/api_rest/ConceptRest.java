package com.urjc.daw.api_rest;


import com.fasterxml.jackson.annotation.JsonView;
import com.urjc.daw.models.concept.Concept;
import com.urjc.daw.models.concept.ConceptService;
import com.urjc.daw.models.item.Item;
import com.urjc.daw.models.lessons.Lesson;
import com.urjc.daw.models.lessons.LessonService;
import com.urjc.daw.models.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/concept")
public class ConceptRest extends OperationsRest<Concept> {

    interface ConceptDetails extends Concept.BasicInfo,Concept.ItemList,Concept.QuestionList,
            Item.BasicInfo,Question.BasicInfo {}
            
    @Autowired
    ConceptService conceptService;
    @Autowired
    LessonService lessonService;



    @GetMapping(value ="/{id}")
    @JsonView(ConceptDetails.class)
    public ResponseEntity<Concept> getConcept(@PathVariable long id){
        Optional<Concept> concept= conceptService.findByOneId(id);
        return checkIfExist(concept);
    }

    @GetMapping(value ="/lesson/{idLesson}")
    @JsonView(ConceptDetails.class)
    public Page<Concept> getConcepts(@PathVariable long idLesson,@PageableDefault(size = 10)Pageable page){
        return conceptService.findByLesson(lessonService.findOne(idLesson).get(),page);
    }

    @DeleteMapping(value ="/{id}")
    @JsonView(ConceptDetails.class)
    public ResponseEntity<Concept> deleteConcept(@PathVariable long id){
        return safeDelete(conceptService.findByOneId(id),conceptService.repository);
    }

    @PostMapping("/lesson/{idLesson}")
    @JsonView(ConceptDetails.class)
    public ResponseEntity<Concept> saveConcept(@RequestBody Concept concept, @PathVariable long idLesson){
        Optional<Lesson> optionalLesson = lessonService.findOne(idLesson);
        if(optionalLesson.isPresent()){
            Lesson lesson = optionalLesson.get();

            concept.setIdLesson(lesson);
            ResponseEntity<Concept> responseEntity = safeCreate(concept,conceptService.repository);

            lesson.addConcept(concept);

            lessonService.addLesson(lesson);
            return responseEntity;

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(ConceptDetails.class)
    public ResponseEntity<Concept> uploadsImage(@PathVariable long id, @RequestParam("file") MultipartFile file){
        Concept conceptNewFile = conceptService.findByOneId(id).get();
        if(!file.isEmpty()){
            File fileConcept = new File("src/main/resources/static/uploads", file.getOriginalFilename());
            try {
                fileConcept.createNewFile();
                FileOutputStream fout = new FileOutputStream(fileConcept);
                fout.write(file.getBytes());
                fout.close();
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(file.getOriginalFilename())
                        .toUriString();
                conceptNewFile.setPicture(fileDownloadUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>( conceptNewFile,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
