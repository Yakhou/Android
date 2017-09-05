package yj.me.express.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import yj.me.express.R;
import yj.me.express.util.DESUtil;
import yj.me.express.util.SnackbarUtils;
import yj.me.express.util.Utils;
import yj.me.express.util.binding.Bind;
import yj.me.express.util.permission.PermissionReq;
import yj.me.express.util.permission.PermissionResult;
import yj.me.express.util.permission.Permissions;
import yj.me.express.zxing.encoding.EncodingHandler;


public class QRCodeActivity extends BaseActivity implements OnClickListener, TextWatcher {
    @Bind(R.id.et_text)
    private EditText etText;
    @Bind(R.id.btn_create)
    private Button btnCreate;
    @Bind(R.id.iv_qr_code)
    private ImageView ivQRCode;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        ivQRCode.setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        etText.addTextChangedListener(this);
        btnCreate.setOnClickListener(this);
        ivQRCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                createQRCode();
                break;
            case R.id.iv_qr_code:
                saveDialog();
                break;
        }
    }

    private void createQRCode() {
        try {
            DESUtil des=new DESUtil();
            String contentString = des.getEnc(etText.getText().toString());
            mBitmap = EncodingHandler.createQRCode(contentString, 500);
            ivQRCode.setImageBitmap(mBitmap);
            ivQRCode.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void saveDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tips)
                .setMessage(R.string.qrcode_save_tips)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                check();
                            }
                        }
                ).
                setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void check() {
        if (!Utils.hasSDCard()) {
            SnackbarUtils.show(this, R.string.qrcode_no_sdcard);
            return;
        }

        PermissionReq.with(this)
                .permissions(Permissions.STORAGE)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        save();
                    }

                    @Override
                    public void onDenied() {
                        SnackbarUtils.show(QRCodeActivity.this, getString(R.string.no_permission, Permissions.STORAGE_DESC, "保存二维码图片"));
                    }
                })
                .request();
    }

    private void save() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String fileName = getString(R.string.qrcode_file_name, sdf.format(new Date(System.currentTimeMillis())));
        File file = new File(Utils.getPictureDir() + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            SnackbarUtils.show(this, R.string.qrcode_save_failure);
            return;
        }
        // 刷新相册
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);

        SnackbarUtils.show(this, getString(R.string.qrcode_save_success, fileName));
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etText.length() > 0) {
            btnCreate.setEnabled(true);
        } else {
            btnCreate.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
