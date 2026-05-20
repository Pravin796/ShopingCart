package com.pravin.shopping_cart.controller;


import com.pravin.shopping_cart.dto.UserDto;
import com.pravin.shopping_cart.entities.User;
import com.pravin.shopping_cart.mappers.UserMapperUtil;
import com.pravin.shopping_cart.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public Iterable<UserDto> getAllUser(
            @RequestParam(required = false,defaultValue = "") String sort
    ){
//        Sort sortObj = Sort.by("id"); // default
//
//        if (sort != null) {
//            sortObj = Sort.by(sort);
//        }

        if(!Set.of("name", "email").contains(sort))
            sort = "name";

       return userRepository.findAll(Sort.by(sort))
               .stream()
//               .map((User user) -> new UserDto(user.getId(), user.getName(), user.getEmail()))
               .map(UserMapperUtil::toDto)
               .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){

        User user =  userRepository.findById(id).orElse(null);

        if(user == null){
            return ResponseEntity.notFound().build();
        }

//        var userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
//        return ResponseEntity.ok(userDto);
        return ResponseEntity.ok(UserMapperUtil.toDto(user));
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto data){
        return data;
    }
}
