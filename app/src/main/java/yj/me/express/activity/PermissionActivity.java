package yj.me.express.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import yj.me.express.util.permission.PermissionReq;



public class PermissionActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
