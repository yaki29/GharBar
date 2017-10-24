package com.example.user.gharbar.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 24/10/17.
 */

public class ServerResponse {


    // variable name should be same as in the json response from php    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

}
