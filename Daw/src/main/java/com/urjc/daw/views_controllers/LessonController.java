package com.urjc.daw.views_controllers;

import com.urjc.daw.models.lessons.Lesson;
import com.urjc.daw.models.lessons.LessonService;
import com.urjc.daw.models.user.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    UserComponent userComponent;

    @ModelAttribute
    public void addUserToModel(Model model) {
        boolean logged = userComponent.getLoggedUser() != null;
        model.addAttribute("logged", logged);
        if (logged) {
            boolean teacher;
            boolean student;
            if (userComponent.getLoggedUser().getUserType().equals("ROLE_STUDENT")) {
                student = true;
                teacher = false;
            } else {
                student = false;
                teacher = true;
            }
            model.addAttribute("admin", teacher);
            model.addAttribute("visitor", false);
            model.addAttribute("student", student);
            model.addAttribute("idUser", userComponent.getLoggedUser().getId());
        }
    }


    @PostMapping("/lessonSearch")
    public String indexSearch(Model model, @RequestParam("searchText") String searchText) {
        List<Lesson> searchLessons = lessonService.searchLessons(searchText);
        model.addAttribute("lessons", searchLessons);
        return "MainPage";
    }

    @GetMapping("/deleteLessons/{id}")
    public String deleteLessons(Model model, @PathVariable long id) {
        lessonService.deleteLessonById(id);
        return "redirect:/MainPage";
    }

    @PostMapping("/saveLesson")
    public String saveLesson(Model model, Lesson lesson) {
        lessonService.addLesson(lesson);
        return "redirect:/MainPage";
    }

    @GetMapping(path = "/loadMore")
    public String topicMoreButton(Model model, @PageableDefault(size = 10) Pageable page) {
        model.addAttribute("lessons", lessonService.findAll(page));
        return "Lesson";
    }
}
