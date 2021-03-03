package com.chrisgya.tryout.controller;

import com.chrisgya.tryout.model.Page;
import com.chrisgya.tryout.model.RoleEnum;
import com.chrisgya.tryout.model.request.CreateUserRequest;
import com.chrisgya.tryout.model.request.UpdateUserRequest;
import com.chrisgya.tryout.model.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static com.chrisgya.tryout.util.ConstantUtil.BASE_URI;
import static com.chrisgya.tryout.util.ConstantsUtils.*;
import static com.chrisgya.tryout.util.FakerUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    private String createURI(String path) {
        return UriComponentsBuilder.fromUriString(String.format(BASE_URI, port, path)).build().toUriString();
    }


    @Test
    void getUsersWithDefaultTest() {

        webTestClient.get()
                .uri(createURI("users"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Page<UserResponse>>() {
                })
                .consumeWith(res -> {
                    var body = res.getResponseBody();

                    assertThat(body.getTotalPages()).isGreaterThanOrEqualTo(1);
                    assertThat(body.getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
                    assertThat(body.getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
                    assertThat(body.getContent().isEmpty()).isFalse();
                    assertThat(body.getContent().size()).isLessThanOrEqualTo(DEFAULT_PAGE_SIZE);

                    var content = body.getContent().get(0);
                    assertThat(content.getId()).isNotNull();
                    assertThat(content.getTitle()).isNotNull();
                    assertThat(content.getFirstName()).isNotNull();
                    assertThat(content.getLastName()).isNotNull();
                    assertThat(content.getEmail()).isNotNull();
                    assertThat(content.getPassword()).isNotNull();
                    assertThat(content.getMobile()).isNotNull();
                    assertThat(content.getRole()).isNotNull();
                    assertThat(content.getStatus()).isNotNull();
                });

    }

    @Test
    void getUsersWithSpecificPageNumberAndSizeTest() {
        var path = String.format("users?pageNumber=%d&pageSize=%d", 1, 2);

        webTestClient.get()
                .uri(createURI(path))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Page<UserResponse>>() {
                })
                .consumeWith(res -> {
                    var body = res.getResponseBody();

                    assertThat(body.getTotalPages()).isGreaterThanOrEqualTo(1);
                    assertThat(body.getPageNumber()).isEqualTo(1);
                    assertThat(body.getPageSize()).isEqualTo(2);
                    assertThat(body.getContent().isEmpty()).isFalse();
                    assertThat(body.getContent().size()).isLessThanOrEqualTo(2);

                    var content = body.getContent().get(0);
                    assertThat(content.getId()).isNotNull();
                    assertThat(content.getTitle()).isNotNull();
                    assertThat(content.getFirstName()).isNotNull();
                    assertThat(content.getLastName()).isNotNull();
                    assertThat(content.getEmail()).isNotNull();
                    assertThat(content.getPassword()).isNotNull();
                    assertThat(content.getMobile()).isNotNull();
                    assertThat(content.getRole()).isNotNull();
                    assertThat(content.getStatus()).isNotNull();
                });

    }

    @Test
    void createUserTest() {
        var path = "user";

        var createUserRequest = CreateUserRequest.builder()
                .title(getTitle())
                .firstName(getFirstName())
                .lastName(getLastName())
                .email(getEmailAddress())
                .mobile(getMobileNumber())
                .password(getPassword())
                .role(RoleEnum.USER.name())
                .build();

        webTestClient.post()
                .uri(createURI(path))
                .body(Mono.just(createUserRequest), CreateUserRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<UserResponse>() {
                })
                .consumeWith(res -> {
                    var userResponse = res.getResponseBody();

                    assertThat(userResponse.getId()).isNotNull();
                    assertThat(userResponse.getTitle()).isEqualTo(createUserRequest.getTitle());
                    assertThat(userResponse.getFirstName()).isEqualTo(createUserRequest.getFirstName());
                    assertThat(userResponse.getLastName()).isEqualTo(createUserRequest.getLastName());
                    assertThat(userResponse.getEmail()).isEqualTo(createUserRequest.getEmail());
                    assertThat(userResponse.getPassword()).isNotNull();
                    assertThat(userResponse.getPassword()).isNotEqualTo(createUserRequest.getPassword());
                    assertThat(userResponse.getMobile()).isEqualTo(createUserRequest.getMobile());
                    assertThat(userResponse.getRole()).isEqualTo(createUserRequest.getRole());
                    assertThat(userResponse.getStatus()).isEqualTo("REGISTERED");
                });

        webTestClient.post()
                .uri(createURI(path))
                .body(Mono.just(createUserRequest), CreateUserRequest.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.code").isEqualTo(409)
                .jsonPath("$.message").isEqualTo(EMAIL_EXIST);

        createUserRequest.setEmail(getEmailAddress());
        webTestClient.post()
                .uri(createURI(path))
                .body(Mono.just(createUserRequest), CreateUserRequest.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.code").isEqualTo(409)
                .jsonPath("$.message").isEqualTo(MOBILE_EXIST);

    }


    @Test
    void updateUserTest() {
        AtomicReference<UserResponse> userResponseToUpdateAtomicReference = new AtomicReference<>();
        AtomicReference<UserResponse> userResponseAtomicReference = new AtomicReference<>();
        webTestClient.get()
                .uri(createURI("users"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Page<UserResponse>>() {
                })
                .consumeWith(res -> {
                    var body = res.getResponseBody();
                    //since we are seeding at least 4 records, this should work else check if there is record before assigning
                    userResponseToUpdateAtomicReference.set(body.getContent().get(0));
                    userResponseAtomicReference.set(body.getContent().get(1));
                });


        var existingUser = userResponseToUpdateAtomicReference.get();
        var path = "user/" + existingUser.getId();
        var updateUserRequest = UpdateUserRequest.builder()
                .title(existingUser.getTitle())
                .firstName(getFirstName())
                .lastName(getLastName())
                .mobile(existingUser.getMobile())
                .role(RoleEnum.ADMIN.name())
                .build();

        webTestClient.put()
                .uri(createURI(path))
                .body(Mono.just(updateUserRequest), UpdateUserRequest.class)
                .exchange()
                .expectStatus().isNoContent();

        var nextExistingUser = userResponseAtomicReference.get();
        updateUserRequest.setMobile(nextExistingUser.getMobile());
        webTestClient.put()
                .uri(createURI(path))
                .body(Mono.just(updateUserRequest), UpdateUserRequest.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.code").isEqualTo(409)
                .jsonPath("$.message").isEqualTo(MOBILE_EXIST);
    }

    @Test
    void deactivateUserTest() {
        AtomicReference<UserResponse> userResponseAtomicReference = new AtomicReference<>();
        webTestClient.get()
                .uri(createURI("users"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Page<UserResponse>>() {
                })
                .consumeWith(res -> {
                    var user = res.getResponseBody().getContent().stream().filter(userResponse -> userResponse.getDateDeactivated() == null).findFirst().get();
                    //since we are seeding at least 4 records, this should work else check if there is record before assigning
                    userResponseAtomicReference.set(user);
                });


        var existingUser = userResponseAtomicReference.get();
        var path = "user/" + existingUser.getId();

        webTestClient.delete()
                .uri(createURI(path))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.delete()
                .uri(createURI(path))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(400)
                .jsonPath("$.message").isEqualTo("user already deactivated");
    }


    @Test
    void createUserTitleValidationTest() {
        var path = "user";

        var createUserRequest = CreateUserRequest.builder()
                .title("")
                .firstName(getFirstName())
                .lastName(getLastName())
                .email(getEmailAddress())
                .mobile(getMobileNumber())
                .password(getPassword())
                .role(RoleEnum.USER.name())
                .build();


        webTestClient.post()
                .uri(createURI(path))
                .body(Mono.just(createUserRequest), CreateUserRequest.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].field").isEqualTo("title")
                .jsonPath("$[1].field").isEqualTo("title")
                .jsonPath("$", hasSize(2));

    }


    @Test
    void createUserFirstNameValidationTest() {
        var path = "user";

        var createUserRequest = CreateUserRequest.builder()
                .title(getTitle())
                .firstName("")
                .lastName(getLastName())
                .email(getEmailAddress())
                .mobile(getMobileNumber())
                .password(getPassword())
                .role(RoleEnum.USER.name())
                .build();


        webTestClient.post()
                .uri(createURI(path))
                .body(Mono.just(createUserRequest), CreateUserRequest.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].field").isEqualTo("firstName")
                .jsonPath("$[1].field").isEqualTo("firstName")
                .jsonPath("$", hasSize(2));

    }

    @Test
    void createUserLastNameValidationTest() {
        var path = "user";

        var createUserRequest = CreateUserRequest.builder()
                .title(getTitle())
                .firstName(getFirstName())
                .lastName("")
                .email(getEmailAddress())
                .mobile(getMobileNumber())
                .password(getPassword())
                .role(RoleEnum.USER.name())
                .build();


        webTestClient.post()
                .uri(createURI(path))
                .body(Mono.just(createUserRequest), CreateUserRequest.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].field").isEqualTo("lastName")
                .jsonPath("$[1].field").isEqualTo("lastName")
                .jsonPath("$", hasSize(2));

    }

    @Test
    void createUserEmailValidationTest() {
        var path = "user";

        var createUserRequest = CreateUserRequest.builder()
                .title(getTitle())
                .firstName(getFirstName())
                .lastName(getLastName())
                .email("")
                .mobile(getMobileNumber())
                .password(getPassword())
                .role(RoleEnum.USER.name())
                .build();


        webTestClient.post()
                .uri(createURI(path))
                .body(Mono.just(createUserRequest), CreateUserRequest.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$", hasSize(3));


    }

    @Test
    void createUserRoleValidationTest() {
        var path = "user";

        var createUserRequest = CreateUserRequest.builder()
                .title(getTitle())
                .firstName(getFirstName())
                .lastName(getLastName())
                .email(getEmailAddress())
                .mobile(getMobileNumber())
                .password(getPassword())
                .role("")
                .build();


        webTestClient.post()
                .uri(createURI(path))
                .body(Mono.just(createUserRequest), CreateUserRequest.class)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].field").isEqualTo("role")
                .jsonPath("$[1].field").isEqualTo("role")
                .jsonPath("$", hasSize(2));

    }
}