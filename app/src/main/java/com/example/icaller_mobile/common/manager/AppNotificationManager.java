package com.example.icaller_mobile.common.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.text.HtmlCompat;

import com.example.icaller_mobile.common.utils.ContactManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import info.icaller.R;
import info.icaller.common.constants.Constants;
import info.icaller.common.constants.IntentConstants;
import info.icaller.common.constants.SharedPreferencesConstants;
import info.icaller.common.utils.Utils;
import info.icaller.models.CallNotification;
import info.icaller.models.ContactResponse;
import info.icaller.models.base.IContactObject;
import info.icaller.models.base.IContactReportObject;
import info.icaller.models.call_log.CallLogDetail;
import info.icaller.receiver.CallStateReceiver;
import info.icaller.receiver.NotificationActionReceiver;
import info.icaller.view.activity.IntroActivity;
import info.icaller.view.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.facebook.FacebookSdk.getApplicationContext;
import static info.icaller.common.constants.NotificationAction.ACTION_ASK_WHO;
import static info.icaller.common.constants.NotificationAction.ACTION_BLOCK_AND_REPORT;
import static info.icaller.common.constants.NotificationAction.ACTION_CALL;
import static info.icaller.common.constants.NotificationAction.ACTION_CORRECT;
import static info.icaller.common.constants.NotificationAction.ACTION_INCORRECT;
import static info.icaller.common.constants.NotificationAction.ACTION_MESSAGE;
import static info.icaller.common.constants.NotificationAction.ACTION_NOTSPAM;
import static info.icaller.common.constants.NotificationAction.ACTION_SPAM;
import static info.icaller.common.constants.NotificationAction.KEY_REPLY;
import static info.icaller.common.constants.NotificationAction.NOTIFICATION_ID_ENDCALL;

public class AppNotificationManager {
    private static info.icaller.common.manager.AppNotificationManager appNotificationManager;
    private WeakReference<Context> context;
    private final int initialID = 3612;
    private final int summaryID = 3611;
    private final int SUMMARY_BLOCK_CALL_ID = 3613;
    private final int NOTIFICATION_UPDATE_DATA_ID = 3614;
    private List<CallNotification> callNotifications;
    private NotificationManagerCompat notificationManagerCompat;

    public static AppNotificationManager getDefault(Context context) {
        if (appNotificationManager == null) {
            appNotificationManager = new info.icaller.common.manager.AppNotificationManager(context);
        }
        return appNotificationManager;
    }

    public AppNotificationManager(Context context) {
        this.context = new WeakReference<>(context);
        this.callNotifications = new ArrayList<>();
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
        // Create notification channel which enable app to show notifications on Android 8+ devices
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = Constants.NOTIFICATION_CHANNEL_ID_ICALLER;
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID_ICALLER, name, importance);
            channel.enableVibration(false);
            channel.setSound(null, null);
            channel.setLightColor(Color.GREEN);
            channel.setDescription(Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
            // Register the channel with the system
            importance = android.app.NotificationManager.IMPORTANCE_HIGH;
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.get().getSystemService(NOTIFICATION_SERVICE);
            channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID_ICALLER_CALL, name, importance);
            channel.enableVibration(false);
            channel.setLightColor(Color.GREEN);
            channel.setDescription(Constants.NOTIFICATION_CHANNEL_ID_ICALLER_CALL);
            // Register the channel with the system
            if (notificationManager != null) notificationManager.createNotificationChannel(channel);

        }
    }

    private synchronized void saveCallNotifications() {
        if (callNotifications.size() == 0) {
            clearCallNotifications();
            return;
        }
        SharedPreferencesManager.getDefault(context.get()).saveAllNotifications(new Gson().toJson(callNotifications));
    }

    public synchronized void updateAndShowCallNotifications(int state, String phoneNumber, final int type) {

        new Thread() {
            @Override
            public void run() {
                super.run();

                //TODO: detect miss call when user actively reject calls
                //TODO: don't cancel ongoing notification

                if (Utils.isEmpty(phoneNumber)) {
                    return;
                }

                int count = callNotifications.size();
                if (state != -1 && count > 0) {
                    for (int i = count - 1; i >= 0; i--) {
                        CallNotification callNotification = callNotifications.get(i);
                        if (callNotification.number.equalsIgnoreCase(phoneNumber) && callNotification.state != TelephonyManager.CALL_STATE_IDLE) {
                            if (callNotification.state == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE) {
                                callNotification.type = CallLog.Calls.MISSED_TYPE;
                            }
                            callNotification.state = state;
                            callNotification.when = new Date().getTime();
                            callNotifications.remove(callNotification);
                            addCallNotification(callNotification);
                            showCallNotifications();
                            break;
                        }
                    }
                }

                if (type == -1) {
                    return;
                }

                if (!Utils.isEmpty(ContactManager.getDefault().getDeviceContactName(phoneNumber))) {
                    return;
                }

                ContactResponse info = ContactManager.getDefault().getContactGroupForPhone(phoneNumber);
                if (info == null) {
                    return;
                }

                CallNotification callNotification = new CallNotification();
                callNotification.id = getNextCallNotificationID();
                callNotification.number = phoneNumber;
                callNotification.group = info.getGroup();
                callNotification.type = type;
                callNotification.state = state;
                callNotification.when = new Date().getTime();
                callNotification.setName(ContactManager.getDefault().nameForICallerContact(info));

                addCallNotification(callNotification);
                showCallNotifications();
            }
        }.start();
    }

    private synchronized void addCallNotification(CallNotification newNotification) {
        if (newNotification == null) {
            return;
        }

        callNotifications.add(newNotification);
        saveCallNotifications();
    }

    private List<CallNotification> fetchCallNotifications() {
        String notificationStrings = SharedPreferencesManager.getDefault(context.get()).getAllNotifications();
        List<CallNotification> callNotifications = new ArrayList<>();
        if (!Utils.isEmpty(notificationStrings)) {
            try {
                List<CallNotification> temp = new Gson().fromJson(notificationStrings, new TypeToken<List<CallNotification>>() {
                }.getType());
                if (temp != null && temp.size() > 0) {
                    callNotifications.addAll(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return callNotifications;
    }

    public synchronized void clearCallNotifications() {
        if (callNotifications.size() > 0) {
            for (CallNotification contact : callNotifications) {
                notificationManagerCompat.cancel(contact.id);
            }
        }
        notificationManagerCompat.cancel(summaryID);
        callNotifications.clear();
        SharedPreferencesManager.getDefault(context.get()).removeKeyPreference(SharedPreferencesConstants.KEY_SAVED_NOTIFICATIONS);
    }

    public synchronized void removeNotificationID(int id) {
        // Clear all calls on Android 6-
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && id == summaryID) {
            if (callNotifications.size() > 0) {
                for (CallNotification contact : callNotifications) {
                    notificationManagerCompat.cancel(contact.id);
                }
            }
            callNotifications.clear();
        } else if (callNotifications.size() > 0 && id != summaryID) {
            for (CallNotification contact : callNotifications) {
                if (contact.id == id) {
                    callNotifications.remove(contact);
                    break;
                }
            }
        }
        if (callNotifications.size() > 0) {
            saveCallNotifications();
        } else {
            clearCallNotifications();
        }
    }

    public synchronized void cancelNotificationID(int id) {
        notificationManagerCompat.cancel(id);
    }

    private boolean shouldShowOverlaySettingsNotification() {
        return !Utils.canDrawOverlays(context.get());
    }

    public void showOverlaySettingsNotification() {
        if (!shouldShowOverlaySettingsNotification()) {
            return;
        }

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.get().getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(context.get().getText(R.string.key_draw_settings).toString());
//        builder.setContentText(context.getText(Utils.isMIUI() ? R.string.key_ask_draw_miui : R.string.key_ask_draw).toString());
        builder.setContentText(Html.fromHtml(context.get().getString(Utils.isMIUI() ? R.string.key_ask_draw_miui : R.string.key_ask_draw)));
//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(context.getText(Utils.isMIUI() ? R.string.key_ask_draw_miui : R.string.key_ask_draw).toString()));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(context.get().getString(Utils.isMIUI() ? R.string.key_ask_draw_miui : R.string.key_ask_draw))));
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_logo_white);
        builder.setColor(context.get().getColor(R.color.color_purple_dark));
        builder.setGroup(Constants.NOTIFICATION_GROUP);

        notificationManagerCompat.notify(Constants.NOTIFICATION_ID_OVERLAY_SETTINGS, builder.build());
    }

    private synchronized int getNextCallNotificationID() {
        int count = callNotifications.size();
        return count > 0 ? callNotifications.get(count - 1).id + 1 : initialID;
    }

    private void removeCompletedCalls() {
        int count = callNotifications.size();
        // Remove finished calls which are not missed calls
        for (int i = count - 1; i >= 0; i--) {
            CallNotification callNotification = callNotifications.get(i);
            if (callNotification.state == TelephonyManager.CALL_STATE_IDLE && callNotification.type != CallLog.Calls.MISSED_TYPE) {
                callNotifications.remove(callNotification);
                notificationManagerCompat.cancel(callNotification.id);
            }
        }
        saveCallNotifications();
    }

    private synchronized void showCallNotifications() {

//        removeCompletedCalls();

        int count = callNotifications.size();
        if (count == 0) {
            return;
        }

        // Cancel all previous notifications so grouping will work
        cancelCallNotifications();
        // Show summary notification
        showSummaryCallNotification();
        for (int i = 0; i < count; i++) {
            showCallNotification(callNotifications.get(i));
        }
        saveCallNotifications();
    }

    private synchronized void cancelCallNotifications() {
        if (callNotifications.size() == 0) {
            return;
        }

        for (CallNotification callNotification : callNotifications) {
            notificationManagerCompat.cancel(callNotification.id);
        }
    }

    private synchronized void showSummaryCallNotification() {
        String summaryText = callNotifications.size() + " " + context.get().getText(R.string.key_icaller_calls).toString();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setClass(context.get(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_SELECTED_TAB_INDEX, 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentTitle(context.get().getText(R.string.app_name).toString());
        builder.setContentText(summaryText);
        builder.setAutoCancel(true);
        builder.setGroup(Constants.NOTIFICATION_GROUP_CALL);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_logo_white);
        builder.setColor(context.get().getColor(R.color.color_purple_dark));
        builder.setGroupSummary(true);
        builder.setSound(null);
        builder.setVibrate(null);
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setSummaryText(context.get().getText(R.string.app_name).toString())
                .setBigContentTitle(summaryText);
        int count = callNotifications.size();
        for (int i = count - 1; i >= 0; i--) {
            CallNotification callNotification = callNotifications.get(i);
            style.addLine(callNotification.fullTitleByType());
        }
        builder.setStyle(style);

        Intent deleteIntent = new Intent(context.get(), CallStateReceiver.class);
        // Very important so that delete intent will deliver correct notification ID
        deleteIntent.setAction("" + summaryID);
        deleteIntent.putExtra(Constants.KEY_NOTIFICATION_ID, summaryID);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context.get(), 0, deleteIntent, 0);
        builder.setDeleteIntent(deletePendingIntent);

        notificationManagerCompat.notify(summaryID, builder.build());
    }

    private synchronized void showCallNotification(CallNotification callNotification) {
        boolean highPriority = callNotification.state == TelephonyManager.CALL_STATE_OFFHOOK;
        String channel = highPriority && !Utils.canDrawOverlays(context.get()) ? Constants.NOTIFICATION_CHANNEL_ID_ICALLER_CALL : Constants.NOTIFICATION_CHANNEL_ID_ICALLER;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.get(), channel);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentTitle(callNotification.titleByType());
        builder.setContentText(callNotification.formattedNumber());
        builder.setSubText(callNotification.group);
        builder.setSmallIcon(R.drawable.ic_logo_white);
        builder.setColor(context.get().getColor(R.color.color_purple_dark));
        builder.setWhen(callNotification.when);
        builder.setGroupSummary(false);
        if (highPriority) {
            Intent intent = new Intent(Constants.NOTIFICATION_ACTION_NONE, null);
            PendingIntent pendingIntent = PendingIntent.getActivity(context.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(false);
            builder.setOngoing(true);
            builder.setPriority(PRIORITY_HIGH);
            builder.setFullScreenIntent(pendingIntent, true);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.KEY_SELECTED_TAB_INDEX, 1);
            intent.setClass(context.get(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            builder.setGroup(Constants.NOTIFICATION_GROUP_CALL);
            builder.setSound(null);
            builder.setVibrate(null);
        }
        if (callNotification.shown) {
            builder.setSound(null);
            builder.setVibrate(null);
        }

        switch (callNotification.type) {
            case CallLog.Calls.INCOMING_TYPE:
                builder.setLargeIcon(BitmapFactory.decodeResource(context.get().getResources(), R.drawable.ic_in_call));
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                builder.setLargeIcon(BitmapFactory.decodeResource(context.get().getResources(), R.drawable.ic_out_call));
                break;
            case CallLog.Calls.MISSED_TYPE:
                builder.setLargeIcon(BitmapFactory.decodeResource(context.get().getResources(), R.drawable.ic_miss_call));
                break;
        }

        Intent deleteIntent = new Intent(context.get(), CallStateReceiver.class);
        // Very important so that delete intent will deliver correct notification ID
        deleteIntent.setAction("" + callNotification.id);
        deleteIntent.putExtra(Constants.KEY_NOTIFICATION_ID, callNotification.id);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context.get(), 0, deleteIntent, 0);
        builder.setDeleteIntent(deletePendingIntent);

        callNotification.shown = true;

        notificationManagerCompat.notify(callNotification.id, builder.build());
    }

    public void showNotificationBlock(String phoneNumber) {
        Intent intent = new Intent(context.get(), MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.putExtra(Constants.KEY_SELECTED_TAB_INDEX, 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_SELECTED_TAB_INDEX, 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        String reason = getApplicationContext().getString(R.string.reason_advertising);
        IContactObject contactBlock = ContactManager.getDefault().getBlockedContactWithNumber(phoneNumber);
        if (contactBlock != null) {
            if (contactBlock.getWarnType() == Constants.ReportType.REPORT_SCAM) {
                reason = context.get().getString(R.string.reason_scam);
            } else if (contactBlock.getWarnType() != Constants.ReportType.REPORT_SCAM && contactBlock.getWarnType() != Constants.ReportType.NORMAL) {
                reason = context.get().getString(R.string.reason_advertising);
            }
        } else {
            boolean blockOutContact = SharedPreferencesManager.getDefault(context.get()).blockOutContactEnable();
            boolean blockForeignNumber = SharedPreferencesManager.getDefault(context.get()).blockForeignNumberEnable();
            boolean blockScamNumber = SharedPreferencesManager.getDefault(context.get()).blockScamEnable();
            boolean blockAdvertisingNumber = SharedPreferencesManager.getDefault(context.get()).blockAdvertisingEnable();
            IContactReportObject contactReport = ContactManager.getDefault().getContactReportByPhone(phoneNumber);

            if (blockScamNumber && contactReport != null && contactReport.getWarn_type() == Constants.ReportType.REPORT_SCAM) {
                reason = context.get().getString(R.string.reason_scam);
            } else if (blockAdvertisingNumber && contactReport != null && contactReport.getWarn_type() != Constants.ReportType.REPORT_SCAM && contactReport.getWarn_type() != Constants.ReportType.NORMAL) {
                reason = context.get().getString(R.string.reason_advertising);
            } else if (blockOutContact) {
                reason = context.get().getString(R.string.reason_not_in_contact);
            } else if (blockForeignNumber) {
                reason = context.get().getString(R.string.reason_foreign);
            } else {
                reason = getApplicationContext().getString(R.string.reason_advertising);
            }

        }
        Notification notification;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            notification = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER)
                    .setContentTitle(Html.fromHtml(context.get().getString(R.string.call_blocked, phoneNumber), HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setContentText(Html.fromHtml(context.get().getString(R.string.reason_blocked, reason), HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_logo_white)
                    .setColor(context.get().getColor(R.color.color_purple_dark))
                    .setGroup(Constants.NOTIFICATION_GROUP)
                    .setAutoCancel(true)
                    .setSound(null)
                    .setVibrate(null)
                    .setPriority(PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .build();

            Notification summaryNotification =
                    new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER)
                            .setSmallIcon(R.drawable.ic_logo_white)
                            .setColor(context.get().getColor(R.color.color_purple_dark))
                            .setGroup(Constants.NOTIFICATION_GROUP)
                            .setGroupSummary(true)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();
            notificationManagerCompat.notify(SUMMARY_BLOCK_CALL_ID, summaryNotification);
        } else {
            notification = new NotificationCompat.Builder(context.get())
                    .setSmallIcon(R.drawable.ic_logo_white)
                    .setColor(context.get().getColor(R.color.color_purple_dark))
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(Html.fromHtml(context.get().getString(R.string.call_blocked, phoneNumber)))
                    .setContentText(Html.fromHtml(context.get().getString(R.string.reason_blocked, reason)))
                    .setContentIntent(pendingIntent)
                    .setPriority(PRIORITY_HIGH)
                    .setAutoCancel(true).build();

        }
        notificationManagerCompat.notify(new Random().nextInt(10000), notification);
    }

    public void showNotificationUpdateData(String message, String target) {
        if (message == null || Utils.isEmpty(message))
            message = context.get().getString(R.string.app_name);
        int monitorScreenTarget;
        try {
            if (target == null || Utils.isEmpty(target)) monitorScreenTarget = 4;
            else monitorScreenTarget = Integer.parseInt(target);
        } catch (Exception ex) {
            ex.printStackTrace();
            monitorScreenTarget = 4;
        }
        Intent intent = new Intent(context.get(), IntroActivity.class);
        intent.setAction(Constants.ACTION_UPDATE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.KEY_SELECTED_TAB_INDEX, monitorScreenTarget);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.get(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Notification notification;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            notification = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER)
                    .setSmallIcon(R.drawable.ic_logo_white)
                    .setColor(context.get().getColor(R.color.color_purple_dark))
                    .setContentTitle(context.get().getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL).build();
        } else {
            notification = new NotificationCompat.Builder(context.get())
                    .setSmallIcon(R.drawable.ic_logo_white)
                    .setColor(context.get().getColor(R.color.color_purple_dark))
                    .setContentTitle(context.get().getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL).build();
        }
        notificationManagerCompat.notify(NOTIFICATION_UPDATE_DATA_ID, notification);
    }


    public void showNotificationScheduleUpdate(String message) {
        Notification notification;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            notification = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER)
                    .setSmallIcon(R.drawable.ic_logo_white)
                    .setColor(context.get().getColor(R.color.color_purple_dark))
                    .setContentTitle(context.get().getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setPriority(PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL).build();
        } else {
            notification = new NotificationCompat.Builder(context.get())
                    .setSmallIcon(R.drawable.ic_logo_white)
                    .setColor(context.get().getColor(R.color.color_purple_dark))
                    .setContentTitle(context.get().getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setPriority(PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL).build();
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManagerCompat.notify(3615, notification);
    }


    /**
     * Display_after_call_3
     * DisplayAfterCall_AskSpam
     *
     * @param callLogDetail
     */
    public void showNotificationAskSpam(CallLogDetail callLogDetail) {
        if (callLogDetail == null || callLogDetail.getContact() == null) return;
        String name = callLogDetail.getContact().getName();
        if (Utils.isEmpty(name)) name = Utils.numberPhoneFormat(callLogDetail.getContact().getPhoneNumber());
        switch (callLogDetail.getTypeCallLog()) {
            case 1:
                name = context.get().getString(R.string.key_incoming_call) + ": " + name;
                break;
            case 2:
                name = context.get().getString(R.string.key_outgoing_call) + ": " + name;
                break;
            case 3:
                name = context.get().getString(R.string.key_missed_call) + ": " + name;
                break;
        }

        String mobileNetwork;
        if (Utils.isEmpty(callLogDetail.getContact().getMobileNetwork()) || callLogDetail.getContact().getMobileNetwork().equalsIgnoreCase(context.get().getString(R.string.unknown))) {
            mobileNetwork = callLogDetail.getContact().getNational();
        } else {
            mobileNetwork = context.get().getText(R.string.mobile).toString() + " - " + callLogDetail.getContact().getMobileNetwork() + "\n" + callLogDetail.getContact().getNational();
        }

        String type = null;
        if (!callLogDetail.getContact().isInContactBlock() && callLogDetail.getContact().getType() != Constants.PhoneNumberType.NONE) {
            if (callLogDetail.getContact().isInContactBlock())
                type = context.get().getString(R.string.key_block_number) + ": " + context.get().getString(callLogDetail.getContact().getType().getResId());
            else if (callLogDetail.getContact().isInContactSpam()) type = context.get().getString(R.string.spam) + ": " + context.get().getString(callLogDetail.getContact().getType().getResId());
        }


        String contentText = context.get().getString(R.string.do_you_think_this_is_spam);
        CharSequence sequence;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sequence = Html.fromHtml(context.get().getString(R.string.text_html_bold, contentText), Html.FROM_HTML_MODE_COMPACT);
        } else {
            sequence = Html.fromHtml(context.get().getString(R.string.text_html_bold, contentText));
        }

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        CharSequence sequenceBigStyle;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sequenceBigStyle = Html.fromHtml(context.get().getString(R.string.text_html_bold_2, Utils.isEmpty(type) ? mobileNetwork : type, contentText), Html.FROM_HTML_MODE_COMPACT);
        } else {
            sequenceBigStyle = Html.fromHtml(context.get().getString(R.string.text_html_bold_2, Utils.isEmpty(type) ? mobileNetwork : type, contentText));
        }
        bigTextStyle.bigText(sequenceBigStyle);


        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            builder = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
        } else {
            builder = new NotificationCompat.Builder(context.get());
        }

        Intent intentNotSpam = new Intent(context.get(), NotificationActionReceiver.class);
        intentNotSpam.setAction(ACTION_NOTSPAM);
        intentNotSpam.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentNotSpam = PendingIntent.getBroadcast(context.get(), 0, intentNotSpam, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent intentSpam = new Intent(context.get(), NotificationActionReceiver.class);
        intentSpam.setAction(ACTION_SPAM);
        intentSpam.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentSpam = PendingIntent.getBroadcast(context.get(), 1, intentSpam, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.drawable.ic_logo_white)
                .setColor(context.get().getColor(R.color.color_purple_dark))
                .setContentTitle(name)
                .setContentText(sequence)
                .setAutoCancel(true)
                .setStyle(bigTextStyle)
                .setPriority(PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);
        builder.addAction(R.drawable.ic_clear_purple, context.get().getString(R.string.not_spam).toUpperCase(), pendingIntentNotSpam);
        builder.addAction(R.drawable.ic_check_purple, context.get().getString(R.string.spam).toUpperCase(), pendingIntentSpam);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManagerCompat.notify(NOTIFICATION_ID_ENDCALL, notification);
    }


    /**
     * Display_after_call_2
     * DisplayAfterCall_Recheck
     *
     * @param callLogDetail
     */
    public void showNotificationRecheck(CallLogDetail callLogDetail) {
        if (callLogDetail == null || callLogDetail.getContact() == null) return;
        String name = callLogDetail.getContact().getName();
        if (Utils.isEmpty(name)) name = Utils.numberPhoneFormat(callLogDetail.getContact().getPhoneNumber());
        switch (callLogDetail.getTypeCallLog()) {
            case 1:
                name = context.get().getString(R.string.key_incoming_call) + ": " + name;
                break;
            case 2:
                name = context.get().getString(R.string.key_outgoing_call) + ": " + name;
                break;
            case 3:
                name = context.get().getString(R.string.key_missed_call) + ": " + name;
                break;
        }

        String mobileNetwork;
        if (Utils.isEmpty(callLogDetail.getContact().getMobileNetwork()) || callLogDetail.getContact().getMobileNetwork().equalsIgnoreCase(context.get().getString(R.string.unknown))) {
            mobileNetwork = callLogDetail.getContact().getNational();
        } else {
            mobileNetwork = context.get().getText(R.string.mobile).toString() + " - " + callLogDetail.getContact().getMobileNetwork() + "\n" + callLogDetail.getContact().getNational();
        }

        String type = null;
        if (!callLogDetail.getContact().isInContactBlock() && callLogDetail.getContact().getType() != Constants.PhoneNumberType.NONE) {
            if (callLogDetail.getContact().isInContactBlock())
                type = context.get().getString(R.string.key_block_number) + ": " + context.get().getString(callLogDetail.getContact().getType().getResId());
            else if (callLogDetail.getContact().isInContactSpam()) type = context.get().getString(R.string.spam) + ": " + context.get().getString(callLogDetail.getContact().getType().getResId());
        }


        String contentText = context.get().getString(R.string.is_this_name_correct);
        CharSequence sequence;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sequence = Html.fromHtml(context.get().getString(R.string.text_html_bold, contentText), Html.FROM_HTML_MODE_COMPACT);
        } else {
            sequence = Html.fromHtml(context.get().getString(R.string.text_html_bold, contentText));
        }

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        CharSequence sequenceBigStyle;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sequenceBigStyle = Html.fromHtml(context.get().getString(R.string.text_html_bold_2, Utils.isEmpty(type) ? mobileNetwork : type, contentText), Html.FROM_HTML_MODE_COMPACT);
        } else {
            sequenceBigStyle = Html.fromHtml(context.get().getString(R.string.text_html_bold_2, Utils.isEmpty(type) ? mobileNetwork : type, contentText));
        }
        bigTextStyle.bigText(sequenceBigStyle);


        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            builder = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
        } else {
            builder = new NotificationCompat.Builder(context.get());
        }

        Intent intentCorrect = new Intent(context.get(), NotificationActionReceiver.class);
        intentCorrect.setAction(ACTION_CORRECT);
        intentCorrect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentCorrect = PendingIntent.getBroadcast(context.get(), 0, intentCorrect, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent intentInCorrect = new Intent(context.get(), NotificationActionReceiver.class);
        intentInCorrect.setAction(ACTION_INCORRECT);
        intentInCorrect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentInCorrect = PendingIntent.getBroadcast(context.get(), 1, intentInCorrect, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.drawable.ic_logo_white)
                .setColor(context.get().getColor(R.color.color_purple_dark))
                .setContentTitle(name)
                .setContentText(sequence)
                .setAutoCancel(true)
                .setStyle(bigTextStyle)
                .setPriority(PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);
        builder.addAction(R.drawable.ic_check_purple, context.get().getString(R.string.correct).toUpperCase(), pendingIntentCorrect);
        builder.addAction(R.drawable.ic_clear_purple, context.get().getString(R.string.incorrect).toUpperCase(), pendingIntentInCorrect);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManagerCompat.notify(NOTIFICATION_ID_ENDCALL, notification);
    }


    /**
     * Display_after_call_1
     * DisplayAfterCall_Standard
     *
     * @param callLogDetail
     */
    public void showNotificationStandard(CallLogDetail callLogDetail) {
        if (callLogDetail == null || callLogDetail.getContact() == null) return;
        String name = callLogDetail.getContact().getName();
        if (Utils.isEmpty(name)) name = Utils.numberPhoneFormat(callLogDetail.getContact().getPhoneNumber());
        switch (callLogDetail.getTypeCallLog()) {
            case 1:
                name = context.get().getString(R.string.key_incoming_call) + ": " + name;
                break;
            case 2:
                name = context.get().getString(R.string.key_outgoing_call) + ": " + name;
                break;
            case 3:
                name = context.get().getString(R.string.key_missed_call) + ": " + name;
                break;
        }

        String mobileNetwork;
        if (Utils.isEmpty(callLogDetail.getContact().getMobileNetwork()) || callLogDetail.getContact().getMobileNetwork().equalsIgnoreCase(context.get().getString(R.string.unknown))) {
            mobileNetwork = callLogDetail.getContact().getNational();
        } else {
            mobileNetwork = context.get().getText(R.string.mobile).toString() + " - " + callLogDetail.getContact().getMobileNetwork() + "\n" + callLogDetail.getContact().getNational();
        }

        String type = null;
        if (!callLogDetail.getContact().isInContactBlock() && callLogDetail.getContact().getType() != Constants.PhoneNumberType.NONE) {
            if (callLogDetail.getContact().isInContactBlock())
                type = context.get().getString(R.string.key_block_number) + ": " + context.get().getString(callLogDetail.getContact().getType().getResId());
            else if (callLogDetail.getContact().isInContactSpam()) type = context.get().getString(R.string.spam) + ": " + context.get().getString(callLogDetail.getContact().getType().getResId());
        }


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        if (Utils.isEmpty(type)) {
            bigTextStyle.bigText(mobileNetwork);
        } else {
            bigTextStyle.bigText(type + "\n" + mobileNetwork);
        }


        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            builder = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
        } else {
            builder = new NotificationCompat.Builder(context.get());
        }

        Intent intentCall = new Intent(context.get(), NotificationActionReceiver.class);
        intentCall.setAction(ACTION_CALL);
        intentCall.putExtra(Constants.KEY_PHONE_NUMBER, callLogDetail.getContact().getPhoneNumber());
        intentCall.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentCall = PendingIntent.getBroadcast(context.get(), 0, intentCall, PendingIntent.FLAG_CANCEL_CURRENT);


        Intent intentMessage = new Intent(context.get(), NotificationActionReceiver.class);
        intentMessage.setAction(ACTION_MESSAGE);
        intentMessage.putExtra(Constants.KEY_PHONE_NUMBER, callLogDetail.getContact().getPhoneNumber());
        intentMessage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentMessage = PendingIntent.getBroadcast(context.get(), 1, intentMessage, PendingIntent.FLAG_CANCEL_CURRENT);


        Intent intentBlock = new Intent(context.get(), NotificationActionReceiver.class);
        intentBlock.setAction(ACTION_BLOCK_AND_REPORT);
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentConstants.KEY_INFO_CONTACT, callLogDetail);
        intentBlock.putExtras(bundle);
        intentBlock.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingBlock = PendingIntent.getBroadcast(context.get(), 2, intentBlock, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.drawable.ic_logo_white)
                .setColor(context.get().getColor(R.color.color_purple_dark))
                .setContentTitle(name)
                .setContentText(type)
                .setAutoCancel(true)
                .setStyle(bigTextStyle)
                .setPriority(PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);
        builder.addAction(R.drawable.ic_call_dark_gray_24dp, context.get().getString(R.string.call).toUpperCase(), pendingIntentCall);
        builder.addAction(R.drawable.ic_message_overlay, context.get().getString(R.string.message).toUpperCase(), pendingIntentMessage);
        builder.addAction(R.drawable.ic_block_dark_gray, context.get().getString(R.string.block_and_report).toUpperCase(), pendingBlock);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManagerCompat.notify(NOTIFICATION_ID_ENDCALL, notification);
    }


    /**
     * Display_after_call_4
     * DisplayAfterCall_AskWho
     *
     * @param callLogDetail
     */
    public void showNotificationAskWho(CallLogDetail callLogDetail) {
        if (callLogDetail == null || callLogDetail.getContact() == null) return;
        String name = callLogDetail.getContact().getName();
        if (Utils.isEmpty(name)) name = Utils.numberPhoneFormat(callLogDetail.getContact().getPhoneNumber());
        switch (callLogDetail.getTypeCallLog()) {
            case 1:
                name = context.get().getString(R.string.key_incoming_call) + ": " + name;
                break;
            case 2:
                name = context.get().getString(R.string.key_outgoing_call) + ": " + name;
                break;
            case 3:
                name = context.get().getString(R.string.key_missed_call) + ": " + name;
                break;
        }

        String mobileNetwork;
        if (Utils.isEmpty(callLogDetail.getContact().getMobileNetwork()) || callLogDetail.getContact().getMobileNetwork().equalsIgnoreCase(context.get().getString(R.string.unknown))) {
            mobileNetwork = callLogDetail.getContact().getNational();
        } else {
            mobileNetwork = context.get().getText(R.string.mobile).toString() + " - " + callLogDetail.getContact().getMobileNetwork() + "\n" + callLogDetail.getContact().getNational();
        }

        String type = null;
        if (!callLogDetail.getContact().isInContactBlock() && callLogDetail.getContact().getType() != Constants.PhoneNumberType.NONE) {
            if (callLogDetail.getContact().isInContactBlock())
                type = context.get().getString(R.string.key_block_number) + ": " + context.get().getString(callLogDetail.getContact().getType().getResId());
            else if (callLogDetail.getContact().isInContactSpam()) type = context.get().getString(R.string.spam) + ": " + context.get().getString(callLogDetail.getContact().getType().getResId());
        }

        int timeAnswer = CallLogManager.getDefault(context.get()).getCountAnswer(callLogDetail.getContact().getPhoneNumber());
        String contentText = context.get().getString(R.string.you_answered_this_number, timeAnswer);
        CharSequence sequence;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sequence = Html.fromHtml(context.get().getString(R.string.text_html_bold, contentText), Html.FROM_HTML_MODE_COMPACT);
        } else {
            sequence = Html.fromHtml(context.get().getString(R.string.text_html_bold, contentText));
        }

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        CharSequence sequenceBigStyle;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sequenceBigStyle = Html.fromHtml(context.get().getString(R.string.text_html_bold_2, Utils.isEmpty(type) ? mobileNetwork : type, contentText), Html.FROM_HTML_MODE_COMPACT);
        } else {
            sequenceBigStyle = Html.fromHtml(context.get().getString(R.string.text_html_bold_2, Utils.isEmpty(type) ? mobileNetwork : type, contentText));
        }
        bigTextStyle.bigText(sequenceBigStyle);


        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            builder = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
        } else {
            builder = new NotificationCompat.Builder(context.get());
        }


        Intent intentAskWho = new Intent(context.get(), NotificationActionReceiver.class);
        intentAskWho.setAction(ACTION_ASK_WHO);
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentConstants.KEY_INFO_CONTACT, callLogDetail);
        intentAskWho.putExtras(bundle);
        intentAskWho.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingAskWho = PendingIntent.getBroadcast(context.get(), 0, intentAskWho, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.drawable.ic_logo_white)
                .setColor(context.get().getColor(R.color.color_purple_dark))
                .setContentTitle(name)
                .setContentText(sequence)
                .setAutoCancel(true)
                .setStyle(bigTextStyle)
                .setPriority(PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);
        builder.addAction(R.drawable.ic_check_purple, context.get().getString(R.string.tell_us_who_this_is).toUpperCase(), pendingAskWho);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManagerCompat.notify(NOTIFICATION_ID_ENDCALL, notification);
    }

    public void showNotificationReportName() {
        String replyLabel = "reply";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY)
                .setLabel(replyLabel)
                .build();

        Intent intent;
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(context.get(), NotificationActionReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context.get(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            intent = new Intent(context.get(), NotificationActionReceiver.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(context.get(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_logo_white, replyLabel, pendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            builder = new NotificationCompat.Builder(context.get(), Constants.NOTIFICATION_CHANNEL_ID_ICALLER);
        } else {
            builder = new NotificationCompat.Builder(context.get());
        }
        builder.setSmallIcon(R.drawable.ic_logo_white)
                .setContentTitle("title")
                .setContentText("content")
                .addAction(replyAction);

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context.get());
        mNotificationManager.notify(NOTIFICATION_ID_ENDCALL, builder.build());
    }
}
