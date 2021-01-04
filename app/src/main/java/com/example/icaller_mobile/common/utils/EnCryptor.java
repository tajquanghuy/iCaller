package com.example.icaller_mobile.common.utils;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.model.local.pref.PreferencesImp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class EnCryptor {

    private byte[] encryption;
    private Context context;

    public EnCryptor(Context context) {
        this.context = context;
    }

    /**
     * Encrypt data
     *
     * @return byte
     * Encrypt text before saving to internal memory (local)
     * @param: context, alias, text
     **/
    public byte[] encryptText(final String alias, final String textToEncrypt)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(Constants.TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

        PreferencesImp.getDefault().setKeyIV(android.util.Base64.encodeToString(cipher.getIV(), Base64.DEFAULT));
        return (encryption = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8)));
    }

    @NonNull
    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, Constants.ANDROID_KEY_STORE);

        keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build());

        return keyGenerator.generateKey();
    }

    public byte[] getEncryption() {
        return encryption;
    }


}
