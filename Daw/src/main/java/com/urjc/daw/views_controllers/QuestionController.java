package com.urjc.daw.views_controllers;

import com.urjc.daw.models.concept.Concept;
import com.urjc.daw.models.concept.ConceptService;
import com.urjc.daw.models.item.Item;
import com.urjc.daw.models.item.ItemService;
import com.urjc.daw.models.question.Question;
import com.urjc.daw.models.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class QuestionController {

    @Autowired
    public QuestionService questionService;

    @Autowired
    public ConceptService conceptService;

    @Autowired
    public ItemService itemService;

    @GetMapping("/deleteQuestion/{id}")
    public String deleteQuestion (Model model, @PathVariable long id) {
        questionService.deleteQuestionById(id);
        return "redirect:/TeacherConcept_View";
    }

    @PostMapping("/saveQuestion/{idQuestion}")
    public String saveQuestion(@Valid Question question, Model model, SessionStatus sessionStatus) {
        questionService.addQuestion(question);
        sessionStatus.setComplete();
        return "redirect:/TeacherConceptView";
    }


    @GetMapping(path = "/createQuestion/{idConcept}")
    public String createQuestion(Model model, @PathVariable long idConcept) {
        Optional<Concept> concept = conceptService.findByOneId(idConcept);
        if (concept.isPresent()) {
            List<Item> selected = new ArrayList<>();
            int type = (int) (Math.random() * 4);
            int item = 0;
            String info = "";
            String attr = "";
            Question question;
            switch (type) {
                case 0:
                    info = "¿Cuáles son " + concept.get().getTitle() + " ?";
                    attr = "question0";
                    break;
                case 1:
                    List<Item> itemList = itemService.findItemByIdConceptIs(concept.get());
                    item = (int) (Math.random() * itemList.size() - 1);
                    info = "¿" + itemList.get(item).getInfo() + " es un elemento de " + concept.get().getTitle() + " ?";
                    attr = "question1";
                    break;
                case 2:
                    List<Item> itemsCorrect = itemService.findByStateAndIdConcept(concept.get(),true);
                    item = (int) (Math.random() * itemsCorrect.size() - 1);
                    itemsCorrect.remove(item);
                    String complementInfo = "";
                    for (int i = 0; i < itemsCorrect.size(); i++) {
                        complementInfo += itemsCorrect.get(i).getInfo() + ", ";
                    }
                    info = "¿Qué elemento falta en " + complementInfo + " para completar la lista de " + concept.get().getTitle() + " ?";
                    attr = "question2";
                    break;
                case 3:
                    List<Item> itemRandom = itemService.findItemByIdConceptIs(concept.get());

                    int numItems = (int) Math.floor(Math.random() * (5 - 3 + 1) + 3);
                    String complement = "";
                    for (int i = 0; i < numItems; i++) {
                        item = (int) (Math.random() * itemRandom.size() - 1);
                        selected.add(itemRandom.get(item));
                        complement += itemRandom.get(item).getInfo() + ", ";
                        itemRandom.remove(item);
                    }
                    info = "¿Qué elementos de " + complement + " no son parte de " + concept.get().getTitle() + " ?";
                    attr = "question3";

                    break;

            }
            question = new Question(type, info);
            model.addAttribute(attr, true);
            model.addAttribute("options", selected);
            model.addAttribute("question", question);
            concept.get().addQuestion(question);
            question.setConcept(concept.get());
            questionService.addQuestion(question);
            conceptService.addConcept(concept.get());
        }
        return "AddnewQuestions";

    }

}
