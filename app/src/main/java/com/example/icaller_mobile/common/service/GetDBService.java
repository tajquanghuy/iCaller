package com.example.icaller_mobile.common.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import com.commonsware.cwac.saferoom.SQLCipherUtils;
import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.manager.SharedPreferencesManager;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.model.encode.Crypto;
import com.example.icaller_mobile.model.encode.KeyManager;
import com.example.icaller_mobile.model.network.RetrofitClient;
import com.example.icaller_mobile.model.network.ServiceApi;
import com.example.icaller_mobile.model.network.ServiceHelper;
import com.example.icaller_mobile.model.network.room.ContactReportResponse;
import com.example.icaller_mobile.model.network.room.DataBean;
import com.example.icaller_mobile.model.network.room.DataBeanRepository;
import com.example.icaller_mobile.model.network.room.DatabeanRepositoryImp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.icaller_mobile.common.constants.Constants.PARAM_SELECT_GET_DB;


public class GetDBService extends Service {
    public NotificationManager notificationManager;
    public static final int ID_NOTIFICATION_DOWNLOAD_DB = 10000;
    int LIMIT_DOWNLOAD = 10000;
    int MAX_LIMIT = 10000;
    int MIN_LIMIT = 1000;
    private static GetDBService mGetDBService;
    private DataBeanRepository dataBeanRepository;
    public MutableLiveData<Boolean> dataBeanSuccess = new MutableLiveData<>();

    public static synchronized GetDBService getDefault(Context mContext) {
        if (mGetDBService == null) {
            mGetDBService = new GetDBService();
        }
        return mGetDBService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataBeanRepository = new DatabeanRepositoryImp(getApplicationContext());
        notificationManager = this.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            String message = getString(R.string.updating_data);
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_logo_white)
                    .setColor(getColor(R.color.color_purple_dark))
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setDefaults(Notification.DEFAULT_ALL).build();
            startForeground(ID_NOTIFICATION_DOWNLOAD_DB, notification);
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
//            String updatedAt = intent.getStringExtra("UPDATED_AT");
//            int id = intent.getIntExtra("ID_LASTEST_CURRENT", 0);
        }
        getDbFromServer();
        return Service.START_STICKY;
    }

    public void getDbFromServer() {
        Intent intent = new Intent();
        Logger.log("getDbFromServer");
        String updateAt = intent.getStringExtra("UPDATED_AT");
        int id = intent.getIntExtra("ID_LASTEST_CURRENT", 0);
        getDB(getApplicationContext(), "" + id, updateAt, LIMIT_DOWNLOAD);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID_ICALLER, Constants.NOTIFICATION_CHANNEL_ID_ICALLER, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
        notificationManager.createNotificationChannel(channel);
        String message = getString(R.string.updating_data);
        Notification notification = new Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID_ICALLER)
                .setSmallIcon(R.drawable.ic_logo_white)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setColor(getColor(R.color.color_purple_dark))
                .build();
        startForeground(ID_NOTIFICATION_DOWNLOAD_DB, notification);
    }

    public void startServiceDB(Context mContext) {
        try {
            Intent intent = new Intent(mContext, GetDBService.class);
            Bundle bundle = new Bundle();
//            bundle.putString("UPDATED_AT", updatedAt);
//            bundle.putInt("ID_LASTEST_CURRENT", id);
            intent.putExtras(bundle);
            if (!Utils.isServiceRunning(GetDBService.class, mContext)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mContext.startForegroundService(intent);
                } else {
                    mContext.startService(intent);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getDB(Context context, String id, String updatedAt, int limit) {
        String key = "12345678909876543212345678909876";
        String iv = "1234567890987654";
        KeyManager km = new KeyManager(getApplicationContext());
        km.setIv(iv.getBytes());
        km.setId(key.getBytes());

        final String SORT_BY = "id";
        final String DIRECTION = "asc";
        final String COUNTRY_CODE = SharedPreferencesManager.getDefault(context).getCountryCodeWithPlus();

        ServiceApi serviceApi = RetrofitClient.getRetrofit(context).create(ServiceApi.class);
        Gson gson = new Gson();
        Type type = new TypeToken<ContactReportResponse>() {
        }.getType();
        Disposable disposable = serviceApi
                .getPhoneDB(">" + updatedAt, limit, ">" + id, COUNTRY_CODE, SORT_BY, DIRECTION, PARAM_SELECT_GET_DB)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ContactReportResponse contactReportResponse = gson.fromJson(Utils.decryptPhoneDB(context.getText(R.string.key_decrypt_phone_db).toString(), s), type);
                        int size = contactReportResponse.getData().size();

                        String idDelete = contactReportResponse.getPhone_deleted();
                        if (size > limit || size == limit) {
                            List<DataBean> dataBeanList = contactReportResponse.getData();
                            //Save to Roomdb
                            Disposable disposable1 = dataBeanRepository.insertAll(dataBeanList)
                                    .subscribe(Logger::log
                                            , throwable -> {
                                                Logger.log(dataBeanList);
                                            });
                            /*try {
                                SQLCipherUtils.encrypt(context.getApplicationContext(),"db_contact_block",context.getText(R.string.key_decrypt_room_db).toString().toCharArray());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            String id = String.valueOf(dataBeanList.get(limit - 1).getId());
                            String strUpdatedAt = dataBeanList.get(limit - 1).getUpdatedAt();
                            SharedPreferencesManager.getDefault(context).saveDateUpdateData(strUpdatedAt);
                            SharedPreferencesManager.getDefault(context).saveLastestId(id);
                            getDB(context, id, updatedAt, limit);
                            stopServices(context);
                        } else {
                            List<DataBean> dataBeanList = contactReportResponse.getData();
                        }
                    }
                }, throwable -> {
                    int a = 1;
                });
    }

    private void stopServices(Context context) {
        context.stopService(new Intent(context, GetDBService.class));
        ServiceHelper.getDefault(context).disposeAll();
        stopForeground(true);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
