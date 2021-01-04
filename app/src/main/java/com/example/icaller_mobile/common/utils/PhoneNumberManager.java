package com.example.icaller_mobile.common.utils;

import android.content.Context;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.manager.SharedPreferencesManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class PhoneNumberManager {
    private PhoneNumberUtil phoneNumberUtil;
    private static PhoneNumberManager phoneNumberManager;
    private WeakReference<Context> context;

    public PhoneNumberManager(Context mContext) {
        this.phoneNumberUtil = PhoneNumberUtil.createInstance(mContext);
        this.context = new WeakReference<>(mContext);
    }

    public static PhoneNumberManager getInstance(Context context) {
        if (phoneNumberManager == null) {
            phoneNumberManager = new PhoneNumberManager(context);
        }
        return phoneNumberManager;
    }

    public PhoneNumberUtil getPhoneNumberUtil() {
        return phoneNumberUtil;
    }

    public boolean isValidPhoneNumber(String phone) {
        if (phone == null || Utils.isEmpty(phone)) return false;
        if (Utils.removepecialCharacters(phone).matches("[0-9+]+")) {
            return phone.length() >= 8 && phone.length() <= 20;
        }
        return false;
    }

    public String extractNationalNumber(String number) {
        if (Utils.isEmpty(number)) {
            return null;
        }


        String extracted = null;
        try {
            Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(number, null);
            if (phoneNumberUtil.isValidNumber(pn)) {
                extracted = "" + pn.getNationalNumber();
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return extracted;
    }


    public String convertNationalNumber(String number) {
        if (Utils.isEmpty(number)) {
            return null;
        }
        if (!isValidPhoneNumber(number)) return number;

        String convert = null;
        try {
            com.google.i18n.phonenumbers.PhoneNumberUtil phoneNumberUtil = com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance();
            String defaultRegion = SharedPreferencesManager.getDefault(context.get()).getCurrentCountry();
            com.google.i18n.phonenumbers.Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(number, (defaultRegion == null || Utils.isEmpty(defaultRegion) ? Constants.ISO_VIETNAM : defaultRegion).toUpperCase());
            convert = phoneNumberUtil.format(pn, com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.E164);
            return convert;
        } catch (Exception e) {
            e.printStackTrace();
            return number;
        }
    }


    public String getDisplayNameCountry(Context mContext, String phoneNumber) {
        if (Utils.isEmpty(phoneNumber)) {
            return null;
        }

        if (switchboard_network.containsKey(Utils.removepecialCharacters(phoneNumber)))
            return "Việt Nam";

        try {
            String phone = convertNationalNumber(phoneNumber);
            if (phone != null && phone.length() > 4) {
                String defaultRegion = SharedPreferencesManager.getDefault(context.get()).getCurrentCountry();
                String country = phoneNumberUtil.getRegionCodeForNumber(phoneNumberUtil.parse(phone, defaultRegion == null || Utils.isEmpty(defaultRegion) ? Constants.ISO_VIETNAM : defaultRegion));
                Locale loc = new Locale("", country);
                String displayNameCountry = loc.getDisplayCountry();
                if (displayNameCountry.trim().equals("Moldova") || phone.substring(0, 4).trim().equals("+373")
                        || displayNameCountry.trim().equals("Tunisia") || phone.substring(0, 4).trim().equals("+216")
                        || displayNameCountry.trim().equals("Equatorial Guinea") || phone.substring(0, 4).trim().equals("+240")
                        || displayNameCountry.trim().equals("Burkina Faso") || phone.substring(0, 4).trim().equals("+226")) {
                    return displayNameCountry + " - " + mContext.getString(R.string.possibility_of_scam);
                }
                return displayNameCountry;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNetworkMobile(String phoneNumber) {
        if (phoneNumber == null || Utils.isEmpty(phoneNumber)) {
            return null;
        }

        if (switchboard_network.containsKey(Utils.removepecialCharacters(phoneNumber)))
            return switchboard_network.get(Utils.removepecialCharacters(phoneNumber));

        String phone = convertNationalNumber(phoneNumber);
        if (phone != null && phone.length() > 3) {
            return mobile_network_to_indicative.get(Utils.fix84(phone).substring(0, 3));
        }
        return null;
    }

    public Map<String, String> switchboard = new HashMap<String, String>();

    {
        switchboard.put("900", "Tổng đài Viettel");
        switchboard.put("18008098", "Tổng đài Viettel");
        switchboard.put("198", "Tổng đài Viettel");
        switchboard.put("197", "Tổng đài Viettel");

        switchboard.put("18001091", "Tổng đài VinaPhone");

        switchboard.put("9090", "Tổng đài Mobifone");
        switchboard.put("18001090", "Tổng đài Mobifone");
        switchboard.put("9393", "Tổng đài Mobifone");

        switchboard.put("0922789789", "Tổng đài Vietnamobile");
        switchboard.put("0922123123", "Tổng đài Vietnamobile");
        switchboard.put("366", "Tổng đài Vietnamobile");
        switchboard.put("0922366366", "Tổng đài Vietnamobile");
        switchboard.put("2121", "Tổng đài Vietnamobile");
        switchboard.put("0923452121", "Tổng đài Vietnamobile");

        switchboard.put("199", "Tổng đài Gmobile");
        switchboard.put("01998880199", "Tổng đài Gmobile");
        switchboard.put("186", "Tổng đài Gmobile");
        switchboard.put("01998880186", "Tổng đài Gmobile");
    }

    private Map<String, String> switchboard_network = new HashMap<String, String>();

    {
        switchboard_network.put("900", "Viettel");
        switchboard_network.put("18008098", "Viettel");
        switchboard_network.put("198", "Viettel");
        switchboard_network.put("197", "Viettel");

        switchboard_network.put("18001091", "VinaPhone");

        switchboard_network.put("9090", "Mobifone");
        switchboard_network.put("18001090", "Mobifone");
        switchboard_network.put("9393", "Mobifone");

        switchboard_network.put("0922789789", "Vietnamobile");
        switchboard_network.put("0922123123", "Vietnamobile");
        switchboard_network.put("366", "Vietnamobile");
        switchboard_network.put("0922366366", "Vietnamobile");
        switchboard_network.put("2121", "Vietnamobile");
        switchboard_network.put("0923452121", "Vietnamobile");

        switchboard_network.put("199", "Gmobile");
        switchboard_network.put("01998880199", "Gmobile");
        switchboard_network.put("186", "Gmobile");
        switchboard_network.put("01998880186", "Gmobile");
    }

    private Map<String, String> mobile_network_to_indicative = new HashMap<String, String>();

    {
        mobile_network_to_indicative.put("086", "Viettel");
        mobile_network_to_indicative.put("096", "Viettel");
        mobile_network_to_indicative.put("097", "Viettel");
        mobile_network_to_indicative.put("098", "Viettel");
        mobile_network_to_indicative.put("032", "Viettel");
        mobile_network_to_indicative.put("033", "Viettel");
        mobile_network_to_indicative.put("034", "Viettel");
        mobile_network_to_indicative.put("035", "Viettel");
        mobile_network_to_indicative.put("036", "Viettel");
        mobile_network_to_indicative.put("037", "Viettel");
        mobile_network_to_indicative.put("038", "Viettel");
        mobile_network_to_indicative.put("039", "Viettel");


        mobile_network_to_indicative.put("089", "Mobifone");
        mobile_network_to_indicative.put("090", "Mobifone");
        mobile_network_to_indicative.put("093", "Mobifone");
        mobile_network_to_indicative.put("070", "Mobifone");
        mobile_network_to_indicative.put("079", "Mobifone");
        mobile_network_to_indicative.put("077", "Mobifone");
        mobile_network_to_indicative.put("076", "Mobifone");
        mobile_network_to_indicative.put("078", "Mobifone");

        mobile_network_to_indicative.put("088", "Vinaphone");
        mobile_network_to_indicative.put("091", "Vinaphone");
        mobile_network_to_indicative.put("094", "Vinaphone");
        mobile_network_to_indicative.put("083", "Vinaphone");
        mobile_network_to_indicative.put("084", "Vinaphone");
        mobile_network_to_indicative.put("085", "Vinaphone");
        mobile_network_to_indicative.put("081", "Vinaphone");
        mobile_network_to_indicative.put("082", "Vinaphone");

        mobile_network_to_indicative.put("099", "Gmobile");
        mobile_network_to_indicative.put("059", "Gmobile");

        mobile_network_to_indicative.put("092", "Vietnamobile");
        mobile_network_to_indicative.put("052", "Vietnamobile");
        mobile_network_to_indicative.put("056", "Vietnamobile");
        mobile_network_to_indicative.put("058", "Vietnamobile");
    }

    private Map<String, String> area_code_to_indicative = new HashMap<String, String>();

    {
        area_code_to_indicative.put("296", "An Giang");
        area_code_to_indicative.put("254", "Bà Rịa – Vũng Tàu");
        area_code_to_indicative.put("209", "Bắc Cạn");
        area_code_to_indicative.put("204", "Bắc Giang");
        area_code_to_indicative.put("291", "Bạc Liêu");
        area_code_to_indicative.put("222", "Bắc Ninh");
        area_code_to_indicative.put("275", "Bến Tre");
        area_code_to_indicative.put("256", "Bình Định");
        area_code_to_indicative.put("274", "Bình Dương");
        area_code_to_indicative.put("271", "Bình Phước");
        area_code_to_indicative.put("252", "Bình Thuận");
        area_code_to_indicative.put("290", "Cà Mau");
        area_code_to_indicative.put("292", "Cần Thơ");
        area_code_to_indicative.put("206", "Cao Bằng");
        area_code_to_indicative.put("236", "Đà Nẵng");
        area_code_to_indicative.put("262", "Đắk Lắk");
        area_code_to_indicative.put("261", "Đắk Nông");
        area_code_to_indicative.put("215", "Điện Biên");
        area_code_to_indicative.put("251", "Đồng Nai");
        area_code_to_indicative.put("277", "Đồng Tháp");
        area_code_to_indicative.put("269", "Gia Lai");
        area_code_to_indicative.put("226", "Hà Nam");
        area_code_to_indicative.put("24", "Hà Nội");
        area_code_to_indicative.put("239", "Hà Tĩnh");
        area_code_to_indicative.put("220", "Hải Dương");
        area_code_to_indicative.put("225", "Hải Phòng");
        area_code_to_indicative.put("293", "Hậu Giang");
        area_code_to_indicative.put("28", "Hồ Chí Minh");
        area_code_to_indicative.put("221", "Hưng Yên");
        area_code_to_indicative.put("258", "Khánh Hoà");
        area_code_to_indicative.put("260", "Kon Tum");
        area_code_to_indicative.put("213", "Lai Châu");
        area_code_to_indicative.put("263", "Lâm Đồng");
        area_code_to_indicative.put("205", "Lạng Sơn");
        area_code_to_indicative.put("214", "Lào Cai");
        area_code_to_indicative.put("272", "Long An");
        area_code_to_indicative.put("228", "Nam Định");
        area_code_to_indicative.put("238", "Nghệ An");
        area_code_to_indicative.put("259", "Ninh Thuận");
        area_code_to_indicative.put("229", "Ninh Bình");
        area_code_to_indicative.put("257", "Phú Yên");
        area_code_to_indicative.put("232", "Quảng Bình");
        area_code_to_indicative.put("235", "Quảng Nam");
        area_code_to_indicative.put("255", "Quảng Ngãi");
        area_code_to_indicative.put("203", "Quảng Ninh");
        area_code_to_indicative.put("233", "Quảng Trị");
        area_code_to_indicative.put("299", "Sóc Trăng");
        area_code_to_indicative.put("212", "Sơn La");
        area_code_to_indicative.put("276", "Tây Ninh");
        area_code_to_indicative.put("227", "Thái Bình");
        area_code_to_indicative.put("208", "Thái Nguyên");
        area_code_to_indicative.put("237", "Thanh Hóa");
        area_code_to_indicative.put("234", "Thừa Thiên – Huế");
        area_code_to_indicative.put("273", "Tiền Giang");
        area_code_to_indicative.put("294", "Trà Vinh");
        area_code_to_indicative.put("207", "Tuyên Quang");
        area_code_to_indicative.put("270", "Vĩnh Long");
        area_code_to_indicative.put("216", "Yên Bái");
        area_code_to_indicative.put("210", "Phú Thọ");
        area_code_to_indicative.put("211", "Vĩnh Phúc");
    }


}
