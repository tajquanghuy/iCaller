package com.example.icaller_mobile.base;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.features.authenticate.LoginViewModel;
import com.example.icaller_mobile.features.block_contact.CreateBlockContactViewModel;
import com.example.icaller_mobile.features.block_detail.DetailContactViewModel;
import com.example.icaller_mobile.features.block_list.BlockListViewModel;
import com.example.icaller_mobile.features.contacts.ContactsViewModel;
import com.example.icaller_mobile.features.current_data.CurrentDataViewModel;
import com.example.icaller_mobile.features.dialpad.DialpadViewModel;
import com.example.icaller_mobile.features.history.HistoryViewModel;
import com.example.icaller_mobile.features.main.MainViewModel;
import com.example.icaller_mobile.features.settings.SettingsViewModel;
import com.example.icaller_mobile.features.settings.settings_about.AboutViewModel;
import com.example.icaller_mobile.features.settings.settings_faq.FAQViewModel;
import com.example.icaller_mobile.features.settings.settings_webview.WebviewViewModel;

public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    private Context context;

    public ViewModelProviderFactory(Context context) {
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            //noinspection unchecked
            return (T) new MainViewModel(context);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            //noinspection unchecked
            return (T) new LoginViewModel(context);
        } else if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            //noinspection unchecked
            return (T) new SettingsViewModel(context);
        } else if (modelClass.isAssignableFrom(AboutViewModel.class)) {
            //noinspection unchecked
            return (T) new AboutViewModel(context);
        } else if (modelClass.isAssignableFrom(WebviewViewModel.class)) {
            //noinspection unchecked
            return (T) new WebviewViewModel(context);
        } else if (modelClass.isAssignableFrom(FAQViewModel.class)) {
            //noinspection unchecked
            return (T) new FAQViewModel(context);
        } else if (modelClass.isAssignableFrom(BlockListViewModel.class)) {
            //noinspection unchecked
            return (T) new BlockListViewModel(context);
        } else if (modelClass.isAssignableFrom(CreateBlockContactViewModel.class)) {
            //noinspection unchecked
            return (T) new CreateBlockContactViewModel(context);
        } else if (modelClass.isAssignableFrom(DetailContactViewModel.class)) {
            //noinspection unchecked
            return (T) new DetailContactViewModel(context);
        } else if (modelClass.isAssignableFrom(CurrentDataViewModel.class)) {
            //noinspection unchecked
            return (T) new CurrentDataViewModel(context);
        } else if (modelClass.isAssignableFrom(DialpadViewModel.class)) {
            //noinspection unchecked
            return (T) new DialpadViewModel(context);
        } else if (modelClass.isAssignableFrom(HistoryViewModel.class)) {
            //noinspection unchecked
            return (T) new HistoryViewModel(context);
        } else if (modelClass.isAssignableFrom(ContactsViewModel.class)) {
            //noinspection unchecked
            return (T) new ContactsViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}