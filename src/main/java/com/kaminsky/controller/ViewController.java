package com.kaminsky.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kaminsky.entity.User;
import com.kaminsky.repository.OrderRepository;
import com.kaminsky.repository.UserRepository;
import com.kaminsky.views.Views;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/")
public class ViewController {
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    public ViewController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @JsonView(Views.UserSummary.class)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.UserDetails.class)
    public ResponseEntity<User> getUserInfo(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @JsonView(Views.UserDetails.class)
    public void saveNewUser(@Valid @RequestBody User user) {
        userRepository.save(user);
    }

    @PutMapping("{id}")
    @JsonView(Views.UserDetails.class)
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(updatedUser);

    }

    @DeleteMapping("{id}")
    @JsonView(Views.UserDetails.class)
    public void deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException();
        }
    }


}
