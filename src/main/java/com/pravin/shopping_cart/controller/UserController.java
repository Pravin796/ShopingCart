package com.pravin.shopping_cart.controller;


import com.pravin.shopping_cart.dto.UserDto;
import com.pravin.shopping_cart.entities.User;
import com.pravin.shopping_cart.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public Iterable<UserDto> getAllUser(){
       return userRepository.findAll()
               .stream()
               .map((User user) -> new UserDto(user.getId(), user.getName(), user.getEmail()))
               .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){

        User user =  userRepository.findById(id).orElse(null);

        if(user == null){
            return ResponseEntity.notFound().build();
        }

        var userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.ok(userDto);
    }
}
