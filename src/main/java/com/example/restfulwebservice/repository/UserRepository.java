package com.example.restfulwebservice.repository;

import com.example.restfulwebservice.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
