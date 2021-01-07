package com.example.icaller_mobile.common.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.example.icaller_mobile.common.constants.IntentConstants;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.features.after_call.AfterCallActivity;
import com.example.icaller_mobile.features.popup.CallInfoOverlay;
import com.example.icaller_mobile.model.callbacks.CallBacks;

public class CallStateReceiver extends BroadcastReceiver {
    private TelephonyManager mTelephonyManager;
    public static boolean isListening = false;
    private CallBacks callBacks;


    private static final String TAG = "PhoneStatReceiver";

    private static boolean incomingFlag = false;

    private static String incoming_number = null;

    private CallInfoOverlay callInfoOverlay;

    public void setCallBacks(CallBacks callBacks) {
        this.callBacks = callBacks;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.log("onReceive");
        callInfoOverlay = CallInfoOverlay.newInstance(context);
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            incomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Logger.log("CALL_OUTGOING ");
        } else {
            TelephonyManager tm =
                    (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    incomingFlag = true;
                    incoming_number = intent.getStringExtra("incoming_number");
                    Logger.log("CALL_STATE_RINGING - " + incoming_number);
                    Intent itCallInfoOverlay = new Intent(context, CallInfoOverlay.class);
                    itCallInfoOverlay.putExtra(IntentConstants.KEY_PHONE_NUMBER, incoming_number);
                    callInfoOverlay.onExtractCallerInformation(incoming_number);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (incomingFlag) {
                        Logger.log("CALL_STATE_OFFHOOK - incoming_number");
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (incomingFlag) {
                        Logger.log("CALL_STATE_IDLE - " + incoming_number);
                        callInfoOverlay.onDestroyOverPlay(incoming_number);
                    }
                    Intent itAfterCall = new Intent(context, AfterCallActivity.class);
                    context.startActivity(itAfterCall);
                    break;
            }
        }
    }
}

