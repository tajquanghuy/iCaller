package com.example.icaller_mobile.features.settings.settings_faq;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.icaller_mobile.base.BaseActivity;
import com.example.icaller_mobile.base.BaseViewModel;
import com.example.icaller_mobile.common.constants.FragmentTag;
import com.example.icaller_mobile.features.settings.settings_faq.repository.FaqRepository;
import com.example.icaller_mobile.features.settings.settings_faq.repository.FaqRepositoryImp;
import com.example.icaller_mobile.model.local.obj.QAObject;

import java.util.List;

public class FAQViewModel extends BaseViewModel {
    private FaqRepository faqRepository;
    public MutableLiveData<List<QAObject>> qaObjectLiveData = new MutableLiveData<>();

    public FAQViewModel(Context context) {
        super(context);
        faqRepository = new FaqRepositoryImp(context);
        qaObjectLiveData.setValue(faqRepository.getListFAQ());
    }

    public void onClickFAQItem() {
        ((BaseActivity<?, ?>) context.get()).push(FAQFragment.newInstance(), FragmentTag.FRAGMENT_FAQ_ITEM);
    }
}