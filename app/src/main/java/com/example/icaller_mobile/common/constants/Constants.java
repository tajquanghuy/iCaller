package com.example.icaller_mobile.common.constants;

import android.Manifest;
import android.os.Build;
import android.os.Environment;

import com.example.icaller_mobile.R;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {
    public static final String CONNECTION_SERVICE_ID = "vn.grooo.iCallerConnectionServiceID";
    public static final String CONNECTION_SERVICE_PHONE_ACCOUNT = "vn.grooo.iCallerConnectionServicePhoneAccount";
    public static final String PATH_FILE = Environment.getExternalStorageDirectory().getPath() + "/i-Caller";
    public static final String PARAM_SELECT_GET_DB = "id,code,phone,name,warn_type,updated_at";
    public static final String[] PERMISSION_GROUP_CALL_LOG = Build.VERSION.SDK_INT < Build.VERSION_CODES.O
            ? new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS}
            : new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.ANSWER_PHONE_CALLS};
    public static final String[] PERMISSION_GROUP_PHONE = Build.VERSION.SDK_INT < Build.VERSION_CODES.O
            ? new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS}
            : new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.ANSWER_PHONE_CALLS};
    public static final String[] PERMISSION_GROUP_CONTACT = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    public static final String KEY_PRIVACY_POLICY = "https://icaller.info/index.php/privacy-policy/";
    public static final String KEY_TOU = "https://icaller.info/index.php/terms-of-service/";
    public final static String COUNTRY_CODE_VN = "84";
    public final static String ISO_VIETNAM = "VN";
    public final static float CLICK_DRAG_TOLERANCE = 10;
    public static final String FILE_NAME = "shared.preferences.icaller";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final String TRANSFORMATION = "AES/GCM/NoPadding";
    public static final int TIME_AUTO_CLOSE_DIALOG = 5000;
    public static final String SAMPLE_ALIAS = "app_cowater";
    public static String PHONENUMBER = "";
    public static final String dateDefault = "2020-01-01 00:00:00";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(DateTimeZone.getDefault());

    public static String API_TOKEN;

    public static final String NOTIFICATION_CHANNEL_ID_ICALLER = "info.icaller.ICALLER";
    public static final String NOTIFICATION_CHANNEL_ID_ICALLER_CALL = "info.icaller.ICALLER_CALL";
    public static final int NOTIFICATION_ID_OVERLAY_SETTINGS = 2;
    public static final String NOTIFICATION_GROUP_CALL = "info.icaller.NOTIFICATION_GROUP_CALL";
    public static final String NOTIFICATION_GROUP = "info.icaller.NOTIFICATION_GROUP";
    public static final String NOTIFICATION_ACTION_NONE = "info.icaller.ACTION_NONE";

    public enum ReportType {
        NORMAL(1),
        REPORT_ADVERTISING(2, R.string.advertising, R.drawable.ic_red_advertising, R.drawable.ic_white_advertising),
        REPORT_FINANCIAL_SERVICE(3, R.string.financial_service, R.drawable.ic_red_financial_service, R.drawable.ic_white_financial_service),
        REPORT_LOAN_COLLECTION(4, R.string.loan_collection, R.drawable.ic_red_loan_collection, R.drawable.ic_white_loan_collection),
        REPORT_SCAM(5, R.string.scam, R.drawable.ic_red_scam, R.drawable.ic_white_scam),
        REPORT_REAL_ESTATE(6, R.string.real_estate, R.drawable.ic_red_real_estate, R.drawable.ic_white_real_estate),
        REPORT_OTHER(7, R.string.other, R.drawable.ic_red_other, R.drawable.ic_white_other);

        private int type;
        private int name;
        private int imageRed;
        private int imageWhite;

        ReportType() {

        }

        ReportType(int type) {
            this.type = type;
        }

        ReportType(int type, int name, int imageRed, int imageWhite) {
            this.type = type;
            this.name = name;
            this.imageRed = imageRed;
            this.imageWhite = imageWhite;
        }

        public int getType() {
            return type;
        }

        public int getName() {
            return name;
        }

        public int getImageRed() {
            return imageRed;
        }

        public int getImageWhite() {
            return imageWhite;
        }
    }

    public enum ToolbarStyle {
        Backable,
        OnlyText,
        BonusSearch,
        SearchBar,
        None
    }
}
