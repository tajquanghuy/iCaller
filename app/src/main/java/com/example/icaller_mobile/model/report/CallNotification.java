package com.example.icaller_mobile.model.report;

import android.provider.CallLog;
import android.text.Html;

import com.example.icaller_mobile.common.utils.Utils;


public class CallNotification {
    public int id;
    public String number;
    public String group;
    public int type;
    public int state;
    public long when;
    public boolean shown;

    private String name;

    public String getName() {
        return Utils.isEmpty(name) ? formattedNumber() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String formattedNumber() {
        if (Utils.isEmpty(this.number)) {
            return "";
        }

        return Utils.Number(this.number);
    }

    public CharSequence titleByType() {
        TitleAttributes attrs = titleAttributesByType();
        String html = "<font color='" + attrs.colorCode + "'><b>" + attrs.prefix + "&nbsp;" + name + "</b></font>";
        return Html.fromHtml(html);
    }

    public CharSequence fullTitleByType() {

        if (Utils.isEmpty(name) || Utils.isEmpty(number)) {
            return "";
        }

        TitleAttributes attrs = titleAttributesByType();
        String html = "<font color='" + attrs.colorCode + "'><b>" + attrs.prefix + "&nbsp;" + name + "</b> " + number + "</font>";
        return Html.fromHtml(html);
    }

    private TitleAttributes titleAttributesByType() {
        String prefix = "";
        String color = "";
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE:
                prefix = "↙";
                color = "#2E8B57";
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                prefix = "↗";
                color = "#1E90FF";
                break;
            case CallLog.Calls.MISSED_TYPE:
                prefix = "↙";
                color = "red";
                break;
        }
        return new TitleAttributes(prefix, color);
    }

    class TitleAttributes {
        public String prefix;
        public String colorCode;

        public TitleAttributes(String prefix, String colorCode) {
            this.prefix = prefix;
            this.colorCode = colorCode;
        }
    }
}
