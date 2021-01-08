package com.example.icaller_mobile.common.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.modules.CallLogChangeObserverClass;
import com.example.icaller_mobile.common.utils.ContactManager;
import com.example.icaller_mobile.common.utils.PhoneNumberManager;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.model.base.IContactObject;
import com.example.icaller_mobile.model.callbacks.GetRoomDataCallBacks;
import com.example.icaller_mobile.model.local.room.BlockContact;
import com.example.icaller_mobile.model.network.response.event.BaseEvent;
import com.example.icaller_mobile.model.network.room.DataBean;
import com.example.icaller_mobile.model.report.CallLog;
import com.example.icaller_mobile.model.report.CallLogGroup;
import com.example.icaller_mobile.model.report.CallLogRequestServer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CallLogManager {

    private final int LIMIT = 100;
    private boolean isCallLogAccessible = false;
    private CallLogChangeObserverClass callLogObserver;
    private ContactManager contactManager;
    private LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 10, TimeUnit.SECONDS, queue);
    private static CallLogManager callLogManager;
    private WeakReference<Context> context;


    public void refresh() {
        refreshPermissions();
    }

    public void refreshPermissions() {
        if (isCallLogAccessible != isCallLogAccessible()) {
            isCallLogAccessible = !isCallLogAccessible;

            EventBus.getDefault().post(CallLogEventFactory.createCallLogPermissionChangedEvent());
        }

        if (isCallLogAccessible && callLogObserver == null) {
            callLogObserver = new CallLogChangeObserverClass(new Handler(Looper.getMainLooper()), context.get());
            context.get().getContentResolver().registerContentObserver(android.provider.CallLog.Calls.CONTENT_URI, true, callLogObserver);
        }
    }

    public boolean isCallLogAccessible() {
        return Utils.arePermissionsGranted(context.get(), Constants.PERMISSION_GROUP_CALL_LOG);
    }

    //endregion

    //region Manage call logs

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED, sticky = true)
    public void handleDeviceContactEvent(ContactManager.DeviceContactEvent event) {
        if (ContactManager.CONTACT_READY.equals(event.name)) {
            getCallLogGroupsAsync();
        }
    }

    @Subscribe
    public void handleICallerContactEvent(ContactManager.IDCallerContactChangedEvent event) {
        getCallLogGroupsAsync();
    }

    public static CallLogManager getDefault(Context context) {
        if (callLogManager == null) {
            callLogManager = new CallLogManager(context);
        }
        return callLogManager;
    }

    public CallLogManager(Context context) {
        this.context = new WeakReference<>(context);
        this.contactManager = ContactManager.getDefault();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        refresh();
    }

    // Return success or failed
    public boolean getCallLogGroupsAsync() {

        if (!isCallLogAccessible()) {
            return false;
        }

        executor.execute(() -> {
            if (getAllCallLogs() != null) {
                List<CallLog> lists = new ArrayList<>(getAllCallLogs());
                List<CallLogGroup> callLogGroups = new ArrayList<>();
                String currentGroup = "";

                for (CallLog contact : lists) {
                    String groupName = "";

                    try {
                        Calendar c = Calendar.getInstance();
                        long diff = c.getTimeInMillis() - contact.getDate();
                        int days = (int) Math.floor(diff / (double) (24 * 60 * 60 * 1000));

                        if (DateUtils.isToday(contact.getDate())) {
                            groupName = context.get().getText(R.string.key_Today).toString();
                        } else if (days < 2) {
                            groupName = context.get().getText(R.string.key_yesterday).toString();
                        } else {
                            groupName = DateUtils.formatDateTime(context.get(), contact.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_ALL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (groupName.isEmpty()) {
                        continue;
                    }

                    if (!currentGroup.equals(groupName)) {
                        currentGroup = groupName;
                        CallLogGroup newGroup = new CallLogGroup();
                        newGroup.title = groupName;
                        newGroup.list = new ArrayList<>();
                        callLogGroups.add(newGroup);
                    }
                    if (callLogGroups.size() > 0) {
                        callLogGroups.get(callLogGroups.size() - 1).list.add(contact);
                    }
                }
                EventBus.getDefault().post(CallLogEventFactory.createCallLogReadyEvent(callLogGroups));
            }
        });
        return true;
    }

    private CallLogRequestServer getCallLogRequestServerEntry(Cursor cursor) {
        int number = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);
        String phNumber = cursor.getString(number);
        int callType = cursor.getInt(type);
        long callDate = cursor.getLong(date);
        int callDuration = cursor.getInt(duration);
        int in_contact;
        if (Utils.arePermissionsGranted(context.get(), Constants.PERMISSION_GROUP_CONTACT)) {
            in_contact = contactManager.getDeviceContact(phNumber) != null ? 1 : 0;
        } else {
            in_contact = 2;
        }

        if (callType == android.provider.CallLog.Calls.OUTGOING_TYPE && callDuration == 0)
            callType = 4;


        /**
         * CallLogRequestServer(String phone, int type, String time, int duration, int in_contact)
         *
         * @param phone (String) : format phone number country code
         * @param type (int) :   1 - incoming call
         *                       2 - outgoing call
         *                       3 - missed call
         *                       4 - listener not listen
         * @param time (String): format yyyy-MM-dd HH:mm:ss
         * @param duration (int): unit seconds (s)
         * @param in_contact (int) :   0 - not in contact device
         *                             1 - in contact device
         *                             2 - deny permission contacts
         */

        return new CallLogRequestServer(PhoneNumberManager.getInstance(context.get()).convertNationalNumber(phNumber),
                callType,
                Utils.formatTime(callDate),
                callDuration,
                in_contact);
    }

    String cachedName;

    private CallLog getCallLogEntry(Cursor cursor) {
        int number = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);
        int name = cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);
        String phNumber = cursor.getString(number);
        String callType = cursor.getString(type);
        long callDate = cursor.getLong(date);
        String callDuration = cursor.getString(duration);
        cachedName = cursor.getString(name);
        String group = "";
        if (Utils.isEmpty(cachedName) && Utils.isEmpty(phNumber)) {
            cachedName = context.get().getText(R.string.key_unknown).toString();
        } else if (Utils.isEmpty(cachedName) && !Utils.isEmpty(phNumber)) {
            IContactObject contactDevice = ContactManager.getDefault().getDeviceContact(phNumber);
            if (contactDevice != null) {
                cachedName = (contactDevice.getName() == null || Utils.isEmpty(contactDevice.getName())) ? Utils.numberPhoneFormat(phNumber) : contactDevice.getName();
            } else {
                ContactManager.getDefault().zipObservable(phNumber, new GetRoomDataCallBacks() {
                    @Override
                    public void getUserFromServer(DataBean dataBean) {
                        cachedName = dataBean.getName();
                    }

                    @Override
                    public void getUserFromDevice(BlockContact blockContact) {
                        cachedName = blockContact.getName();
                    }

                    @Override
                    public void getUnknowUser() {
                        cachedName = Utils.isEmpty(cachedName) ? Utils.numberPhoneFormat(phNumber) : cachedName;
                    }
                });
            }

        }
        String phut = String.valueOf(Integer.parseInt(callDuration) / 60);
        String phuts;
        if (phut.length() == 1) {
            phuts = "0" + phut;
        } else {
            phuts = phut;
        }

        String giay = String.valueOf(Integer.parseInt(callDuration) % 60);
        String giays;
        if (giay.length() == 1) {
            giays = "0" + giay;
        } else {
            giays = giay;
        }
        String durations = phuts + ":" + giays;
        String defaultRegion = SharedPreferencesManager.getDefault(context.get()).getCurrentCountry();
        return new CallLog(cachedName, phNumber, PhoneNumberManager.getInstance(context.get()).convertNationalNumber(phNumber), callType, callDate, durations, group);
    }

    public CallLog getCallLogFirst() {
        if (!isCallLogAccessible()) {
            return null;
        }
        CallLog callLog = null;
        final String sortOrder = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER + " LIMIT " + 1;

        @SuppressLint("MissingPermission")
        Cursor cursor = context.get().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                null, null, null, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.isFirst()) {
                    callLog = getCallLogEntry(cursor);
                } else {
                    break;
                }
            }

            cursor.close();
            return callLog;
        }
        return null;
    }

    public List<CallLog> getAllCallLogs() {
        if (!isCallLogAccessible()) {
            return Collections.emptyList();
        }
        final String sortOrder = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER + " limit " + LIMIT;

        @SuppressLint("MissingPermission")
        Cursor cursor = context.get().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                null, null, null, sortOrder);

        if (cursor != null) {
            List<CallLog> lists = new ArrayList<>();
            while (cursor.moveToNext()) {
                CallLog callLog = getCallLogEntry(cursor);
                lists.add(callLog);
            }
            cursor.close();
            return lists;
        }
        return null;
    }

    public List<CallLogRequestServer> getAllCallLogsAfterDate() {
        if (!isCallLogAccessible()) {
            return Collections.emptyList();
        }
        final String sortOrder = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER + " limit " + LIMIT;
        String mSelectionClause;
        String[] mSelectionArgs;

        String dateUpload = SharedPreferencesManager.getDefault(context.get()).getDateUploadHistory();
        if (dateUpload == null || Utils.isEmpty(dateUpload)) {
            dateUpload = Constants.dateDefault;
        }
        mSelectionClause = android.provider.CallLog.Calls.DATE + " > ?";
        mSelectionArgs = new String[]{String.valueOf(Utils.formatTimeFromString(dateUpload).getMillis())};

        @SuppressLint("MissingPermission")
        Cursor cursor = context.get().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                null, mSelectionClause, mSelectionArgs, sortOrder);

        if (cursor != null) {
            List<CallLogRequestServer> lists = new ArrayList<>();
            while (cursor.moveToNext()) {
                CallLogRequestServer callLog = getCallLogRequestServerEntry(cursor);
                lists.add(callLog);
            }
            cursor.close();
            return lists;
        }
        return null;
    }


    public List<CallLog> getAllCallLogsBefore() {
        if (!isCallLogAccessible()) {
            return null;
        }
        CallLog callLog;
        final String sortOrder = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER + " LIMIT " + LIMIT;
        @SuppressLint("MissingPermission")
        Cursor cursor = context.get().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                null, null, null, sortOrder);
        if (cursor != null) {
            List<CallLog> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                if (!cursor.isFirst()) {
                    callLog = getCallLogEntry(cursor);
                    list.add(callLog);
                }
            }
            cursor.close();
            return list;
        }
        return null;
    }

    public boolean isContainsCallLog(String number) {
        if (Utils.isEmpty(number)) return false;
        if (getAllCallLogsBefore() != null) {
            List<CallLog> list = new ArrayList<>(getAllCallLogsBefore());
            String nationalNumber = PhoneNumberManager.getInstance(context.get()).convertNationalNumber(number);
            CallLog callLogNationalNumber = new CallLog(nationalNumber);
            return list.contains(callLogNationalNumber);
        }
        return false;
    }

    public int getCountAnswer(String number) {
        if (number == null || Utils.isEmpty(number)) return 1;
        int count = 0;
        if (getAllCallLogs() != null) {
            List<CallLog> list = new ArrayList<>(getAllCallLogs());
            for (CallLog callLog : list) {
                String phone = callLog.getPhone() == null || Utils.isEmpty(callLog.getPhone()) ? "" : callLog.getPhone();
                String nationalNumber = callLog.getNationalNumber() == null || Utils.isEmpty(callLog.getNationalNumber()) ? "" : callLog.getNationalNumber();
                String convertNationalNumber = PhoneNumberManager.getInstance(context.get()).convertNationalNumber(number) == null || Utils.isEmpty(PhoneNumberManager.getInstance(context.get()).convertNationalNumber(number)) ? "" : PhoneNumberManager.getInstance(context.get()).convertNationalNumber(number);
                if (phone.equals(number) || (convertNationalNumber != null && !Utils.isEmpty(convertNationalNumber) && nationalNumber.equals(convertNationalNumber))) {
                    count++;
                }
            }
            return count;
        }
        return 1;
    }


    //region Events

    public static final String CALL_LOG_SESSION_CHANGED = "CALL_LOG_SESSION_CHANGED";
    public static final String CALL_LOG_GROUP_READY = "CALL_LOG_GROUP_READY";
    public static final String CALL_LOG_PERMISSION_CHANGED = "CALL_LOG_PERMISSION_CHANGED";

    public static class CallLogEventFactory {
        public static CallLogEvent createCallLogReadyEvent(List<CallLogGroup> callLogGroups) {
            CallLogEvent event = new CallLogEvent(CALL_LOG_GROUP_READY);
            event.setCallLogGroups(callLogGroups);
            return event;
        }

        public static CallLogEvent createCallLogPermissionChangedEvent() {
            return new CallLogEvent(CALL_LOG_PERMISSION_CHANGED);
        }

        public static CallLogEvent createCallLogSessionChangedEvent() {
            return new CallLogEvent(CALL_LOG_SESSION_CHANGED);
        }
    }

    public static class CallLogEvent extends BaseEvent {
        private List<CallLogGroup> callLogGroups = new ArrayList<>();

        public List<CallLogGroup> getCallLogGroups() {
            return callLogGroups;
        }

        public CallLogEvent(String name) {
            super(name);
        }

        public void setCallLogGroups(List<CallLogGroup> callLogGroups) {
            if (callLogGroups != null) {
                this.callLogGroups.clear();
                this.callLogGroups.addAll(callLogGroups);
            }
        }
    }

    //endregion Events
}
