package com.dimas.controller;

import com.dimas.api.SecuredApi;
import com.dimas.api.model.ApiDialogMessage;
import com.dimas.api.model.ApiDialogUserIdSendPostRequest;
import com.dimas.api.model.ApiLoginPost200Response;
import com.dimas.api.model.ApiLoginPostRequest;
import com.dimas.api.model.ApiPost;
import com.dimas.api.model.ApiPostCreatePostRequest;
import com.dimas.api.model.ApiPostUpdatePutRequest;
import com.dimas.api.model.ApiUser;
import com.dimas.api.model.ApiUserRegisterPost200Response;
import com.dimas.api.model.ApiUserRegisterPostRequest;
import com.dimas.domain.mapper.PersonMapper;
import com.dimas.log.LogRequest;
import com.dimas.service.PersonService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
@LogRequest
public class OpenApiController implements SecuredApi {

    private final PersonService personService;
    private final PersonMapper personMapper;


    @Override
    public ApiLoginPost200Response loginPost(ApiLoginPostRequest apiLoginPostRequest) {
        var response = personService.login(UUID.fromString(apiLoginPostRequest.getId()), apiLoginPostRequest.getPassword());
        return new ApiLoginPost200Response().token(response);
    }

    @Override
    public ApiUserRegisterPost200Response userRegisterPost(ApiUserRegisterPostRequest apiUserRegisterPostRequest) {
        var request = personMapper.map(apiUserRegisterPostRequest);
        var person = personService.create(request);
        return new ApiUserRegisterPost200Response().userId(person.getId().toString());
    }

    @Override
    public ApiUser userGetIdGet(String id) {
        return personMapper.map(personService.findById(UUID.fromString(id)));
    }

    @Override
    public List<ApiDialogMessage> dialogUserIdListGet(String userId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Response dialogUserIdSendPost(String userId, ApiDialogUserIdSendPostRequest apiDialogUserIdSendPostRequest) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Response friendDeleteUserIdPut(String userId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Response friendSetUserIdPut(String userId) {
        throw new RuntimeException("not implemented");
    }


    @Override
    public String postCreatePost(ApiPostCreatePostRequest apiPostCreatePostRequest) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Response postDeleteIdPut(String id) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<ApiPost> postFeedGet(BigDecimal offset, BigDecimal limit) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public ApiPost postGetIdGet(String id) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Response postUpdatePut(ApiPostUpdatePutRequest apiPostUpdatePutRequest) {
        throw new RuntimeException("not implemented");
    }


    @Override
    public List<ApiUser> userSearchGet(String firstName, String lastName) {
        throw new RuntimeException("not implemented");
    }

}
