package com.example.managerstaff.api;

import com.example.managerstaff.models.responses.CommentResponse;
import com.example.managerstaff.models.responses.ListCalendarResponse;
import com.example.managerstaff.models.responses.ListPostResponse;
import com.example.managerstaff.models.responses.ListUserResponse;
import com.example.managerstaff.models.responses.ObjectResponse;
import com.example.managerstaff.models.responses.PostResponse;
import com.example.managerstaff.models.responses.SettingResponse;
import com.example.managerstaff.models.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    ApiService apiService = ApiConfig.getClient("http:/192.168.11.100:8000")
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


    @POST("/api/feedback/add_feedback/{id}")
    Call<ObjectResponse> sendFeedBack(@Path("id") int id,
                                      @Query("time_feedback") String timeFeedback,
                                      @Query("content") String content);

    @POST("/api/comment/add_comment/{idu}/{idp}")
    Call<CommentResponse> addComment(@Path("idu") int idUser,
                                     @Path("idp") int idPart,
                                     @Query("time_cmt") String timeComment,
                                     @Query("content") String content);

    @GET("/api/calendar/list_calender_by_part/{id}")
    Call<ListCalendarResponse> getCalendar(@Path("id") int idPart,
                                           @Query("day_calendar") String dayCalendar);

    @GET("/api/user/all_user")
    Call<ListUserResponse> getListUser();

    @GET("/api/post/post_detail/{id}")
    Call<PostResponse> getPostDetail(@Path("id") int id);

    @GET("/api/setting/get_setting")
    Call<SettingResponse> getSetting();

}