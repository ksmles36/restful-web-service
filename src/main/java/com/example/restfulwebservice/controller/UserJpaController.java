package com.example.restfulwebservice.controller;

import com.example.restfulwebservice.bean.User;
import com.example.restfulwebservice.dto.RetrieveAllUsersResponse;
import com.example.restfulwebservice.exception.UserNotFoundException;
import com.example.restfulwebservice.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
@RequiredArgsConstructor
public class UserJpaController {

    private final UserRepository userRepository;

//    @GetMapping("/users")
//    public List<User> retrieveAllUsers() {
//        return userRepository.findAll();
//    }

    //Quiz - 사용자 전체목록보기에 사용자 수를 함께 반환하도록 API 수정해보라
    @GetMapping("/users")
    public ResponseEntity<RetrieveAllUsersResponse> retrieveAllUsers() {

        List<User> userList = userRepository.findAll();
        
        //빌더패턴 활용
//        RetrieveAllUsersResponse response = RetrieveAllUsersResponse.builder()
//                .userList(userList)
//                .count(userList.size())
//                .build();

        //정적팩토리메소드 활용
        RetrieveAllUsersResponse response = RetrieveAllUsersResponse.of(userList.size(), userList);
        
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity retrieveUsersById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent())
            throw new UserNotFoundException("id - " + id);

        //HATEOAS 적용위한 코드
        EntityModel entityModel = EntityModel.of(user);
        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(webMvcLinkBuilder.withRel("all-users"));  //all-users -> http://localhost:8088/users
        //따로 uri 를 만들어주지 않아도 알아서 링크 형태로 기능과 연관된 기능의 메소드 호출 uri 를 생성해서 HAL JSON 문법으로 response 해줌

        return ResponseEntity.ok().body(entityModel);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


}
