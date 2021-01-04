package com.example.icaller_mobile.model.network;


import androidx.annotation.NonNull;

import com.example.icaller_mobile.model.network.response.MessageResponse;
import com.example.icaller_mobile.model.network.response.NumberContactBlockResponse;
import com.example.icaller_mobile.model.network.response.StatusTokenResponse;
import com.example.icaller_mobile.model.network.response.User;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {
    @POST("auth/login")
    @FormUrlEncoded
    Observable<User> loginObservable(@Field("phone") @NonNull String phone,
                                     @Field("g_token") @NonNull String gToken,
                                     @Field("noti_token") String notiToken);


    @POST("/auth/logout")
    @FormUrlEncoded
    Observable<MessageResponse> logoutObservable(@Field("api_token") @NonNull String apiToken,
                                                 @Field("noti_token") @NonNull String notiToken);


    @POST("phone/report-spam")
    @FormUrlEncoded
    Observable<Void> reportSpamObservable(@Field("api_token") @NonNull String apiToken,
                                          @Field("phone") @NonNull String phone,
                                          @Field("name") @NonNull String name,
                                          @Field("report_type") @NonNull int type);


    @POST("phone/update-contact")
    @FormUrlEncoded
    Observable<Void> updateContactObservable(@Field("api_token") @NonNull String apiToken,
                                             @Field("list_contact") @NonNull String listContactString);

    @GET("auth/check")
    Observable<StatusTokenResponse> checkStatusTokenObservable(@Query("api_token") String apiToken);

    @POST("me/profile")
    @FormUrlEncoded
    Observable<Void> updateProfileObservable(@Field("api_token") @NonNull String apiToken,
                                             @Field("phone") String phone,
                                             @Field("name") String name,
                                             @Field("email") String email,
                                             @Field("setting_noti") boolean settingNoti,
                                             @Field("setting_email") boolean settingEmail);

    @POST("me/profile")
    @FormUrlEncoded
    Observable<Void> updateProfileObservable(@Field("api_token") @NonNull String apiToken,
                                             @Field("phone") String phone,
                                             @Field("name") String name,
                                             @Field("email") String email);

    @GET("me/profile")
    Observable<User> getMemberProfile(@Query("api_token") String apiToken);


//    // TODO: paging
//    @GET("phone/get-db")
//    Call<String> getPhoneDB(@Query("update_at") String updatedAt,
//                            @Query("limit") int limit,
//                            @Query("paging") int paging,
//                            @Query("page") int page,
//                            @Query("code") String countryCode,
//                            @Query("select") String select);
    //room
////    @GET("phone/get-db")
////    Observable<String> getPhoneRoomDB(@Query("updated_at") String updatedAt,
////                                  @Query("limit") int limit,
////                                  @Query("code") String countryCode,
////                                  @Query("sort") String sort,
////                                  @Query("direction") String direction,
////                                  @Query("select") String select);
////
////    @GET("phone/get-db")
////    Observable<String> getPhoneDB(@Query("updated_at") String updatedAt,
////                            @Query("limit") int limit,
////                            @Query("paging") int paging,
////                            @Query("page") int page,
////                            @Query("code") String countryCode,
////                            @Query("sort") String sort,
////                            @Query("direction") String direction,
//                            @Query("select") String select);


    @GET("phone/get-db")
    Observable<String> getPhoneDB(@Query("updated_at") String updatedAt,
                            @Query("limit") int limit,
                            @Query("id") String idSelection,
                            @Query("code") String countryCode,
                            @Query("sort") String sort,
                            @Query("direction") String direction,
                            @Query("select") String select);

    @GET("phone/get-db")
    Observable<NumberContactBlockResponse> getNumberPhoneDBObservable(@Query("updated_at") String updatedAt,
                                                                      @Query("count") int count);

    @POST("phone/check-phone-spam")
    @FormUrlEncoded
    Observable<MessageResponse> tickPhoneNotSpamObservable(@Field("api_token") @NonNull String apiToken,
                                                           @Field("phone") @NonNull String phone);


    @POST("phone/check-name-phone")
    @FormUrlEncoded
    Observable<MessageResponse> checkNamePhoneObservable(@Field("api_token") @NonNull String apiToken,
                                                         @Field("phone") @NonNull String phone,
                                                         @Field("check") @NonNull int check,
                                                         @Field("name") String name);

    @POST("me/noti-token")
    @FormUrlEncoded
    Observable<MessageResponse> updateNotiTokenObservable(@Field("api_token") @NonNull String apiToken,
                                                          @Field("noti_token") @NonNull String notiToken);


    @POST("call-history/store")
    @FormUrlEncoded
    Observable<MessageResponse> updateHistoryObservable(@Field("api_token") String apiToken,
                                                        @Field("list_call_history") String listCallHistory);


}
