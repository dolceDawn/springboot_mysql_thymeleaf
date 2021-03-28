package com.epam.weather.service;

import com.epam.weather.controller.UserController;
import com.epam.weather.repository.UserRepository;
import com.epam.weather.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    public void saveUsers(String name, String email, String password) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassWord(password);
        userRepository.save(user);

        System.out.println(1/0);

        log.info(user.toString()+" saved to the repo");

    }

    public List<User> findByEmail(String email) {

        return userRepository.findByEmail(email);

    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

}
