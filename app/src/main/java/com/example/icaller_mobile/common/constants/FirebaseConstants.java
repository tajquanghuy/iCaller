package com.example.icaller_mobile.common.constants;

public class FirebaseConstants {
    public static final String PARAM_PHONENUMBER = "phone_number";
    public static final String PARAM_AUTH_TOKEN = "api_token";
    public static final String PARAM_BLOCK_TYPE = "block_type";
    public static final String PARAM_REPORT_TYPE = "report_type";
    public static final String EVENT_MATCH_ID_DRAW = "match_caller_id_permitted_draw";

    /**
     * Event detect phone number (incoming call / outgoing call) in list contact-report (list update from Server)
     */
    public static final String EVENT_MATCH_ID = "match_caller_id";

    /**
     * Event block successed a call comes to the phone
     */
    public static final String EVENT_BLOCK_CALL = "block_call_successed";

    /**
     * Event click button Update DB in scr contact-block
     */
    public static final String EVENT_UPDATE_DATA = "update_data";

    /**
     * Event block local phone number
     * PARAM: phone_number
     * PARAM: type_report : 2 - REPORT_ADVERTISING
     *                      3 - REPORT_FINANCIAL_SERVICE
     *                      4 - REPORT_LOAN_COLLECTION
     *                      5 - REPORT_SCAM
     *                      6 - REPORT_REAL_ESTATE
     *                      7 - REPORT_OTHER
     */
    public static final String EVENT_BLOCK_CONTACT = "block_contact_request";

    /**
     * Event report phone number
     * PARAM: phone_number
     * PARAM: type_report : 2 - REPORT_ADVERTISING
     *                      3 - REPORT_FINANCIAL_SERVICE
     *                      4 - REPORT_LOAN_COLLECTION
     *                      5 - REPORT_SCAM
     *                      6 - REPORT_REAL_ESTATE
     *                      7 - REPORT_OTHER
     */
    public static final String EVENT_REPORT_CONTACT = "block_report_request";

    /**
     * Event when a call comes to the phone
     * PARAM: phone_number
     */
    public static final String EVENT_INCOMING_CALL = "incoming_call";

    /**
     * Event send otp firebase
     */
    public static final String EVENT_OUTGOING_CALL = "outgoing_call";

    /**
     * Event send otp firebase
     */
    public static final String EVENT_VERIFY_PHONENUMBER = "verify_phonenumber_request";

    /**
     * Event login App successed
     */
    public static final String EVENT_LOGIN_SUCCESSED = "login_successed";
    
    
    public static final String EVENT_DISPLAY_AFTER_CALL_1 = "DisplayAfterCall_Standard";
    public static final String EVENT_DISPLAY_AFTER_CALL_2 = "DisplayAfterCall_Recheck";
    public static final String EVENT_DISPLAY_AFTER_CALL_3 = "DisplayAfterCall_AskSpam";
    public static final String EVENT_DISPLAY_AFTER_CALL_4 = "DisplayAfterCall_AskWho";

    /**
     * Event open scr diapad
     */
    public static final String SCREEN_DIAPAD = "Screen_diapad";

    /**
     * Event open scr history
     */
    public static final String SCREEN_CALL_HISTORY = "Screen_call_history";


    /**
     * Event open scr contact-device
     */
    public static final String SCREEN_CONTACT_LIST = "Screen_contact_list";

    /**
     * Event open scr contact-block
     */
    public static final String SCREEN_SPAM_CONFIGURATION = "Screen_spam_configuration";

    /**
     * Event click button Search in src Contact
     */
    public static final String SCREEN_CONTACT_LIST_SEARCH = "Screen_contact_list_search";

    /**
     * Event open scr introduction
     */
    public static final String SCREEN_SETUP_INTRODUCTION = "Screen_setup_introduction";

    /**
     * Event open scr policy
     */
    public static final String SCREEN_SETUP_POLICY = "Screen_setup_policy";

    /**
     * Event open scr tearms of service
     */
    public static final String SCREEN_SETUP_TEARMSOFSERVICE = "Screen_setup_tearmsofservice";

    /**
     * Event open scr FAQ
     */
    public static final String SCREEN_SETUP_FAQ = "Screen_setup_faq";

    /**
     *  Event request permission draw over app
     *  event NOT_ALLOW_DRAW_OVER_APP_SCREEN : denied permission
     *  event ALLOW_DRAW_OVER_APP_SCREEN : allow permission
     */
    public static final String NOT_ALLOW_DRAW_OVER_APP_SCREEN = "not_allow_draw_over_app_screen";
    public static final String ALLOW_DRAW_OVER_APP_SCREEN = "allow_draw_over_app_screen";

    /**
     *  Event show dialog request permission contact
     *  event NOT_ALLOW_CONTACT_PERMISSION : denied permission
     *  event ALLOW_CONTACT_PERMISSION : allow permission
     */
    public static final String NOT_ALLOW_CONTACT_PERMISSION = "not_allow_contact_permission";
    public static final String ALLOW_CONTACT_PERMISSION = "allow_contact_permission";

    /**
     *  Event show dialog request permission call log
     *  event NOT_ALLOW_CALL_LOG_PERMISSION : denied permission
     *  event ALLOW_CALL_LOG_PERMISSION : allow permission
     */
    public static final String NOT_ALLOW_CALL_LOG_PERMISSION = "not_allow_call_log_permission";
    public static final String ALLOW_CALL_LOG_PERMISSION = "allow_call_log_permission";

    /**
     *  Event open scr Add contact-block
     */
    public static final String SCREEN_MANUAL_ADD_BLACK_LIST = "screen_manual_add_black_list";
}
