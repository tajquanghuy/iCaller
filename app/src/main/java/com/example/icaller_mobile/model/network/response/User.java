package com.example.icaller_mobile.model.network.response;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private ProfileBean profile;

    protected User(Parcel in) {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public ProfileBean getProfile() {
        return profile;
    }

    public void setProfile(ProfileBean profile) {
        this.profile = profile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static class ProfileBean {

        private int id;
        private String phone;
        private Object password;
        private Object name;
        private Object avatar;
        private Object group;
        private Object active;
        private String auth_token;
        private String noti_token;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
            this.avatar = avatar;
        }

        public Object getGroup() {
            return group;
        }

        public void setGroup(Object group) {
            this.group = group;
        }

        public Object getActive() {
            return active;
        }

        public void setActive(Object active) {
            this.active = active;
        }

        public String getAuth_token() {
            return auth_token;
        }

        public void setAuth_token(String auth_token) {
            this.auth_token = auth_token;
        }

        public String getNoti_token() {
            return noti_token;
        }

        public void setNoti_token(String noti_token) {
            this.noti_token = noti_token;
        }
    }
}
