package com.example.icaller_mobile.common.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.SharedPreferencesConstants;

import org.joda.time.DateTime;

import java.util.Set;


public class SharedPreferencesManager {
    private SharedPreferences mPreferences;
    private static SharedPreferencesManager sharedPreferencesManager;

    public SharedPreferencesManager(Context mContext) {
        mPreferences = mContext.getSharedPreferences(Constants.FILE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getDefault(Context mContext) {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = new SharedPreferencesManager(mContext);
        }
        return sharedPreferencesManager;
    }

    public static synchronized SharedPreferencesManager getDefault() {
        return sharedPreferencesManager;
    }

    public void savePreference(String key, Object data) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, String.valueOf(data));
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Set) {
            editor.putStringSet(key, (Set<String>) data);
        }
        editor.apply();
    }

    public Object getPreference(String key, Object defaultValue) {
        if (defaultValue instanceof String) {
            return mPreferences.getString(key, String.valueOf(defaultValue));
        } else if (defaultValue instanceof Integer) {
            return mPreferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            return mPreferences.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Float) {
            return mPreferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return mPreferences.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Set) {
            return mPreferences.getStringSet(key, (Set<String>) defaultValue);
        }
        return null;
    }

    public void clearPreference() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void removeKeyPreference(String keyName) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(keyName);
        editor.apply();
    }

    public String getUserInfo() {
        return (String) getPreference(SharedPreferencesConstants.KEY_USER_INFO, "");
    }

    public void saveUserInfo(String user) {
        savePreference(SharedPreferencesConstants.KEY_USER_INFO, user);
    }


    public String getDateUpdateData() {
        return (String) getPreference(SharedPreferencesConstants.KEY_DATE_UPDATE_DATA, "");
    }

    public void saveDateUpdateData(String date) {
        savePreference(SharedPreferencesConstants.KEY_DATE_UPDATE_DATA, date);
    }

    public String getLastestId() {
        return (String) getPreference(SharedPreferencesConstants.KEY_ID_LASTEST_CURRENT, "");
    }

    public void saveLastestId(String id) {
        savePreference(SharedPreferencesConstants.KEY_ID_LASTEST_CURRENT, id);
    }

    public int getPageDownloaded() {
        return (int) getPreference(SharedPreferencesConstants.KEY_DATA_PAGE_DOWNLOAD, 1);
    }

    public void savePageDownloadeded(int page) {
        savePreference(SharedPreferencesConstants.KEY_DATA_PAGE_DOWNLOAD, page);
    }

    public String getApiToken() {
        return (String) getPreference(SharedPreferencesConstants.KEY_API_TOKEN, "");
    }

    public void saveApiToken(String apiToken) {
        savePreference(SharedPreferencesConstants.KEY_API_TOKEN, apiToken);
    }

    public int getIDLastestCurrent() {
        return (int) getPreference(SharedPreferencesConstants.KEY_ID_LASTEST_CURRENT, 0);
    }

    public void saveIDLastestCurrent(int id) {
        savePreference(SharedPreferencesConstants.KEY_ID_LASTEST_CURRENT, id);
    }

    public String getCurrentCountry() {
        return (String) getPreference(SharedPreferencesConstants.KEY_CURRENT_COUNTRY, Constants.ISO_VIETNAM);
    }

    public void saveCurrentCountry(String country) {
        savePreference(SharedPreferencesConstants.KEY_CURRENT_COUNTRY, country);
    }

    public String getCountryCodeWithPlus() {
        return (String) getPreference(SharedPreferencesConstants.KEY_COUNTRY_CODE_WITH_PLUS, "+" + Constants.COUNTRY_CODE_VN);
    }

    public void saveCountryCodeWithPlus(String countryCode) {
        savePreference(SharedPreferencesConstants.KEY_COUNTRY_CODE_WITH_PLUS, countryCode);
    }

    public long lastUpdateDBDaily() {
        return (long) getPreference(SharedPreferencesConstants.KEY_LAST_UPDATE_DB_DAILY, 0L);
    }

    public void saveLastUpdateDBDaily() {
        savePreference(SharedPreferencesConstants.KEY_LAST_UPDATE_DB_DAILY, DateTime.now().getMillis());
    }

    public int getHourUpdateDaily() {
        return (int) getPreference(SharedPreferencesConstants.KEY_HOUR_UPDATE_DAILY, -1);
    }

    public void saveHourUpdateDaily(int hour) {
        savePreference(SharedPreferencesConstants.KEY_HOUR_UPDATE_DAILY, hour);
    }

    public void saveFirstTimeLaunch(boolean isFirstTime) {
        savePreference(SharedPreferencesConstants.IS_FIRST_TIME_LAUNCH, isFirstTime);
    }

    public boolean isFirstTimeLaunch() {
        return (boolean) getPreference(SharedPreferencesConstants.IS_FIRST_TIME_LAUNCH, true);
    }

    public String getAllNotifications() {
        return (String) getPreference(SharedPreferencesConstants.KEY_SAVED_NOTIFICATIONS, null);
    }

    public void saveAllNotifications(String notificationStrings) {
        savePreference(SharedPreferencesConstants.KEY_SAVED_NOTIFICATIONS, notificationStrings);
    }

    public boolean blockOutContactEnable() {
        return (boolean) getPreference(SharedPreferencesConstants.KEY_BLOCK_SETTING_OUT_CONTACT, false);
    }

    public void saveBlockOutContact(boolean status) {
        savePreference(SharedPreferencesConstants.KEY_BLOCK_SETTING_OUT_CONTACT, status);
    }

    public boolean blockForeignNumberEnable() {
        return (boolean) getPreference(SharedPreferencesConstants.KEY_BLOCK_SETTING_FOREIGN_NUMBER, false);
    }

    public void saveBlockForeignNumber(boolean status) {
        savePreference(SharedPreferencesConstants.KEY_BLOCK_SETTING_FOREIGN_NUMBER, status);
    }

    public boolean blockScamEnable() {
        return (boolean) getPreference(SharedPreferencesConstants.KEY_BLOCK_SETTING_SCAM, true);
    }

    public void saveBlockScam(boolean status) {
        savePreference(SharedPreferencesConstants.KEY_BLOCK_SETTING_SCAM, status);
    }

    public boolean blockAdvertisingEnable() {
        return (boolean) getPreference(SharedPreferencesConstants.KEY_BLOCK_SETTING_ADVERTISING, false);
    }

    public void saveBlockAdvertising(boolean status) {
        savePreference(SharedPreferencesConstants.KEY_BLOCK_SETTING_ADVERTISING, status);
    }

    public void saveFinishedActionState(boolean action) {
        savePreference(SharedPreferencesConstants.KEY_PERMISSION_ASKED, action);
    }

    public boolean isActionFinished() {
        return (boolean) getPreference(SharedPreferencesConstants.KEY_PERMISSION_ASKED, false);
    }

    public void saveAcceptTermsState(boolean status) {
        savePreference(SharedPreferencesConstants.KEY_ACCEPT_TERMS, status);
    }

    public boolean isAcceptTerms() {
        return (boolean) getPreference(SharedPreferencesConstants.KEY_ACCEPT_TERMS, false);
    }

}
