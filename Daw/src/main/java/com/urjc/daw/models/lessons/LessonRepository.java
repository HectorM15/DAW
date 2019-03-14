package com.urjc.daw.models.lessons;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface LessonRepository extends JpaRepository<Lesson, Long>, PagingAndSortingRepository<Lesson, Long> {

    @Query(value = "SELECT * FROM lesson WHERE UPPER(name) LIKE CONCAT('%', CONCAT(UPPER(:name), '%'))", nativeQuery = true)
    List<Lesson> searchByName(@Param("name") String name);


    @Override
    Optional<Lesson> findById(Long aLong);

    Page<Lesson> findAll(Pageable page);







}