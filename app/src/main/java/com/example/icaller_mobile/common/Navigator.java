package com.example.icaller_mobile.common;

import androidx.fragment.app.Fragment;

public interface Navigator {
    void push(Fragment fragment, String tag);
    void push(Fragment fragment, String tag, boolean animated);
    boolean pop();
    boolean pop(boolean animated);
    boolean isNavigationAnimationDisabled();
}
