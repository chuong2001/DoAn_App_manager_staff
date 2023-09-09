package com.example.managerstaff.api;

import com.example.managerstaff.models.responses.ListPostResponse;
import com.example.managerstaff.models.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    ApiService apiService = ApiConfig.getClient("http://192.168.0.104:8000")
            .create(ApiService.class);


    @GET("/api/user/login")
    Call<UserResponse> loginUser(@Query("username") String username ,
                                 @Query("password") String password);


    @GET("/api/user/user_detail/{id}")
    Call<UserResponse> getUserDetail (@Path("id") int id);

    @GET("/api/post/all_post")
    Call<ListPostResponse> getAllPost ();
//
//    //http://videoapi.kakoak.tls.tl/video-service/v1/video/6097/related?msisdn=0969633777&timestamp=123&security=123&page=0&size=10&lastHashId=13asd
//    @GET("/video-service/v1/video/{id}/related")
//    Call<AppMedia> getVideoRelated(@Path("id") int id,
//                                   @Query("msisdn") String msisdn,
//                                   @Query("timestamp") String timestamp,
//                                   @Query("security")  String security,
//                                   @Query("page") int page,
//                                   @Query("size") int size,
//                                   @Query("lastHashId") String lastHashId,
//                                   @Header("Accept-language") String header);
//
//    @GET("/video-service/v1/video/hot")
//    Call<AppMedia> getHomeListVideoHot(@Query("msisdn") String msisdn,
//                                             @Query("timestamp") String timestamp,
//                                             @Query("security") String security,
//                                             @Query("page") int page,
//                                             @Query("size") int size,
//                                             @Query("lastHashId") String lastHashId,
//                                             @Header("Accept-language") String acceptLanguage,
//                                             @Header("mocha-api") String mochaApi,
//                                             @Header("sec-api") String sec_api);
//
//    // tìm channel theo id
//    //http://videoapi.kakoak.tls.tl/video-service/v1/channel/328/info?msisdn=%2B67075615473&timestamp=1611796455960&security=&clientType=Android&revision=15511
//    @GET("/video-service/v1/channel/{id}/info")
//    Call<ChannelDetail> getChannelById(@Path("id") int id,
//                                       @Header("Accept-language") String header,
//                                       @Header("mocha-api") String mochaApi,
//                                       @Query("msisdn") String msisdn,
//                                       @Query("timestamp") String timestamp,
//                                       @Query("security") String security,
//                                       @Query("clientType") String clientType,
//                                       @Query("revision") String revision);
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
