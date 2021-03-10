package com.example.icaller_mobile.common.utils;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepository;
import com.example.icaller_mobile.model.base.IContactObject;
import com.example.icaller_mobile.model.callbacks.GetDisplayNameCallBacks;
import com.example.icaller_mobile.model.callbacks.GetRoomDataCallBacks;
import com.example.icaller_mobile.model.local.room.BlockContact;
import com.example.icaller_mobile.model.local.room.BlockContactDAO;
import com.example.icaller_mobile.model.local.room.BlockContactDB;
import com.example.icaller_mobile.model.network.response.contact_respone.ContactObject;
import com.example.icaller_mobile.model.network.response.contact_respone.ContactPhoneObject;
import com.example.icaller_mobile.model.network.response.event.BaseEvent;
import com.example.icaller_mobile.model.network.room.DataBean;
import com.example.icaller_mobile.model.network.room.DataBeanDAO;
import com.example.icaller_mobile.model.network.room.DataBeanRepository;
import com.example.icaller_mobile.model.network.room.DatabeanRepositoryImp;
import com.google.i18n.phonenumbers.NumberParseException;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ContactManager {
    private boolean isContactAccessible = false;
    private WeakReference<Context> context;
    private LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 10, TimeUnit.SECONDS, queue);
    private static ContactManager contactManager;
    private static DataBeanRepository dataBeanRepository;
    private BlockContactRepository blockContactRepository;
    private DataBean dataBean;

    public static ContactManager init(Context context) {
        if (contactManager == null) {
            contactManager = new ContactManager(context);
            dataBeanRepository = new DatabeanRepositoryImp(context);

        }
        return contactManager;
    }

    public static ContactManager getDefault() {
        return contactManager;
    }


    public ContactManager(Context context) {
        this.context = new WeakReference<>(context);
    }


    public boolean isPhoneNumberOutContact(String phone) {
        if (phone == null || Utils.isEmpty(phone)) return true;
        return getDeviceContact(phone) == null;
    }

    public boolean isPhoneNumberForeign(String phone) {
        if (phone == null || Utils.isEmpty(phone)) return true;
        try {
            com.google.i18n.phonenumbers.PhoneNumberUtil phoneNumberUtil = com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance();
            com.google.i18n.phonenumbers.Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(phone, "VN");
            return pn.getCountryCode() != 84;
        } catch (NumberParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    // Danh bạ
    public IContactObject getDeviceContact(String phoneNumber) {
        if (!isContactAccessible() || phoneNumber == null || Utils.isEmpty(phoneNumber)) {
            return null;
        }
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.LOOKUP_KEY,
                ContactsContract.PhoneLookup.TYPE
        };

        Cursor c = context.get().getContentResolver().query(uri, projection, ContactsContract.PhoneLookup.NUMBER + "=?", new String[]{phoneNumber}, ContactsContract.PhoneLookup.DISPLAY_NAME);
        IContactObject contact = null;
        if (c != null) {
            if (c.moveToFirst()) {
                int idIndex = c.getColumnIndex(ContactsContract.PhoneLookup._ID);
                int nameIndex = c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                String contactId = c.getString(idIndex);
                String contactDisplayName = c.getString(nameIndex);
                String number = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
                String lookupKey = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.LOOKUP_KEY));
                int type = c.getInt(c.getColumnIndex(ContactsContract.PhoneLookup.TYPE));
                String customLabel = "Custom";
                CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.get().getResources(), type, customLabel);
                contact = new ContactObject(contactId, contactDisplayName, lookupKey);
                String a = Normalizer.normalize(((contactDisplayName != null && contactDisplayName.length() > 0 ? contactDisplayName.charAt(0) : "") + "").toUpperCase(), Normalizer.Form.NFD);
                a = a.replaceAll("[^\\p{ASCII}]", "");
                if (a.equalsIgnoreCase("") || a.equalsIgnoreCase("d")) {
                    contact.setGroup("D");
                } else if (a.toUpperCase().compareTo("A") < 0) {
                    contact.setGroup("#");
                } else {
                    contact.setGroup(a);
                }
                contact.getNumbers().add(new ContactPhoneObject(number, phoneType.toString()));
            }
            c.close();
        }
        return contact;
    }


    public String getDeviceContactName(String phoneNumber) {
        if (!isContactAccessible() || phoneNumber == null || Utils.isEmpty(phoneNumber)) {
            return null;
        }
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = context.get().getContentResolver().query(uri, projection, ContactsContract.PhoneLookup.NUMBER + "=?", new String[]{phoneNumber}, ContactsContract.PhoneLookup.DISPLAY_NAME);
        String name = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(0);
            }
            cursor.close();
        }
        return name;
    }

    public void getBlockedContactWithNumber(String number) {
        BlockContactDB blockContactDB = BlockContactDB.getDatabase(context.get());
        BlockContactDAO blockContactDAO = blockContactDB.blockContactDAO();
        Disposable disposable = blockContactDAO.getContactLocal(number)
                .subscribe(BlockContact::getName, throwable -> {
                    int a = 1;
                });
    }

    public void zipObservable(String phoneNumber, GetRoomDataCallBacks dataCallBacks) {
        BlockContactDB dataBeanDB = BlockContactDB.getDatabase(context.get());
        DataBeanDAO dataBeanDAO = dataBeanDB.dataBeanDAO();
        BlockContactDB blockContactDB = BlockContactDB.getDatabase(context.get());
        BlockContactDAO blockContactDAO = blockContactDB.blockContactDAO();
        Disposable disposable = Observable.zip(dataBeanDAO.getContactServer(phoneNumber), blockContactDAO.getListContactLocal(phoneNumber), DataZip::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataZip>() {
                    @Override
                    public void accept(DataZip dataZip) throws Exception {
                        if (dataZip.blockContacts.size() != 0) {
                            dataCallBacks.getUserFromDevice(dataZip.blockContacts.get(0));
                        } else if (dataZip.dataBeans.size() != 0) {
                            dataCallBacks.getUserFromServer(dataZip.dataBeans.get(0));
                        } else {
                            dataCallBacks.getUnknowUser();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        int a = 1;
                    }
                });

    }


    public void zipObservableTwo(String phoneNumber, GetDisplayNameCallBacks callBacks) {
        BlockContactDB dataBeanDB = BlockContactDB.getDatabase(context.get());
        DataBeanDAO dataBeanDAO = dataBeanDB.dataBeanDAO();
        BlockContactDB blockContactDB = BlockContactDB.getDatabase(context.get());
        BlockContactDAO blockContactDAO = blockContactDB.blockContactDAO();
        Disposable disposable = Observable.zip(dataBeanDAO.getContactServer(phoneNumber), blockContactDAO.getListContactLocal(phoneNumber), DataZip::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DataZip>() {
                    @Override
                    public void accept(DataZip dataZip) throws Exception {
                        if (dataZip.blockContacts.size() != 0) {
                            callBacks.getUserFromDevice(dataZip.blockContacts.get(0));
                        } else if (dataZip.dataBeans.size() != 0) {
                            callBacks.getUserFromServer(dataZip.dataBeans.get(0));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        int a = 1;
                    }
                });

    }

    static class DataZip {
        List<DataBean> dataBeans;
        List<BlockContact> blockContacts;

        public DataZip(List<DataBean> dataBeans, List<BlockContact> blockContacts) {
            this.dataBeans = dataBeans;
            this.blockContacts = blockContacts;
        }

        public List<DataBean> getDataBeans() {
            return dataBeans;
        }

        public List<BlockContact> getBlockContacts() {
            return blockContacts;
        }
    }


    public boolean isContactAccessible() {
        return Utils.arePermissionsGranted(context.get(), Constants.PERMISSION_GROUP_CONTACT);
    }

    public boolean getDeviceContactAsync() {
        if (!isContactAccessible()) {
            return false;
        }
        executor.execute(() -> {
            List<IContactObject> contacts = getAllDeviceContacts();
            EventBus.getDefault().postSticky(ContactEventFactory.createDeviceContactReadyEvent(contacts));
        });

        return true;
    }


    public List<IContactObject> getDeviceContacts(String filter) {
        List<IContactObject> contacts = new ArrayList<>();
        if (!isContactAccessible() || Utils.isEmpty(filter)) {
            return contacts;
        }

        String[] projectionFields = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        };

        String selection = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " like 1 ";
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC ";
        Cursor c = context.get().getContentResolver().query(Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(filter)), projectionFields, selection, null, sortOrder);
        IContactObject contact = null;
        int dIndex = -1;
        if (c != null) {
            while (c.moveToNext()) {
                int idIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                int nameIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String contactId = c.getString(idIndex);
                String contactDisplayName = c.getString(nameIndex);
                String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String lookupKey = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
                int type = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                String customLabel = "Custom";
                CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.get().getResources(), type, customLabel);
                if (contact == null || !contactId.equalsIgnoreCase(contact.getId())) {
                    contact = new ContactObject(contactId, contactDisplayName, lookupKey);
                    String a = Normalizer.normalize(((contactDisplayName != null && contactDisplayName.length() > 0 ? contactDisplayName.charAt(0) : "") + "").toUpperCase(), Normalizer.Form.NFD);
                    a = a.replaceAll("[^\\p{ASCII}]", "");
                    if (a.equalsIgnoreCase("") || a.equalsIgnoreCase("d")) {
                        if (dIndex == -1) {
                            dIndex = contacts.size();
                        }
                        contact.setGroup("D");
                    } else if (a.toUpperCase().compareTo("A") < 0) {
                        contact.setGroup("#");
                    } else {
                        contact.setGroup(a);
                    }
                    contacts.add(contact);
                }
                contact.getNumbers().add(new ContactPhoneObject(number, phoneType.toString()));
            }
            c.close();
        }

        return contacts;
    }

    public List<IContactObject> getAllDeviceContacts() {

        List<IContactObject> contacts = new ArrayList<>();

        if (!isContactAccessible()) {
            return contacts;
        }

        String[] projectionFields = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        };

        String selection = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " like 1 ";
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC ";

        Cursor c = context.get().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projectionFields, selection, null, sortOrder);

        IContactObject contact = null;
        int dIndex = -1;
        List<IContactObject> dGroup = new ArrayList<>();
        List<IContactObject> otherGroup = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                int idIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                int nameIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String contactId = c.getString(idIndex);
                String contactDisplayName = c.getString(nameIndex);
                String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String lookupKey = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
                int type = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                String customLabel = "Custom";
                CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.get().getResources(), type, customLabel);
                if (contact == null || !contactId.equalsIgnoreCase(contact.getId())) {
                    contact = new ContactObject(contactId, contactDisplayName, lookupKey);
                    String a = Normalizer.normalize(((contactDisplayName != null && contactDisplayName.length() > 0 ? contactDisplayName.charAt(0) : "") + "").toUpperCase(), Normalizer.Form.NFD);
                    a = a.replaceAll("[^\\p{ASCII}]", "");
                    if (a.equalsIgnoreCase("") || a.equalsIgnoreCase("d")) {
                        if (dIndex == -1) {
                            dIndex = contacts.size();
                        }
                        contact.setGroup("D");
                        dGroup.add(contact);
                    } else if (a.toUpperCase().compareTo("A") < 0) {
                        contact.setGroup("#");
                        otherGroup.add(contact);
                    } else {
                        contact.setGroup(a);
                        contacts.add(contact);
                    }
                }
                contact.getNumbers().add(new ContactPhoneObject(number, phoneType.toString()));
            }
            c.close();
        }
        Collections.sort(contacts, (o1, o2) -> o1.getGroup().compareTo(o2.getGroup()));
        contacts.addAll(otherGroup);
        return contacts;
    }


    private long getContactID(String number) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));

        String[] projection = {ContactsContract.PhoneLookup._ID};

        ContentResolver contactHelper = context.get().getContentResolver();
        try (Cursor cursor = contactHelper.query(contactUri, projection, null, null,
                null)) {

            assert cursor != null;
            if (cursor.moveToFirst()) {
                int personID = cursor.getColumnIndex(ContactsContract.PhoneLookup._ID);
                return cursor.getLong(personID);
            }

            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void deleteContact(String number) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ContentResolver contactHelper = context.get().getContentResolver();
        String[] args = new String[]{String.valueOf(getContactID(number))};

        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
        try {
            contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public void updateContact(String number, String newName) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ContentResolver contactHelper = context.get().getContentResolver();
        String[] args = new String[]{String.valueOf(getContactID(number))};

        ops.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args)
                .withValue(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, newName).build());
        try {
            contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }


    public static final String CONTACT_READY = "CONTACT_READY";
    public static final String CONTACT_ICALLER_CHANGED = "CONTACT_ICALLER_CHANGED";
    public static final String CONTACT_PERMISSION_CHANGED = "CONTACT_PERMISSION_CHANGED";
    public static final String CONTACT_BLOCKED_CHANGED = "CONTACT_BLOCKED_CHANGED";

    public static class ContactEventFactory {
        public static DeviceContactEvent createDeviceContactReadyEvent(List<IContactObject> contacts) {
            DeviceContactEvent event = new DeviceContactEvent(ContactManager.CONTACT_READY);
            event.setContacts(contacts);
            return event;
        }

        public static DeviceContactEvent createContactPermissionChangedEvent() {
            return new DeviceContactEvent(ContactManager.CONTACT_PERMISSION_CHANGED);
        }

        public static BlockedContactEvent createBlockedContactChangedEvent() {
            return new BlockedContactEvent(CONTACT_BLOCKED_CHANGED);
        }
    }

    public static class ContactEvent extends BaseEvent {
        public ContactEvent(String name) {
            super(name);
        }
    }

    public static class BlockedContactEvent extends ContactEvent {
        public BlockedContactEvent(String name) {
            super(name);
        }
    }

    public static class DeviceContactEvent extends ContactEvent {
        private List<IContactObject> contacts = new ArrayList<>();

        public List<IContactObject> getContacts() {
            return contacts;
        }

        public DeviceContactEvent(String name) {
            super(name);
        }

        public void setContacts(List<IContactObject> contacts) {
            if (contacts != null) {
                this.contacts.clear();
                this.contacts.addAll(contacts);
            }
        }
    }

    public static class IDCallerContactChangedEvent extends ContactEvent {
        public IDCallerContactChangedEvent(String name) {
            super(name);
        }
    }


}
