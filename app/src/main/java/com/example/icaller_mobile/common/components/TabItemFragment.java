package com.example.icaller_mobile.common.components;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.icaller_mobile.R;

import org.jetbrains.annotations.NotNull;


public class TabItemFragment extends Fragment {

    private Fragment child;

    public TabItemFragment() {
    }

    public TabItemFragment(Fragment child) {
        this.child = child;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.tab_item_fragment, container, false);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (child != null) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainer, child).commit();
        }
    }

    public Fragment getChild() {
        return child;
    }
}
