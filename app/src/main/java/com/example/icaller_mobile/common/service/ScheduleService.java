package com.example.icaller_mobile.common.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.icaller_mobile.common.manager.SharedPreferencesManager;
import com.example.icaller_mobile.common.utils.NetworkUtils;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.interfaces.GetNumberPhoneDBCallBack;
import com.example.icaller_mobile.model.network.ServiceHelper;
import com.example.icaller_mobile.model.network.response.NumberContactBlockResponse;
import com.example.icaller_mobile.model.report.ScheduleReceiver;

import org.joda.time.DateTime;
import org.joda.time.Days;


public class ScheduleService extends Service {
    public static int SCHEDULE_REPEAT_HOURLY_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        if (shouldUpdateDaily(context)) {
            if (NetworkUtils.isOn(context)) {
                String versionData = SharedPreferencesManager.getDefault(context).getDateUpdateData();
                if (versionData != null && !Utils.isEmpty(versionData)) {
                    ServiceHelper.getDefault(context).updateDataDaily(">" + versionData, new GetNumberPhoneDBCallBack() {
                        @Override
                        public void getNumberPhoneDBSuccess(NumberContactBlockResponse response) {
                            //TODO: Notification
//                            NotificationManager.getDefault().showNotificationScheduleUpdate("Update Data Success");
                            if (response != null) {
                                int number = response.getData();
                                if (number > 0) {
                                    SharedPreferencesManager.getDefault(context).saveLastUpdateDBDaily();
                                    int id = SharedPreferencesManager.getDefault(context).getIDLastestCurrent();
                                    ServiceHelper.getDefault(context).getPhoneDB(context, ">" + versionData, id);
                                }
                            }
                        }

                        @Override
                        public void getNumberPhoneDBFail(String message) {
                            int hourCurrent = getHourCurrent();
                            if (hourCurrent < 18) {
                                setSchedule(context, hourCurrent + 1, "Update Data Fail");
                            }
                        }
                    });
//
//                    List<CallLogRequestServer> callLogs = CallLogManager.getDefault(context).getAllCallLogsYesterday();
//                    if (callLogs != null)
//                        ServiceHelper.getDefault(context).updateCallLogsServer(new HashSet<>(callLogs));
                }
            } else {
                int hourCurrent = getHourCurrent();
                if (hourCurrent < 18) {
                    setSchedule(context, hourCurrent + 1, "Network off");
                }
            }
        }
        return START_STICKY;
    }

    private boolean shouldUpdateDaily(Context context) {
        long previousTime = SharedPreferencesManager.getDefault(context).lastUpdateDBDaily();
        DateTime previousDate = new DateTime(previousTime);
        return Math.abs(Days.daysBetween(previousDate.withTimeAtStartOfDay(), DateTime.now().withTimeAtStartOfDay()).getDays()) >= 1;
    }

    private void setSchedule(Context context, int hour, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, ScheduleReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, SCHEDULE_REPEAT_HOURLY_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
        DateTime scheduleUpdate = new DateTime().withHourOfDay(hour).withMinuteOfHour(0).withSecondOfMinute(0);
        long milliseconds = scheduleUpdate.getMillis();
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, milliseconds, pendingIntent);


            //TODO: Notification
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, milliseconds, pendingIntent);
//            NotificationManager.getDefault().showNotificationScheduleUpdate(message + " : " + Utils.formatTime(milliseconds));
        }

    }


    private int getHourCurrent() {
        try {
            return DateTime.now().getHourOfDay();
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
