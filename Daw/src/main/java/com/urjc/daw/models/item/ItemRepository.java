package com.urjc.daw.models.item;


import com.urjc.daw.models.concept.Concept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemByState(boolean state);
    Page<Item> findItemByIdConcept(Pageable page,Concept IdConcept);
    List<Item> findItemByIdConceptIs(Concept IdConcept);
    List<Item> findByStateAndIdConcept(boolean state,Concept IdConcept);
}