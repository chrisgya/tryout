package com.chrisgya.tryout.controller;

import com.chrisgya.tryout.model.Page;
import com.chrisgya.tryout.model.request.CreateUserRequest;
import com.chrisgya.tryout.model.request.UpdateUserRequest;
import com.chrisgya.tryout.model.response.UserResponse;
import com.chrisgya.tryout.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User management", description = "Manage user information")
@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final String[] DISALLOWED_FIELDS = new String[]{};

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        binder.setDisallowedFields(DISALLOWED_FIELDS);
    }

    @Operation(summary = "Get users", description = "This will return users. It also support pagination. The default 'page number' and 'page size' are 1 and 15 respectively")
    @GetMapping("users")
    public Page<UserResponse> getUsers(@RequestParam(required = false) Integer pageNumber,
                                       @RequestParam(required = false) Integer pageSize) {
        return userService.getUsers(pageNumber, pageSize);
    }

    @Operation(summary = "Create a new user", description = "This endpoint will create a new user and send verification code to the user for user verification")
    @PostMapping("user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    @Operation(summary = "Verify user account", description = "This endpoint accept verification code to verify user email. The verification code was sent to the user registered email when a user is created")
    @PutMapping("user/verify/{verificationCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyUser(@PathVariable String verificationCode) {
        userService.verifyUser(verificationCode);
    }

    @Operation(summary = "Update existing user", description = "This endpoint will update a existing user information provided a valid ID is supplied")
    @PutMapping("user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        userService.updateUser(id, updateUserRequest);
    }

    @Operation(summary = "Deactivate user account", description = "This endpoint will deactivate the user with the provided ID")
    @DeleteMapping("user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
    }

}
