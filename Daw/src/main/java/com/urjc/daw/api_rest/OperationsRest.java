package com.urjc.daw.api_rest;


import com.urjc.daw.models.answer.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

public class OperationsRest<T> {

    public ResponseEntity<T> checkIfExist(Optional<T> t) {
        if (t.isPresent()) {
            return new ResponseEntity<>(t.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<T> safeDelete(Optional<T> t, JpaRepository<T, Long> service) {
        if (t.isPresent()) {
            service.delete(t.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<T> safeCreate(T t, JpaRepository<T, Long> service) {
        service.save(t);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }
}
