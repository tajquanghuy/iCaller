package com.example.icaller_mobile.features.ocr_machine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import com.example.icaller_mobile.R;
import com.example.icaller_mobile.base.BaseFragment;
import com.example.icaller_mobile.base.ViewModelProviderFactory;
import com.example.icaller_mobile.common.constants.Constants;
import com.example.icaller_mobile.databinding.FragmentTextRecognitionBinding;
import com.example.icaller_mobile.features.main.MainViewModel;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class TextRecognitionFragment extends BaseFragment<FragmentTextRecognitionBinding, TextRecognitionViewModel> implements SurfaceHolder.Callback, Detector.Processor {
    private CameraSource cameraSource;
    private String numbers;
    private StringBuilder strBuilder1;
    private Boolean check = false;
    private TextRecognitionViewModel mViewModel;
    private MainViewModel mainViewModel;
    private Context mContext;

    public static TextRecognitionFragment newInstance() {
        return new TextRecognitionFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_ocr_search);
    }

    @Override
    public Constants.ToolbarStyle getToolbarStyle() {
        return Constants.ToolbarStyle.OnlyText;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_text_recognition;
    }

    @Override
    public TextRecognitionViewModel getViewModel() {
        mainViewModel = new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(MainViewModel.class);
        return new ViewModelProvider(this, new ViewModelProviderFactory(requireContext())).get(TextRecognitionViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeSurfaceView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        cameraSource.start(binding.sureFaceView.getHolder());
                    } catch (Exception e) {

                    }
                }
            }
            break;
        }
    }

    private void initializeSurfaceView() {
        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtScan.setText("");
            }
        });
        binding.btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallToMetallurgGates();
            }
        });
//        binding.btnRegex.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!check)
//                    check = true;
//                else
//                    check = false;
//            }
//        });
        binding.btnRegex.setChecked(true);
        binding.btnRegex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = false;
                } else {
                    check = true;
                }
            }
        });

        TextRecognizer txtRecognizer = new TextRecognizer.Builder(getContext()).build();
        if (!txtRecognizer.isOperational()) {
            Log.e("Main Activity", "Detector dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getContext(), txtRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            binding.sureFaceView.getHolder().addCallback(this);
            txtRecognizer.setProcessor(this);
        }
    }

    public void CallToMetallurgGates() {
        try {
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                numbers = binding.edtScan.getText().toString();
                numbers = numbers.replace("-", "");
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + numbers));
                startActivity(dialIntent);
            } else {
                numbers = binding.edtScan.getText().toString();
                numbers = numbers.replace("-", "");
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + numbers));
                startActivity(dialIntent);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                return;
            }
            cameraSource.start(binding.sureFaceView.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(@NonNull Detector.Detections detections) {
        SparseArray items = detections.getDetectedItems();
        final StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            TextBlock item = (TextBlock) items.valueAt(i);

            strBuilder.append(item.getValue());
            strBuilder.append("/");

        }

        //Regex Operation
        String str = strBuilder.toString();
        String str2 = str.replace("/", "\n");
        String str1[] = str2.split("\n");
        Log.d("numbersss", str);
        strBuilder1 = new StringBuilder();
        for (int i = 0; i < str1.length; i++) {
            if (check) {
                //^((\+92)|(0092))-{0,1}\d{3}-{0,1}\d{7}$|^\d{11}$|^\d{4}-\d{7}$
                if (str1[i].replace("-", "").matches("^(\\+\\d{1,9}[- ]?)?\\d{10}$")) {
                    strBuilder1.append(str1[i]);
                    strBuilder1.append("/");
                    Log.d("adad", str1[i]);
                }
            } else {
                strBuilder1.append(str1[i]);
                strBuilder1.append("/");
                Log.d("adad", str1[i]);
            }

        }
        Log.v("strBuilder", strBuilder.toString());
        //Log.d("beeer", strBuilder.toString());

        binding.txtView.post(new Runnable() {
            @Override
            public void run() {
                binding.txtView.setText(strBuilder1.toString());
                binding.txtView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (binding.edtScan.getText().toString().contains("")) {

                        }
                        binding.sureFaceView.setVisibility(View.GONE);
                        binding.txtView.setVisibility(View.GONE);
                        binding.btnRegex.setVisibility(View.GONE);
                        binding.edtScan.setVisibility(View.VISIBLE);
                        binding.btnClear.setVisibility(View.VISIBLE);
                        binding.btnCopy.setVisibility(View.VISIBLE);
                        binding.scrollView.setVisibility(View.VISIBLE);
                        binding.btnDial.setVisibility(View.GONE);

                        String all_number = binding.txtView.getText().toString();
                        all_number = all_number.replace("/", "\n");
                        String all[] = all_number.split("\n");

                      /*  for(int i=0 ; i<all.length;i++)
                        {
                            if(all[i].length()<11 || all[i].length()>16 )
                                all[i]="";

                        }
                        String n = Arrays.toString(all);

                        n = n.replace("0092", "0");
                        n = n.replace("+92", "0");
                        n = n.replace(",", ";");
                        n = n.replace("; ;", ";");
                        n = n.replace("[", "");
                        n = n.replace("]", "");
                        n = n.replace("\n", "");
                        n = n.replace(" ", "");
                        n = n.replace(";;", ";");
*/
                        binding.edtScan.setText(all_number);

                    }


                });
//                binding.btnCopy.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        //send message
//                        /*numbers = txt1.getText().toString();
//
//                        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + numbers));
//                        sendIntent.putExtra("address", numbers);
//                        sendIntent.putExtra("sms_body", "");
//                        startActivity(sendIntent);*/
//
//                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText("Copied", binding.edtScan.getText().toString());
//                        clipboard.setPrimaryClip(clip);
//
//                    }
//                });

            }
        });

    }

}