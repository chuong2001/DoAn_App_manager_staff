package com.example.managerstaff.api;

import com.example.managerstaff.models.responses.ListPostResponse;
import com.example.managerstaff.models.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    ApiService apiService = ApiConfig.getClient("http://192.168.0.105:8000")
            .create(ApiService.class);


    @GET("/api/user/login")
    Call<UserResponse> loginUser(@Query("username") String username ,
                                 @Query("password") String password);


    @GET("/api/user/user_detail/{id}")
    Call<UserResponse> getUserDetail (@Path("id") int id);

    @GET("/api/post/all_post")
    Call<ListPostResponse> getAllPost ();

    @PUT("/api/user/update_user/{id}")
    Call<UserResponse> updateUser(@Path("id") int id,
                                   @Query("avatar") String avatar,
                                   @Query("full_name") String full_name,
                                   @Query("birthday")  String birthday,
                                   @Query("gender") String gender,
                                   @Query("address") String address,
                                   @Query("email") String email,
                                   @Query("phone") String phone,
                                    @Query("wage") double wage);

    @PUT("/api/user/change_password/{id}")
    Call<UserResponse> changePassword(@Path("id") int id,
                                      @Query("password") String password);

    @GET("/api/time/get_time_user/{id}")
    Call<UserResponse> getTimeKeeping(@Path("id") int id,
                                       @Query("day_start") String day_start,
                                       @Query("day_end") String day_end);
//    // list video của 1 channel
//    //http://videoapi.kakoak.tls.tl/video-service/v1/video/10000/channel?msisdn=%2B67075600203&lastHashId=&page=0&size=20&timestamp=1611103216618&security=&clientType=Android&revision=15511
//    @GET("/video-service/v1/video/{id}/channel")
//    Call<AppMedia> getListVideoByChannelId(@Path("id") int id,
//                                           @Header("Accept-language") String header,
//                                           @Header("mocha-api") String mochaApi,
//                                           @Query("msisdn") String msisdn,
//                                           @Query("lastHashId") String lastHashId,
//                                           @Query("page") int page,
//                                           @Query("size") int size,
//                                           @Query("timestamp") String timestamp,
//                                           @Query("security") String security,
//                                           @Query("clientType") String clientType,
//                                           @Query("revision") String revision);


}
