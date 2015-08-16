package com.campusfeedapp.campusfeed.DTO;

import com.campusfeedapp.campusfeed.Utils.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chinmay on 9/8/15.
 */

public class PostItemDTO {

    @SerializedName(Constants.Keys.POST_ID)
    public String postID;

    @SerializedName(Constants.Keys.POST_TEXT)
    public String text;

    @SerializedName(Constants.Keys.POST_IMAGE_URL)
    public String imgUrl;

    @SerializedName(Constants.Keys.POST_TIMESTAMP)
    public String time;

    @SerializedName(Constants.Keys.POST_PENDING_BIT)
    public String pendingBit;

    @SerializedName(Constants.Keys.POST_AUTHOR_FULL_NAME)
    public String userFullName;

    @SerializedName(Constants.Keys.POST_AUTHOR_IMAGE_URL)
    public String userImgUrl;

    @SerializedName(Constants.Keys.POST_AUTHOR_IMAGE_URL)
    public String userBranch;

    @SerializedName(Constants.Keys.POST_VIEWS)
    public String numViews;

    public String getNumViews() {
        return numViews;
    }

    public void setNumViews(String numViews) {
        this.numViews = numViews;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPendingBit() {
        return pendingBit;
    }

    public void setPendingBit(String pendingBit) {
        this.pendingBit = pendingBit;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getUserBranch() {
        return userBranch;
    }

    public void setUserBranch(String userBranch) {
        this.userBranch = userBranch;
    }
}
