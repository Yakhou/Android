package yj.me.express.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import yj.me.express.R;
import yj.me.express.util.SnackbarUtils;
import yj.me.express.util.Utils;
import yj.me.express.util.permission.PermissionReq;
import yj.me.express.util.permission.PermissionResult;
import yj.me.express.util.permission.Permissions;

/**
 * Created by yangj on 2017/5/18.
 */

public class ImageAcivity extends Activity {
    private Bitmap abitmap;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_qr);
        Intent intent = getIntent();
        byte buff[]=intent.getByteArrayExtra("image");
        //将byte转为
        abitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
        BitmapDrawable mBitmapDrawable = new BitmapDrawable(abitmap);
        img.setBackgroundDrawable(mBitmapDrawable);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDialog(abitmap);
            }
        });

    }
    private void saveDialog(final Bitmap bitmap) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.tips)
                .setMessage(R.string.qrcode_save_tips)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                check(bitmap);
                            }
                        }
                ).
                setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void check(final Bitmap bitmap) {
        if (!Utils.hasSDCard()) {
            SnackbarUtils.show(this, R.string.qrcode_no_sdcard);
            return;
        }

        PermissionReq.with(this)
                .permissions(Permissions.STORAGE)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        save(bitmap);
                    }

                    @Override
                    public void onDenied() {
                        SnackbarUtils.show(ImageAcivity.this, getString(R.string.no_permission, Permissions.STORAGE_DESC, "保存二维码图片"));
                    }
                })
                .request();
    }

    private void save(Bitmap mBitmap) {
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
}
