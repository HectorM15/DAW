package com.urjc.daw.views_controllers;

import com.urjc.daw.models.concept.Concept;
import com.urjc.daw.models.concept.ConceptService;
import com.urjc.daw.models.item.Item;
import com.urjc.daw.models.item.ItemRepository;
import com.urjc.daw.models.item.ItemService;
import com.urjc.daw.models.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    ConceptService conceptService;

    @Autowired
    ItemRepository itemRepository;


    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/deleteItem/{idItem}/{idConcept}")
    public String deleteItem(Model model, @PathVariable long idItem, @PathVariable long idConcept) {
        itemService.deleteItemById(idItem);

        return "redirect:/TeacherConceptView/{idConcept}";
    }

    @PostMapping("/saveItem/{idConcept}")
    public String saveItem(Model model, Item item, @PathVariable long idConcept) {
        Optional<Concept> c = conceptService.findByOneId(idConcept);
        if (c.isPresent()) {
            Concept concept = c.get();
            concept.addItem(item);
            conceptService.addConcept(concept);
        }
        return "redirect:/TeacherConceptView/{idConcept}";
    }


    @GetMapping(path = "/loadMoreItems")
    public String topicMoreButton(Model model, @PageableDefault(size = 3) Pageable page) {
        //Optional<concept> c = conceptService.findByOneId(idConcept);
        model.addAttribute("items", itemService.findAll(page));
        return "Items";
    }
}
