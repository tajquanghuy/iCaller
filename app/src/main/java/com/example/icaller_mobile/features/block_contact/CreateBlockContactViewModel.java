package com.example.icaller_mobile.features.block_contact;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseViewModel;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.utils.Logger;
import com.example.icaller_mobile.common.widget.bottom_sheet.BottomSheetDescription;
import com.example.icaller_mobile.common.widget.dialog.ConfirmDialog;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepository;
import com.example.icaller_mobile.features.block_list.repository.BlockContactRepositoryImp;
import com.example.icaller_mobile.features.settings.settings_logout.DialogOnclickListener;
import com.example.icaller_mobile.model.encode.Crypto;
import com.example.icaller_mobile.model.encode.KeyManager;
import com.example.icaller_mobile.model.local.room.BlockContact;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.disposables.Disposable;

public class CreateBlockContactViewModel extends BaseViewModel {
    private BlockContactRepository blockContactRepository;
    public MutableLiveData<String> nameLiveData = new MutableLiveData<>();
    public MutableLiveData<String> phoneLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> blockSuccess = new MutableLiveData<>();
    public MutableLiveData<BottomSheetDescription.Description> typeDescription = new MutableLiveData<>();
    private Context mContext;

    public CreateBlockContactViewModel(Context context) {
        super(context);
        this.mContext = context;
        blockContactRepository = new BlockContactRepositoryImp(context);
    }

    public void setTypeOfDescription(BottomSheetDescription.Description typeOfDescription) {
        typeDescription.setValue(typeOfDescription);
    }


    public void saveBlockContact() {
        String key = "12345678909876543212345678909876";
        String iv = "1234567890987654";
        KeyManager km = new KeyManager(mContext);
        km.setIv(iv.getBytes());
        km.setId(key.getBytes());
        String phoneNumber = phoneLiveData.getValue().replace(" ", "").trim();
        if (nameLiveData.getValue() == null || phoneNumber == null) {
            ConfirmDialog confirmDialog = new ConfirmDialog(mContext, mContext.getString(R.string.error),
                    mContext.getString(R.string.key_missing_phone_name),
                    mContext.getString(R.string.key_ok),
                    new DialogOnclickListener() {
                        @Override
                        public void onButtonConfirmClick() {

                        }

                        @Override
                        public void onImageCloseClick() {
                        }
                    });
            confirmDialog.show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mContext == null) return;
                    ((Activity) mContext).runOnUiThread(() -> {
                        if (confirmDialog.isShowing()) {
                            confirmDialog.dismiss();
                        }
                    });
                }
            }, Constants.TIME_AUTO_CLOSE_DIALOG);
        } else {
            if (typeDescription.getValue() == null) {
                BlockContact contact = new BlockContact(nameLiveData.getValue(), phoneNumber, 7);

                String encrypted_Data = "data";
                String data = contact.getName();
                try {
                    Crypto crypto = new Crypto(mContext);
                    encrypted_Data = crypto.armorEncrypt(data.getBytes());
                } catch (InvalidKeyException e) {
                    Logger.log("SE3", "Exception in StoreData: " + e.getMessage());
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (NoSuchAlgorithmException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (NoSuchPaddingException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (IllegalBlockSizeException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (BadPaddingException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (InvalidAlgorithmParameterException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                }
                contact.setName(encrypted_Data);

                ConfirmDialog confirmDialog = new ConfirmDialog(mContext, mContext.getString(R.string.dialog_block),
                        mContext.getString(R.string.key_block_success),
                        mContext.getString(R.string.key_ok),
                        new DialogOnclickListener() {
                            @Override
                            public void onButtonConfirmClick() {
                                Disposable disposable = blockContactRepository.insert(contact)
                                        .subscribe(aLong -> blockSuccess.setValue(true)
                                                , throwable -> {
                                                    blockSuccess.setValue(false);
                                                });
                            }

                            @Override
                            public void onImageCloseClick() {
                            }
                        });
                confirmDialog.show();
            } else {
                BlockContact contact = new BlockContact(nameLiveData.getValue(), phoneNumber, typeDescription.getValue().getType());
                String encrypted_Data = "data";
                String data = contact.getName();
                try {
                    Crypto crypto = new Crypto(mContext);
                    encrypted_Data = crypto.armorEncrypt(data.getBytes());
                } catch (InvalidKeyException e) {
                    Logger.log("SE3", "Exception in StoreData: " + e.getMessage());
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (NoSuchAlgorithmException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (NoSuchPaddingException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (IllegalBlockSizeException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (BadPaddingException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                } catch (InvalidAlgorithmParameterException e) {
                    Log.e("SE3", "Exception in StoreData: " + e.getMessage());
                }
                contact.setName(encrypted_Data);
                ConfirmDialog confirmDialog = new ConfirmDialog(mContext, mContext.getString(R.string.dialog_block),
                        mContext.getString(R.string.key_block_success),
                        mContext.getString(R.string.key_ok),
                        new DialogOnclickListener() {
                            @Override
                            public void onButtonConfirmClick() {
                                Disposable disposable = blockContactRepository.insert(contact)
                                        .subscribe(aLong -> blockSuccess.setValue(true)
                                                , throwable -> {
                                                    blockSuccess.setValue(false);
                                                });
                            }

                            @Override
                            public void onImageCloseClick() {
                            }
                        });
                confirmDialog.show();
            }
        }
    }
}