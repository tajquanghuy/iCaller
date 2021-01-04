package com.example.icaller_mobile.features.settings;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseActivity;
import com.example.icaller_mobile.base.BaseViewModel;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.FragmentTag;
import com.example.icaller_mobile.features.authenticate.LoginActivity;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepository;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepositoryImp;
import com.example.icaller_mobile.features.settings.settings_about.AboutFragment;
import com.example.icaller_mobile.features.settings.settings_faq.FAQFragment;
import com.example.icaller_mobile.features.settings.settings_logout.DialogOnclickListener;
import com.example.icaller_mobile.features.settings.settings_logout.SelectionDialog;
import com.example.icaller_mobile.features.settings.settings_webview.WebviewFragment;
import com.example.icaller_mobile.model.network.room.DataBeanRepository;
import com.example.icaller_mobile.model.network.room.DatabeanRepositoryImp;

public class SettingsViewModel extends BaseViewModel {
    private BlockContactRepository blockContactRepository;
    private DataBeanRepository dataBeanRepository;
    public MutableLiveData<Boolean> blockSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> dataBeanSuccess = new MutableLiveData<>();
    int LIMIT_DOWNLOAD = 10000;

    public SettingsViewModel(Context context) {
        super(context);
        blockContactRepository = new BlockContactRepositoryImp(context);
        dataBeanRepository = new DatabeanRepositoryImp(context);
    }

    public void onClickAbout() {
        ((BaseActivity<?, ?>) context.get()).push(AboutFragment.newInstance(), FragmentTag.FRAGMENT_ABOUT);
    }

    public void onClickPrivacyPolicy() {
        WebviewFragment webviewFragment = new WebviewFragment();
        webviewFragment.url = Constants.KEY_PRIVACY_POLICY;
        webviewFragment.title = context.get().getString(R.string.key_privacy_policy);
        ((BaseActivity<?, ?>) context.get()).push(webviewFragment, FragmentTag.FRAGMENT_PRIVACY_POLICY);
    }

    public void onClickTermOfService() {
        WebviewFragment webviewFragment = new WebviewFragment();
        webviewFragment.url = Constants.KEY_TOU;
        webviewFragment.title = context.get().getString(R.string.key_tou);
        ((BaseActivity<?, ?>) context.get()).push(webviewFragment, FragmentTag.FRAGMENT_TERM_OF_SERVICE);
    }

    public void onClickFAQ() {
        ((BaseActivity<?, ?>) context.get()).push(FAQFragment.newInstance(), FragmentTag.FRAGMENT_FAQ);
    }


    public void logout() {
        SelectionDialog selectionDialog = new SelectionDialog(context.get(),
                context.get().getString(R.string.logout),
                context.get().getString(R.string.logout_confirm),
                context.get().getString(R.string.cancel),
                context.get().getString(R.string.ok),
                new DialogOnclickListener() {
                    @Override
                    public void onButtonEndClick() {
                        context.get().startActivity(new Intent(context.get(), LoginActivity.class));
                    }
                });
        selectionDialog.show();
//        Intent intent = new Intent();
//        String updateAt = intent.getStringExtra("UPDATED_AT");
//        int id = intent.getIntExtra("ID_LASTEST_CURRENT", 0);
//        getDB(context.get(), "" + id, updateAt, LIMIT_DOWNLOAD);
    }

//    public void getDB(Context context, String id, String updatedAt, int limit) {
//        final String SORT_BY = "id";
//        final String DIRECTION = "asc";
//        final String COUNTRY_CODE = SharedPreferencesManager.getDefault(context).getCountryCodeWithPlus();
//
//        ServiceApi serviceApi = RetrofitClient.getRetrofit(context).create(ServiceApi.class);
//        Gson gson = new Gson();
//        Type type = new TypeToken<ContactReportResponse>() {
//        }.getType();
//        Disposable disposable = serviceApi
//                .getPhoneDB(">" + updatedAt, limit, ">" + id, COUNTRY_CODE, SORT_BY, DIRECTION, PARAM_SELECT_GET_DB)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        ContactReportResponse contactReportResponse = gson.fromJson(Utils.decryptPhoneDB(context.getText(R.string.key_decrypt_phone_db).toString(), s), type);
//                        int size = contactReportResponse.getData().size();
//                        String idDelete = contactReportResponse.getPhone_deleted();
//                        if (size > limit || size == limit) {
//                            List<DataBean> dataBeanList = contactReportResponse.getData();
//                            Disposable disposable1 = dataBeanRepository.insertAll(dataBeanList)
//                                    .subscribe(aLong -> dataBeanSuccess.setValue(true)
//                                            , throwable -> {
//                                                Logger.log(dataBeanList);
//                                            });
//                            String id = String.valueOf(dataBeanList.get(limit - 1).getId());
//                            String strUpdatedAt = dataBeanList.get(limit - 1).getUpdatedAt();
//                            SharedPreferencesManager.getDefault(context).saveDateUpdateData(strUpdatedAt);
//                            SharedPreferencesManager.getDefault(context).saveLastestId(id);
//                            getDB(context, id, updatedAt, limit);
//                        }
//                    }
//                }, throwable -> {
//                    int a = 1;
//                });
//    }

}