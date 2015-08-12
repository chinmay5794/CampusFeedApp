package com.campusfeedapp.campusfeed.DTO;

import com.campusfeedapp.campusfeed.Utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chinmay on 8/8/15.
 */
// id name imgurl nmfollowers
public class ChannelItemDTO {

    @SerializedName(Constants.Keys.CHANNEL_ID)
    private String channelID;

    @SerializedName(Constants.Keys.CHANNEL_NAME)
    private String channelName;

    @SerializedName(Constants.Keys.CHANNEL_IMAGE_URL)
    private String imgUrl;

    @SerializedName(Constants.Keys.CHANNEL_NUM_FOLLOWERS)
    private String num_followers;

    @SerializedName(Constants.Keys.STATUS)
    private String status;

    @SerializedName(Constants.Keys.CHANNEL_DESCRIPTION)
    private String description;

    @SerializedName(Constants.Keys.CHANNEL_CREATED_TIME)
    private String createdTime;

    @SerializedName(Constants.Keys.FIRST_NAME)
    private String firstName;

    @SerializedName(Constants.Keys.LAST_NAME)
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNum_followers() {
        return num_followers;
    }

    public void setNum_followers(String num_followers) {
        this.num_followers = num_followers;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

}
