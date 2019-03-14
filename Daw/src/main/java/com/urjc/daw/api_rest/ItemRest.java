package com.urjc.daw.api_rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.urjc.daw.models.concept.Concept;
import com.urjc.daw.models.concept.ConceptService;
import com.urjc.daw.models.item.Item;
import com.urjc.daw.models.item.ItemService;
import com.urjc.daw.models.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping ("/api/items")
public class ItemRest extends OperationsRest<Item> {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ConceptService conceptService;

    interface ItemsDetails extends Item.BasicInfo,Item.ConceptList,Item.QuestionList,
            Question.BasicInfo,Concept.BasicInfo {}


    @GetMapping(value = "/{id}")
    @JsonView(ItemsDetails.class)
    public ResponseEntity<Item> getItem (@PathVariable long id) {
        Optional<Item> item = itemService.findOne(id);
        return checkIfExist(item);
    }

    @GetMapping(value ="/concept/{idConcept}")
    @JsonView(ConceptRest.ConceptDetails.class)
    public Page<Item> getConcepts(@PathVariable long idConcept, @PageableDefault(size = 10) Pageable page){
        return itemService.findItemByIdConcept(page,conceptService.findByOneId(idConcept).get());
    }

    @PostMapping("/concept/{idConcept}")
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(ItemsDetails.class)
    public ResponseEntity<Item> createItem(@PathVariable long idConcept,@RequestBody Item item){
        Optional<Concept> c = conceptService.findByOneId(idConcept);
        if (c.isPresent()) {
            Concept concept = c.get();
            item.setIdConcept(c.get());
            ResponseEntity<Item> responseEntity = safeCreate(item,itemService.repository);
            concept.addItem(item);
            conceptService.addConcept(concept);
            return responseEntity;
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @JsonView(ItemsDetails.class)
    public ResponseEntity<Item> deteleItem(@PathVariable long id){
        Optional<Item> deleteItem = itemService.findByOneId(id);
        return safeDelete(deleteItem,itemService.repository);
    }
}
