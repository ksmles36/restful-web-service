package com.example.restfulwebservice.controller;

import com.example.restfulwebservice.bean.User;
import com.example.restfulwebservice.exception.UserNotFoundException;
import com.example.restfulwebservice.service.UserDaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserDaoService userDaoService;

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = userDaoService.findOne(id);

        if (user == null)
            throw new UserNotFoundException(String.format("ID[%s] not found", id));

        //HATEOAS 적용위한 코드
        EntityModel entityModel = EntityModel.of(user);
        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(webMvcLinkBuilder.withRel("all-users"));  //all-users -> http://localhost:8088/users
        //따로 uri 를 만들어주지 않아도 알아서 링크 형태로 기능과 연관된 기능의 메소드 호출 uri 를 생성해서 HAL JSON 문법으로 response 해줌

        return entityModel;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) {
        User deletedUser = userDaoService.deleteById(id);

        if(deletedUser == null)
            throw new UserNotFoundException(String.format("ID[%s] not found", id));

        return ResponseEntity.noContent().build(); //성공응답이라도 상황에 맞게 status 응답해주는 것이 바람직함.
    }


}
