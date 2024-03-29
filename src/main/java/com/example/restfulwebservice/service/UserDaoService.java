package com.example.restfulwebservice.service;

import com.example.restfulwebservice.bean.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {

    private static List<User> users = new ArrayList<>();
    private static int usersCount = 3;

    static{
        users.add(new User(1, "aaa", new Date(), "pw111", "111111-111111", "VIP"));
        users.add(new User(2, "bbb", new Date(), "pw222", "222222-222222", "Gold"));
        users.add(new User(3, "ccc", new Date() ,"pw333", "333333-333333", "Silver"));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if(user.getId() == null)
            user.setId(++usersCount);

        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if(user.getId() == id)
                return user;
        }
        return null;
    }

    public User deleteById(int id) {
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.getId() == id) {
                iterator.remove();
                return user;
            }
        }

        return null;
    }

}
