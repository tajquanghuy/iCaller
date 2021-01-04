package com.example.icaller_mobile.common.widget.dialog;
import com.example.icaller_mobile.common.utils.Utils;

public class ContentDialog {
    private String title;
    private String subtitle;
    private String message;
    private String textStartButton;
    private String textEndButton;

    public ContentDialog() {
    }

    public ContentDialog(String title, String subtitle, String message, String textStartButton, String textEndButton) {
        this.title = Utils.isEmpty(title) ? "" : title;
        this.subtitle = Utils.isEmpty(subtitle) ? "" : subtitle;
        this.message = Utils.isEmpty(message) ? "" : message;
        this.textStartButton = Utils.isEmpty(textStartButton) ? "" : textStartButton;
        this.textEndButton = Utils.isEmpty(textEndButton) ? "" : textEndButton;
    }

    public String getTitle() {
        return Utils.isEmpty(title) ? "" : title;
    }


    public String getSubtitle() {
        return Utils.isEmpty(subtitle) ? "" : subtitle;
    }


    public String getMessage() {
        return Utils.isEmpty(message) ? "" : message;
    }


    public String getTextStartButton() {
        return Utils.isEmpty(textStartButton) ? "" : textStartButton;
    }

    public String getTextEndButton() {
        return Utils.isEmpty(textEndButton) ? "" : textEndButton;
    }


}
