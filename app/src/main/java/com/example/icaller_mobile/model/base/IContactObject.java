package com.example.icaller_mobile.model.base;

/**
 * Created by baonv on 18,July,2019
 * Hiworld JSC.
 */
public interface IContactObject {
    String getGroup();

    void setGroup(String group);

    String getLookupKey();

    void setLookupKey(String lookupKey);

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    IContactPhoneList getNumbers();

    void setNumbers(IContactPhoneList numbers);

    int getWarnType();

    void setWarnType(int warnType);
}
