package com.example.restfulwebservice.dto;

import com.example.restfulwebservice.bean.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Builder
public class RetrieveAllUsersResponse {

    private int count;
    private List<User> userList;

    private RetrieveAllUsersResponse(int count, List<User> userList) {
        this.count = count;
        this.userList = userList;
    }

    public static RetrieveAllUsersResponse of(int count, List<User> userList) {
        return new RetrieveAllUsersResponse(count, userList);
    }
}
