package com.urjc.daw.models.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    Optional<User> findByIdUser(long idUser);
}