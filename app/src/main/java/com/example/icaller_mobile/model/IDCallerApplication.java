//package com.example.icaller_mobile.model;
//
//import android.app.Application;
//
//import com.google.android.gms.ads.MobileAds;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import info.icaller.api.response.User;
//import info.icaller.common.constants.SharedPreferencesConstants;
//import info.icaller.common.manager.ContactManager;
//import info.icaller.common.manager.SharedPreferencesManager;
//import info.icaller.modules.call_handler.CallInfoOverlay;
//
//
///**
// * Created by baonv on 17,July,2019
// * Hiworld JSC.
// */
//public class IDCallerApplication extends Application {
//
//    private User currentUser;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        ContactManager.init(this);
//        new CallInfoOverlay(this);
//
//        MobileAds.initialize(this, initializationStatus -> {
//        });
//    }
//
//
//    public User getCurrentUser() {
//        String user = SharedPreferencesManager.getDefault(this).getUserInfo();
//        this.currentUser = new Gson().fromJson(user, new TypeToken<User>() {
//        }.getType());
//        return this.currentUser;
//    }
//
//    public void setCurrentUser(User currentUser) {
//        this.currentUser = currentUser;
//        if (this.currentUser != null) {
//            SharedPreferencesManager.getDefault(this).saveUserInfo(new Gson().toJson(this.currentUser));
//        } else {
//            SharedPreferencesManager.getDefault(this).removeKeyPreference(SharedPreferencesConstants.KEY_USER_INFO);
//        }
//    }
//}
