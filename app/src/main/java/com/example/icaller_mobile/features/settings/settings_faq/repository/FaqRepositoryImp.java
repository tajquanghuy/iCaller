package com.example.icaller_mobile.features.settings.settings_faq.repository;

import android.content.Context;

import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.model.local.obj.QAObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FaqRepositoryImp implements FaqRepository {
    private Context mContext;

    public FaqRepositoryImp(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<QAObject> getListFAQ() {
        String jsonText;
        if (Locale.getDefault().getLanguage().equals("vi")) {
            jsonText = Utils.loadJSONFromAsset(mContext, Utils.isMIUI() ? "faq_vn_miui.json" : "faq_vn.json");
        } else {
            jsonText = Utils.loadJSONFromAsset(mContext, Utils.isMIUI() ? "faq_en_miui.json" : "faq_en.json");
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<QAObject>>() {
        }.getType();
        return gson.fromJson(jsonText, type);
    }
}
