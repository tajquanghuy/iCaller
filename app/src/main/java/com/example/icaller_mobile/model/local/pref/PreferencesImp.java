package com.example.icaller_mobile.model.local.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.SharedPreferencesConstants;
import com.example.icaller_mobile.common.utils.DeCryptor;
import com.example.icaller_mobile.common.utils.EnCryptor;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PreferencesImp implements Preferences {
    private SharedPreferences mPreferences;
    private static PreferencesImp sharedPreferencesImp;
    private WeakReference<Context> weakContext;

    public PreferencesImp(Context mContext) {
        weakContext = new WeakReference<>(mContext);
        mPreferences = mContext.getSharedPreferences(SharedPreferencesConstants.FILE_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesImp init(Context mContext) {
        if (sharedPreferencesImp == null) {
            sharedPreferencesImp = new PreferencesImp(mContext);
        }
        return sharedPreferencesImp;
    }

    public static synchronized PreferencesImp getDefault() {
        return sharedPreferencesImp;
    }

    @Override
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

    @Override
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

    @Override
    public void clearPreference() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void removeKeyPreference(String keyName) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(keyName);
        editor.apply();
    }

    @Override
    public void setFirstTimeLaunch(boolean isFirstTime) {
        savePreference(SharedPreferencesConstants.IS_FIRST_TIME_LAUNCH, isFirstTime);
    }

    @Override
    public boolean isFirstTimeLaunch() {
        return (boolean) getPreference(SharedPreferencesConstants.IS_FIRST_TIME_LAUNCH, true);
    }

    @Override
    public String getKeyIV() {
        return (String) getPreference(SharedPreferencesConstants.KEY_IV, "");
    }

    @Override
    public void setKeyIV(String keyIV) {
        savePreference(SharedPreferencesConstants.KEY_IV, keyIV);
    }

    @Override
    public String getUsernameAuthentic() {
        return (String) getPreference(SharedPreferencesConstants.KEY_USERNAME_AUTH, "");
    }

    @Override
    public void setUsernameAuthentic(String user) {
        savePreference(SharedPreferencesConstants.KEY_USERNAME_AUTH, user);
    }

    @Override
    public String getPasswordAuthentic() {
        String password = null;
        String keyIv = getKeyIV();
        try {
            DeCryptor decryptor = new DeCryptor();
            String passwordEncrypt = (String) getPreference(SharedPreferencesConstants.KEY_PASSWORD_AUTH, "");
            password = (decryptor.decryptData(Constants.SAMPLE_ALIAS, Base64.decode(passwordEncrypt, Base64.DEFAULT), Base64.decode(keyIv, Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    @Override
    public void setPasswordAuthentic(String pass) {
        EnCryptor encryptor = new EnCryptor(weakContext.get());
        String passwordEncrypt = null;
        try {
            final byte[] encryptedText = encryptor.encryptText(Constants.SAMPLE_ALIAS, pass);
            passwordEncrypt = Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException ignored) {
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        savePreference(SharedPreferencesConstants.KEY_PASSWORD_AUTH, passwordEncrypt);
    }


}
