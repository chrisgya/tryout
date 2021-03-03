package com.chrisgya.tryout.service.user;

import com.chrisgya.tryout.config.RabbitTopicConfig;
import com.chrisgya.tryout.dao.UserDao;
import com.chrisgya.tryout.exception.BadRequestException;
import com.chrisgya.tryout.exception.NotFoundException;
import com.chrisgya.tryout.model.Page;
import com.chrisgya.tryout.model.request.CreateUserRequest;
import com.chrisgya.tryout.model.request.MessageUserDeactivationRequest;
import com.chrisgya.tryout.model.request.MessageUserVerificationRequest;
import com.chrisgya.tryout.model.request.UpdateUserRequest;
import com.chrisgya.tryout.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.chrisgya.tryout.util.ConstantsUtils.*;
import static com.chrisgya.tryout.util.validation.Messages.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final RabbitTemplate rabbitTemplate;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        String verificationToken = UUID.randomUUID().toString();
        createUserRequest.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        createUserRequest.setVerificationCode(verificationToken);
        UserResponse user = userDao.create(createUserRequest);

        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE_NAME,
                RabbitTopicConfig.SEND_USER_VERIFICATION_EMAIL_ROUTING_KEY,
                MessageUserVerificationRequest.builder()
                        .name(user.getFirstName())
                        .email(user.getEmail())
                        .verificationToken(verificationToken).build());

        log.info("createUserRequest:: {}", createUserRequest);
        return user;
    }

    @Override
    public void updateUser(Long id, UpdateUserRequest updateUserRequest) {
        updateUserRequest.setId(id);
        var result = userDao.update(updateUserRequest);
        if (result < 1) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    public void deactivateUser(Long id) {
        var user = getUser(id);
        var result = userDao.delete(id);
        if (result < 1) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE_NAME,
                RabbitTopicConfig.SEND_USER_DEACTIVATION_EMAIL_ROUTING_KEY,
                MessageUserDeactivationRequest.builder()
                        .name(user.getFirstName())
                        .email(user.getEmail()).build());
    }

    @Override
    public void verifyUser(String verificationCode) {
        var result = userDao.verifyUserEmail(verificationCode);
        if (result < 1) {
            throw new BadRequestException(FAILED_USER_VERIFICATION);
        }
    }

    @Override
    public Page<UserResponse> getUsers(Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber == null || pageNumber < 1 ? DEFAULT_PAGE_NUMBER : pageNumber;
        pageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        return userDao.findAll(pageNumber, pageSize);
    }

   private UserResponse getUser(Long id) {
        UserResponse user = userDao.find(id);
        if (user == null) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        return user;
    }

}
