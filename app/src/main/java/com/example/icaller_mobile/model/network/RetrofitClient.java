package com.example.icaller_mobile.model.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.text.DateFormat;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //    Server test
    public static final String API_BASE_URL = "https://icaller.grooo.com.vn/v1/";

    //    Server PROD
//    public static final String API_BASE_URL = "https://api.icaller.info/v1/";
    private static Retrofit retrofit = null;

    public static OkHttpClient getOkHttpClient(Context mContext) {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.SSL_3_0, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384,
                        CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new ChuckInterceptor(mContext))
                .connectionSpecs(Collections.singletonList(spec))
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit getRetrofit(Context mContext) {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat(DateFormat.DEFAULT)
                    .create();

            retrofit = new Retrofit.Builder().client(getOkHttpClient(mContext))
                    .baseUrl(API_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }
}
