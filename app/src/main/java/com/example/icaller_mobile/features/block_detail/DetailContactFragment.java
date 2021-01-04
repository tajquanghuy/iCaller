package com.example.icaller_mobile.features.block_detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.common.constants.IntentConstants;
import com.example.icaller_mobile.common.event.EventBusAction;
import com.example.icaller_mobile.common.event.MessageEvent;
import com.example.icaller_mobile.common.utils.Utils;
import com.example.icaller_mobile.databinding.FragmentDetailContactBinding;
import com.example.icaller_mobile.features.main.MainViewModel;
import com.example.icaller_mobile.model.local.room.BlockContact;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.icaller_mobile.common.manager.DialogManager.setSizeDialog;

public class DetailContactFragment extends BaseFragment<FragmentDetailContactBinding, DetailContactViewModel> {
    private MainViewModel mainViewModel;
    private Context mContext;

    public static DetailContactFragment newInstance() {
        DetailContactFragment fragment = new DetailContactFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.profile);
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_detail_contact;
    }

    @Override
    public DetailContactViewModel getViewModel() {
        mainViewModel = new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(MainViewModel.class);
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(DetailContactViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        initViews();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    private void initViews() {
        BlockContact contact = getArguments().getParcelable(IntentConstants.KEY_BLOCK_LIST_FRAGMENT);
        binding.textName.setText(contact.getName());
        binding.textPhoneNumberAbove.setText(contact.getPhoneNumber());
        binding.textPhoneNumber.setText(contact.getPhoneNumber());
        Constants.ReportType reportType = getTypeBlock(contact.getTypeReport());
        binding.viewRed.setText(reportType.getName());
        binding.vCall.setOnClickListener(v -> {
            Constants.PHONENUMBER = Utils.fix84(contact.getPhoneNumber());
            Utils.callPhone2(mContext, Constants.PHONENUMBER);
        });
        binding.vMessage.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_SENDTO);
            intent1.setData(Uri.parse("smsto:" + Uri.encode(contact.getPhoneNumber())));
            startActivity(intent1);
        });
        binding.vSave.setOnClickListener(v -> {
            Intent intent1 = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent1.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent1.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhoneNumber());
            intent1.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName().equals(contact.getPhoneNumber()) ? "" : contact.getName());
            startActivity(intent1);
            pop();
        });
        binding.vUnBlock.setOnClickListener(v -> {
            if (contact != null) {
                //DialogManager.showDialogUnblockSuccess(mContext);
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog_comfirm);
                if (dialog.getWindow() != null) {
                    setSizeDialog(mContext, dialog.getWindow());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                    AppCompatButton btnOK = dialog.findViewById(R.id.btn_ok);
                    AppCompatTextView textMessage = dialog.findViewById(R.id.text_message_confirm);
                    textMessage.setText(mContext.getText(R.string.key_number_unblocked).toString());
                    btnOK.setOnClickListener(view -> {
                        dialog.dismiss();
                        EventBus.getDefault().postSticky(new MessageEvent(EventBusAction.DATA_BLOCK_CHANGE, null, null));
                    });
                    dialog.show();

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ((Activity) mContext).runOnUiThread(() -> {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                    EventBus.getDefault().postSticky(new MessageEvent(EventBusAction.DATA_BLOCK_CHANGE, null, null));
                                }
                            });
                        }
                    }, Constants.TIME_AUTO_CLOSE_DIALOG);
                }
                mViewModel.unBlockContact(contact);
                mActivity.pop();
            }
        });
    }



    public Constants.ReportType getTypeBlock(int type) {
        if (type == Constants.ReportType.REPORT_ADVERTISING.getType()) {
            return Constants.ReportType.REPORT_ADVERTISING;
        } else if (type == Constants.ReportType.REPORT_FINANCIAL_SERVICE.getType()) {
            return Constants.ReportType.REPORT_FINANCIAL_SERVICE;
        } else if (type == Constants.ReportType.REPORT_LOAN_COLLECTION.getType()) {
            return Constants.ReportType.REPORT_LOAN_COLLECTION;
        } else if (type == Constants.ReportType.REPORT_SCAM.getType()) {
            return Constants.ReportType.REPORT_SCAM;
        } else if (type == Constants.ReportType.REPORT_REAL_ESTATE.getType()) {
            return Constants.ReportType.REPORT_REAL_ESTATE;
        } else if (type == Constants.ReportType.REPORT_OTHER.getType()) {
            return Constants.ReportType.REPORT_OTHER;
        } else {
            return Constants.ReportType.NORMAL;
        }
    }
}