package com.example.user.gharbar.Utilities;

import com.example.user.gharbar.Models.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by user on 24/10/17.
 */

public interface APIService {
    @Multipart
    @POST("/picUpload.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);



}
