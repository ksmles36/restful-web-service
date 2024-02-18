package com.example.restfulwebservice.controller;

import com.example.restfulwebservice.bean.AdminUser;
import com.example.restfulwebservice.bean.User;
import com.example.restfulwebservice.exception.UserNotFoundException;
import com.example.restfulwebservice.service.UserDaoService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    private final UserDaoService userDaoService;

    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUserForAdmin(@PathVariable int id) {
        User user = userDaoService.findOne(id);

        AdminUser adminUser = new AdminUser();
        if (user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        } else {
            BeanUtils.copyProperties(user, adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "password", "ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsersForAdmin() {
        List<User> users = userDaoService.findAll();

        List<AdminUser> adminUsers = new ArrayList<>();

        for (User user : users) {
            AdminUser adminUser = new AdminUser();
            BeanUtils.copyProperties(user, adminUser);
            adminUsers.add(adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "password", "ssn");
        SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(adminUsers);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }



}