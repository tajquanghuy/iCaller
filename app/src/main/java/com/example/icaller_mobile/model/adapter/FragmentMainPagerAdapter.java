//package com.example.icaller_mobile.model.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//import com.example.icaller_mobile.R;
//import com.example.icaller_mobile.common.components.TabItemFragment;
//import com.example.icaller_mobile.common.utils.Utils;
//import com.example.icaller_mobile.features.block_list.BlockListFragment;
//import com.example.icaller_mobile.features.dialpad.DialpadFragment;
//import com.example.icaller_mobile.features.history.HistoryFragment;
//
//import org.jetbrains.annotations.NotNull;
//
//
///**
// * Created by baonv on 18,July,2019
// * Hiworld JSC.
// */
//
//public class FragmentMainPagerAdapter extends FragmentPagerAdapter {
//
//    private int selectChildTabContact;
//    private int selectChildTabSetting;
//    private TabItemFragment[] listFragment = new TabItemFragment[5];
//
//    public FragmentMainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
//        super(fm, behavior);
//    }
//
//    public void setSelectChildTabContact(int selectChildTabContact) {
//        this.selectChildTabContact = selectChildTabContact;
//    }
//
//    public void setSelectChildTabSetting(int selectChildTabSetting) {
//        this.selectChildTabSetting = selectChildTabSetting;
//    }
//
//    @NotNull
//    @Override
//    public Fragment getItem(int i) {
//        TabItemFragment fragment;
//        switch (i) {
//            case 0:
//                fragment = new TabItemFragment(DialpadFragment.newInstance());
//                break;
//            case 1:
//                fragment = new TabItemFragment(HistoryFragment.newInstance());
//                break;
//            case 2:
//                ContactDeviceFragment contactDeviceFragment = ContactDeviceFragment.newInstance();
//                contactDeviceFragment.setSelectChildIndex(selectChildTabContact);
//                fragment = new TabItemFragment(contactDeviceFragment);
//                break;
//            case 3:
//                BlockListFragment blockListFragment = BlockListFragment.newInstance();
//                blockListFragment.setSelectChildIndex(selectChildTabContact);
//                fragment = new TabItemFragment(blockListFragment);
//                break;
//            case 4:
//                SettingFragment settingFragment = SettingFragment.newInstance();
//                settingFragment.setSelectChildIndex(selectChildTabSetting);
//                fragment = new TabItemFragment(settingFragment);
//                break;
//            default:
//                fragment = null;
//                break;
//        }
//        listFragment[i] = fragment;
//        return fragment;
//    }
//
//    @Override
//    public int getCount() {
//        return 5;
//    }
//
//    public TabItemFragment getTabFragment(int position) {
//        return listFragment[position];
//    }
//
//    public TabItemFragment[] getListFragment() {
//        return listFragment;
//    }
//
//    public View getItemView(Context mContext, int position) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fixed_tabs, null);
//        ImageView imgIcon = view.findViewById(R.id.imgTabIcon);
//        TextView badge = view.findViewById(R.id.badge);
//        switch (position) {
//            case 0:
//                imgIcon.setImageResource(R.drawable.ic_dial);
//                break;
//            case 1:
//                imgIcon.setImageResource(R.drawable.ic_history);
//                break;
//            case 2:
//                imgIcon.setImageResource(R.drawable.ic_phonebook);
//                break;
//            case 3:
//                imgIcon.setImageResource(R.drawable.ic_blocking);
//                break;
//            case 4:
//                imgIcon.setImageResource(R.drawable.ic_setting);
//                badge.setVisibility(!Utils.canDrawOverlays(mContext) ? View.VISIBLE : View.GONE);
//                break;
//            default:
//                break;
//        }
//        return view;
//    }
//}
