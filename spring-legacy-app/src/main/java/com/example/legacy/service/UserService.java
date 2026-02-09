package com.example.legacy.service;

import com.example.legacy.model.User;
import com.example.legacy.model.UserForm;
import com.example.legacy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User create(UserForm form) {
        User user = new User(form.getName(), form.getEmail(), form.getAge(), form.getDepartment());
        log.info("Creating user: {}", user);
        return userRepository.save(user);
    }

    public Optional<User> update(Long id, UserForm form) {
        return userRepository.findById(id).map(user -> {
            user.setName(form.getName());
            user.setEmail(form.getEmail());
            user.setAge(form.getAge());
            user.setDepartment(form.getDepartment());
            log.info("Updating user: {}", user);
            return userRepository.save(user);
        });
    }

    public void delete(Long id) {
        log.info("Deleting user id={}", id);
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }

    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }
}
