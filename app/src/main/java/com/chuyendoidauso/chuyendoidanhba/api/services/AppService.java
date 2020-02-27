package com.chuyendoidauso.chuyendoidanhba.api.services;

import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
import com.chuyendoidauso.chuyendoidanhba.models.PostDataResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppService {

    @GET("chuyendoidauso/lich_chuyen_doi.php")
    Observable<InfoNumberChange> getInfo();

    @GET("api/log_share_app.php")
    Observable<ResponseBody> postDeviceId(@Query("package") String packageName, @Query("deviceID") String deviceId,
                                          @Query("version") String version,
                                          @Query("os_version") String os_version,
                                          @Query("phone_name") String phone_name);

    @GET("api/doi_dau_so.php")
    Observable<AdsModel> getAdsID(@Query("code") String code,
                                  @Query("deviceID") String deviceId,
                                  @Query("version") String version,
                                  @Query("os_version") String os_version,
                                  @Query("phone_name") String phone_name);

    @POST("api/apps/register_token.php")
    @FormUrlEncoded
    Observable<ResponseBody> registerToken(
            @Field("package") String packageName,
            @Field("token_id") String token_id,
            @Field("code") String code,
            @Field("deviceID") String deviceId,
            @Field("version") String version,
            @Field("os_version") String os_version,
            @Field("phone_name") String phone_name);

    @POST("chuyendoidauso/update_data.php")
    @FormUrlEncoded
    Observable<PostDataResponse> postCountNumberPhone(
            @Field("number_11_so") int countNumberPhone,
            @Field("code") String code,
            @Field("deviceID") String deviceId);

    @POST("api/apps/update_time_push_notify.php")
    @FormUrlEncoded
    Observable<ResponseBody> updateTimePushNotify(
            @Field("package") String packageName,
            @Field("id_push") String id_push,
            @Field("code") String code,
            @Field("deviceID") String deviceId,
            @Field("version") String version,
            @Field("os_version") String os_version,
            @Field("phone_name") String phone_name);
}
