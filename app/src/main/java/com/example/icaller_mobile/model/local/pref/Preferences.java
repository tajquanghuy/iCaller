package com.example.icaller_mobile.model.local.pref;

public interface Preferences {
    void savePreference(String key, Object data);

    Object getPreference(String key, Object defaultValue);

    void clearPreference();

    void removeKeyPreference(String keyName);

    void setFirstTimeLaunch(boolean isFirstTime);

    boolean isFirstTimeLaunch();

    void setKeyIV(String keyIV);

    String getKeyIV();

    void setUsernameAuthentic(String user);

    String getUsernameAuthentic();

    void setPasswordAuthentic(String pass);

    String getPasswordAuthentic();
}
