package com.urjc.daw.models.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public User findUserByName(String name) { return userRepository.findByName(name); }
    public void addUser(User user) { userRepository.save(user);}
    public List<User> findAllUser() { return userRepository.findAll(); }
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

}
