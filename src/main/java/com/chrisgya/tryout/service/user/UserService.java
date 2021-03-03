package com.chrisgya.tryout.service.user;

import com.chrisgya.tryout.model.Page;
import com.chrisgya.tryout.model.request.CreateUserRequest;
import com.chrisgya.tryout.model.request.UpdateUserRequest;
import com.chrisgya.tryout.model.response.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest createUserRequest);

    void updateUser(Long id, UpdateUserRequest updateUserRequest);

    void deactivateUser(Long id);

    void verifyUser(String verificationCode);

    Page<UserResponse> getUsers(Integer pageNumber, Integer pageSize);
}
