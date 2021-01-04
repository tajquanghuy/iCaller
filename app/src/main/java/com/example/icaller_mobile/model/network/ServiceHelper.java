package com.example.icaller_mobile.model.network;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.manager.SharedPreferencesManager;
import com.example.icaller_mobile.common.service.GetDBService;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.interfaces.GetNumberPhoneDBCallBack;
import com.example.icaller_mobile.interfaces.LoginCallBack;
import com.example.icaller_mobile.interfaces.ReportSpamCallBack;
import com.example.icaller_mobile.model.models.ContactUpdateModel;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.lang.ref.WeakReference;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by baonv on 17,July,2019
 * Hiworld JSC.
 */
public class ServiceHelper {
    public final int CODE_UNAUTHORIZED = 401;
    private static ServiceHelper mServiceHelper;
    private ServiceApi mServiceApi;
    private WeakReference<Context> weakContext;
    private static CompositeDisposable compositeDisposable;


    public ServiceHelper(Context mContext) {
        weakContext = new WeakReference<>(mContext);
        compositeDisposable = new CompositeDisposable();
        mServiceApi = RetrofitClient.getRetrofit(weakContext.get()).create(ServiceApi.class);
    }

    public static synchronized ServiceHelper getDefault(Context mContext) {
        if (mServiceHelper == null) {
            mServiceHelper = new ServiceHelper(mContext);
        }
        return mServiceHelper;
    }

    public void loginServer(@NonNull String phone, @NonNull String gToken, String notiToken, LoginCallBack loginCallBack) {
        Disposable disposable = mServiceApi.loginObservable(phone, gToken, notiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            if (loginCallBack != null) loginCallBack.loginSuccess(user);
                        },
                        throwable -> {
                            try {
                                HttpException httpException = (HttpException) throwable;
                                if (httpException != null && loginCallBack != null) {
                                    if (httpException.code() == CODE_UNAUTHORIZED) {
                                        loginCallBack.unauthorized();
                                    } else {
                                        loginCallBack.loginFail(throwable.getMessage());
                                    }
                                }
                            } catch (Exception e) {
                                if (loginCallBack != null)
                                    loginCallBack.loginFail(throwable.getMessage());
                            }
                        });
        compositeDisposable.add(disposable);
    }

    public void logoutServer(@NonNull String apiToken, @NonNull String notiToken) {
        Disposable disposable = mServiceApi.logoutObservable(apiToken, notiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageResponse -> Logger.log(messageResponse != null ? messageResponse.getMessage() : ""),
                        throwable -> Logger.log(throwable.getMessage()));

    }


    public void reportSpam(@NonNull String apiToken, @NonNull String phone, @NonNull String name, int reportType, ReportSpamCallBack reportSpamCallBack) {
        Disposable disposable = mServiceApi.reportSpamObservable(apiToken, phone, name, reportType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageResponse -> {
                    if (reportSpamCallBack != null) reportSpamCallBack.reportSpamSuccess();
                }, throwable -> {
                    String message = weakContext.get().getString(R.string.error);
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        if (httpException.code() == 422) {
                            message = weakContext.get().getString(R.string.error_user_unprocessable);
                        } else if (httpException.code() == 500) {
                            message = weakContext.get().getString(R.string.error_internal_server);
                        } else if (httpException.code() == 401) {
                            if (reportSpamCallBack != null)
                                reportSpamCallBack.unauthorized();
                            return;
                        } else {
                            message = throwable.getMessage();
                        }
                    }
                    if (reportSpamCallBack != null)
                        reportSpamCallBack.reportSpamFail(message);
                });

        compositeDisposable.add(disposable);
    }

    public void updateContactsServer(Set<ContactUpdateModel> contactUpdateModels) {
        boolean status = SharedPreferencesManager.getDefault(weakContext.get()).isAcceptTerms();
        if (status) {
            Gson gson = new Gson();
            String json = gson.toJson(contactUpdateModels);
            Logger.log(json);
            updateContacts(Constants.API_TOKEN, json);
        }
    }

    public void updateContacts(@NonNull String apiToken, @NonNull String listContactString) {
        Disposable disposable = mServiceApi.updateContactObservable(apiToken, listContactString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                        },
                        throwable -> {
                        });
    }

    public void updateProfile(@NonNull String apiToken, String phone, String name, String email, boolean settingNoti, boolean settingEmail) {
        Disposable disposable = mServiceApi.updateProfileObservable(apiToken, phone, name, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> Logger.log(weakContext.get().getText(R.string.update_profile_success).toString()),
                        throwable -> Logger.log(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void getPhoneDB(Context mContext, String updatedAt, int id) {
        try {
            Intent intent = new Intent(mContext, GetDBService.class);
            Bundle bundle = new Bundle();
            bundle.putString("UPDATED_AT", updatedAt);
            bundle.putInt("ID_LASTEST_CURRENT", id);
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
//
//    public void getNumberPhoneDB(String updatedAt, GetNumberPhoneDBCallBack getNumberPhoneDBCallBack) {
//        Disposable disposable = mServiceApi.getNumberPhoneDBObservable(updatedAt, 1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(numberContactBlockResponse -> {
//                    if (getNumberPhoneDBCallBack != null)
//                        getNumberPhoneDBCallBack.getNumberPhoneDBSuccess(numberContactBlockResponse);
//                }, throwable -> {
//                    try {
//                        HttpException httpException = (HttpException) throwable;
//                        if (httpException != null && getNumberPhoneDBCallBack != null) {
//                            if (httpException.code() == CODE_UNAUTHORIZED) {
//                                getNumberPhoneDBCallBack.unauthorized();
//                            } else {
//                                getNumberPhoneDBCallBack.getNumberPhoneDBFail(throwable.getMessage());
//                            }
//                        }
//                    } catch (Exception e) {
//                        if (getNumberPhoneDBCallBack != null)
//                            getNumberPhoneDBCallBack.getNumberPhoneDBFail(throwable.getMessage());
//                    }
//                });
//
//        compositeDisposable.add(disposable);
//    }


    public void tickPhoneNotSpam(@NonNull String apiToken, @NonNull String phone) {
        Disposable disposable = mServiceApi.tickPhoneNotSpamObservable(apiToken, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageResponse -> Logger.log(messageResponse.getMessage()),
                        throwable -> Logger.log(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }


    public void checkNamePhone(@NonNull String apiToken, @NonNull String phone, int check, String name) {
        Disposable disposable = mServiceApi.checkNamePhoneObservable(apiToken, phone, check, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageResponse -> Logger.log(messageResponse.getMessage()),
                        throwable -> Logger.log(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void updateNotiToken(@NonNull String apiToken, @NonNull String notiToken) {
        Disposable disposable = mServiceApi.updateNotiTokenObservable(apiToken, notiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageResponse -> Logger.log(messageResponse.getMessage()),
                        throwable -> Logger.log(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void updateDataDaily(String updatedAt, GetNumberPhoneDBCallBack getNumberPhoneDBCallBack) {
        Disposable disposable = mServiceApi.getNumberPhoneDBObservable(updatedAt, 1).
                subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(numberContactBlockResponse -> {
                    if (getNumberPhoneDBCallBack != null) {
                        getNumberPhoneDBCallBack.getNumberPhoneDBSuccess(numberContactBlockResponse);
                    }
                }, throwable -> {
                    try {
                        HttpException httpException = (HttpException) throwable;
                        if (httpException != null && getNumberPhoneDBCallBack != null) {
                            if (httpException.code() == CODE_UNAUTHORIZED) {
                                getNumberPhoneDBCallBack.unauthorized();
                            } else {
                                getNumberPhoneDBCallBack.getNumberPhoneDBFail(throwable.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        if (getNumberPhoneDBCallBack != null)
                            getNumberPhoneDBCallBack.getNumberPhoneDBFail(throwable.getMessage());
                    }
                });

    }
//
//    public void updateCallLogsServer(Set<CallLogRequestServer> CallLogs) {
//        boolean status = SharedPreferencesManager.getDefault(weakContext.get()).isAcceptTerms();
//        if (status) {
//            Gson gson = new Gson();
//            String json = gson.toJson(CallLogs);
//            Logger.log(json);
//            updateCallLogs(Constants.API_TOKEN, json);
//        }
//    }
//
//    public void updateCallLogs(String apiToken, String listCallHistory) {
//        Disposable disposable = mServiceApi.updateHistoryObservable(apiToken, listCallHistory)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(aVoid -> {
//                }, throwable -> {
//                });
//
//    }

    public void disposeAll() {
        if (compositeDisposable != null) compositeDisposable.dispose();
        compositeDisposable = new CompositeDisposable();
    }
}


