package com.dimas.api;

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
import com.dimas.controller.filter.LogFilterRequest;
import com.dimas.controller.filter.Secured;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * OTUS Highload Architect
 * <p>No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)</p>
 */
@Path("")
@ApplicationScoped
//@LogRequest
@LogFilterRequest
public interface SecuredApi {


    /**
     * Получение анкеты пользователя
     *
     * @param id Идентификатор пользователя
     */
    @GET
    @Path("/user/get/{id}")
    @Produces({"application/json"})
    @Secured
    ApiUser userGetIdGet(@PathParam("id") String id);

    @POST
    @Path("/login")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    ApiLoginPost200Response loginPost(ApiLoginPostRequest apiLoginPostRequest);

    @POST
    @Path("/user/register")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    ApiUserRegisterPost200Response userRegisterPost(ApiUserRegisterPostRequest apiUserRegisterPostRequest);

    /**
     * Поиск анкет
     *
     * @param firstName Условие поиска по имени
     * @param lastName  Условие поиска по фамилии
     */
    @GET
    @Path("/user/search")
    @Produces({"application/json"})
    @Secured
    List<ApiUser> userSearchGet(
            @QueryParam("first_name") String firstName,
            @QueryParam("last_name") String lastName
    );

    /**
     * @param userId
     */
    @GET
    @Path("/dialog/{user_id}/list")
    @Produces({"application/json"})
    List<ApiDialogMessage> dialogUserIdListGet(@PathParam("user_id") String userId);

    /**
     * @param userId
     * @param apiDialogUserIdSendPostRequest
     */
    @POST
    @Path("/dialog/{user_id}/send")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    jakarta.ws.rs.core.Response dialogUserIdSendPost(
            @PathParam("user_id") String userId,
            ApiDialogUserIdSendPostRequest apiDialogUserIdSendPostRequest
    );

    /**
     * @param userId
     */
    @PUT
    @Path("/friend/delete/{user_id}")
    @Produces({"application/json"})
    jakarta.ws.rs.core.Response friendDeleteUserIdPut(@PathParam("user_id") String userId);

    /**
     * @param userId
     */
    @PUT
    @Path("/friend/set/{user_id}")
    @Produces({"application/json"})
    jakarta.ws.rs.core.Response friendSetUserIdPut(@PathParam("user_id") String userId);

    /**
     * Упрощенный процесс аутентификации путем передачи идентификатор пользователя и получения токена для дальнейшего прохождения авторизации
     *
     * @param apiLoginPostRequest
     */


    /**
     * @param apiPostCreatePostRequest
     */
    @POST
    @Path("/post/create")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Secured
    String postCreatePost(ApiPostCreatePostRequest apiPostCreatePostRequest);

    /**
     * @param id
     */
    @PUT
    @Path("/post/delete/{id}")
    @Produces({"application/json"})
    jakarta.ws.rs.core.Response postDeleteIdPut(@PathParam("id") String id);

    /**
     * @param offset
     * @param limit
     */
    @GET
    @Path("/post/feed")
    @Produces({"application/json"})
    List<ApiPost> postFeedGet(
            @QueryParam("offset") BigDecimal offset,
            @QueryParam("limit") BigDecimal limit
    );

    /**
     * @param id
     */
    @GET
    @Path("/post/get/{id}")
    @Produces({"application/json"})
    ApiPost postGetIdGet(@PathParam("id") String id);

    /**
     * @param apiPostUpdatePutRequest
     */
    @PUT
    @Path("/post/update")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    jakarta.ws.rs.core.Response postUpdatePut(ApiPostUpdatePutRequest apiPostUpdatePutRequest);


    /**
     * Регистрация нового пользователя
     *
     * @param apiUserRegisterPostRequest
     */

}
