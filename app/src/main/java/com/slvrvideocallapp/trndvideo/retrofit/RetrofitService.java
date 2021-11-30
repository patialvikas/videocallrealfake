
package com.slvrvideocallapp.trndvideo.retrofit;

import com.google.gson.JsonObject;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.AnylitecsAddRoot;
import com.slvrvideocallapp.trndvideo.models.AnylitecsRemoveRoot;
import com.slvrvideocallapp.trndvideo.models.CommentRoot;
import com.slvrvideocallapp.trndvideo.models.CountryRoot;
import com.slvrvideocallapp.trndvideo.models.EmojiIconRoot;
import com.slvrvideocallapp.trndvideo.models.EmojicategoryRoot;
import com.slvrvideocallapp.trndvideo.models.HitAdsRoot;
import com.slvrvideocallapp.trndvideo.models.OwnAdsRoot;
import com.slvrvideocallapp.trndvideo.models.PaperRoot;
import com.slvrvideocallapp.trndvideo.models.ProductKRoot;
import com.slvrvideocallapp.trndvideo.models.ProfileRoot;
import com.slvrvideocallapp.trndvideo.models.SettingsRoot;
import com.slvrvideocallapp.trndvideo.models.ThumbRoot;
import com.slvrvideocallapp.trndvideo.models.UserRoot;
import com.slvrvideocallapp.trndvideo.models.ValidationRoot;
import com.slvrvideocallapp.trndvideo.models.VideoRoot;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("/country")
    Call<CountryRoot> getCountries();

    @GET("/category")
    Call<EmojicategoryRoot> getCategories();


    @GET("/random")
    Call<ThumbRoot> getThumbs(@Query("country") String cid);

    @GET("/user/profile")
    Call<ProfileRoot> getUser(@Query("user_id") String uid);

    @GET("/random/comment")
    Call<CommentRoot> getComment(@Query("country") String cid);

    @GET("/gift/category")
    Call<EmojiIconRoot> getEmojiByCategory(@Query("category") String cid);

    @GET("/random/video")
    Call<VideoRoot> getVideo(@Query("country") String cid);


    @POST("/user/emaillogin")
    Call<ValidationRoot> validionFromEmail(@Body JsonObject obj);

    @POST("/user/mobilelogin")
    Call<ValidationRoot> validionFromMobile(@Body JsonObject obj);

    @POST("/user")
    Call<UserRoot> signUpUser(@Body JsonObject object);


    @POST("/user/less")
    Call<UserRoot> lessCoin(@Body JsonObject object);

    @POST("/user/add")
    Call<UserRoot> addCoin(@Body JsonObject object);

    @GET("/advertisement/credential")
    Call<AdvertisementRoot> getAdvertisement();

    @GET("/advertisement")
    Call<OwnAdsRoot> getOwnAds();

    @GET("/reward")
    Call<SettingsRoot> getSettings();

    @POST("/impression")
    Call<HitAdsRoot> sendImpression(@Body JsonObject obj);

    @POST("/hit")
    Call<HitAdsRoot> sendhit(@Body JsonObject obj);


    @GET("/admin/api/gets")
    Call<PaperRoot> getPapers();


    @GET("/api/clientpackage")
    Call<ProductKRoot> getProducts(@Query("key") String key);


    @PUT("liveuser")
    Call<AnylitecsRemoveRoot> removeUser(@Query("user_id") String uid);

    @POST("liveuser")
    Call<AnylitecsAddRoot> addUser(@Body JsonObject jsonObject);
}
