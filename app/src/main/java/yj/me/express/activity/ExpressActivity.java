package yj.me.express.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import yj.me.express.Dao.DatabaseHelper;
import yj.me.express.Dao.User;
import yj.me.express.R;
import yj.me.express.adapter.UserAdapter;
import yj.me.express.util.DESUtil;
import yj.me.express.util.SnackbarUtils;
import yj.me.express.util.Utils;
import yj.me.express.util.binding.Bind;
import yj.me.express.util.binding.ViewBinder;
import yj.me.express.util.permission.PermissionReq;
import yj.me.express.util.permission.PermissionResult;
import yj.me.express.util.permission.Permissions;
import yj.me.express.zxing.ZxingCode;
import yj.me.express.zxing.activity.CaptureActivity;


public class ExpressActivity extends PermissionActivity implements AdapterView.OnItemLongClickListener,OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    @Bind(R.id.tv_search)
    private TextView tvSearch;
    @Bind(R.id.tv_post)
    private TextView tvPost;
    @Bind(R.id.tv_sweep)
    private TextView tvSweep;
    @Bind(R.id.lv_un_check)
    private ListView userinfo;
    @Bind(R.id.tv_empty)
    private TextView tvEmpty;
    private long mExitTime = 0;
    private UserAdapter adapter;
    private DatabaseHelper helper;
    private List<User> userList;
   // private Bitmap mBitmap;


    private DESUtil desUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        ViewBinder.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        userinfo = (ListView) findViewById(R.id.lv_un_check);

        //创建数据库及表
           helper = new DatabaseHelper(this);
            userList = helper.getALllUser();
           String name = userList.get(1).toString();
            String tel = userList.get(2).toString();
            String address = userList.get(3).toString();
            String info = name + " " + tel + " " + address;
            //将信息加密
            desUtil = new DESUtil();
            String encrypt = desUtil.getEnc(info);
            //加密信息生成二维码
            final Bitmap bitmap = ZxingCode.Create2DCode(encrypt);
            adapter = new UserAdapter(this, userList,bitmap);//把所有从List中取出的数据放到adapter
            userinfo.setAdapter(adapter);
           //将数据加载到userinfo列表上


    }


    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setNavigationItemSelectedListener(this);
        tvSearch.setOnClickListener(this);
        tvPost.setOnClickListener(this);
        tvSweep.setOnClickListener(this);
        userinfo.setOnItemLongClickListener(this);
        //userinfo.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
      /* List<User> unCheckList = helper.getUnCheckList();
        userList.clear();
        userList.addAll(unCheckList);
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(userList.isEmpty() ? View.VISIBLE : View.GONE);*/
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                SnackbarUtils.show(this, "敬请期待");
                break;
            case R.id.tv_post:
                Intent i = new Intent(ExpressActivity.this, RegisteActivity.class);
                i.putExtra("request", "Add");
                startActivityForResult(i, 1);
                break;
            case R.id.tv_sweep:
                startCaptureActivity();
                break;
            default:
                break;
           /*
                    View view1 = LayoutInflater.from(ExpressActivity.this).inflate(R.layout.dialog_capture, null);
                    final TextView tvResult = (TextView) view1.findViewById(R.id.tv_result);
                    tvResult.setText(desUtil.getDec(result.getText()));
                    tvResult.setAutoLinkMask(Linkify.ALL);
                    tvResult.setMovementMethod(LinkMovementMethod.getInstance());
                    new android.support.v7.app.AlertDialog.Builder(ExpressActivity.this)
                            .setTitle("扫描结果")
                            .setView(view1)
                            .setPositiveButton("复制文本", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                    cmb.setText(tvResult.getText());
                                    SnackbarUtils.show(ExpressActivity.this, "复制成功");
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                }
            default:
                break;
        }*/
        }
    }

    private void startCaptureActivity() {
        PermissionReq.with(this)
                .permissions(Permissions.CAMERA)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        CaptureActivity.start(ExpressActivity.this, false, 0);
                    }

                    @Override
                    public void onDenied() {
                        SnackbarUtils.show(ExpressActivity.this, getString(R.string.no_permission, Permissions.CAMERA_DESC, "打开扫一扫"));
                    }
                })
                .request();
    }

       /* @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            View view1 = LayoutInflater.from(ExpressActivity.this).inflate(R.layout.dialog_capture, null);
            final TextView tvResult = (TextView) view1.findViewById(R.id.tv_result);
            userList = helper.getALllUser();
            String name = userList.get(i).getName();
            String tel = userList.get(i).getTel();
            String address = userList.get(i).getAddress();
            String info = name + " " + tel + " " + address;
            tvResult.setText(info);
            tvResult.setAutoLinkMask(Linkify.ALL);
            tvResult.setMovementMethod(LinkMovementMethod.getInstance());
            new android.support.v7.app.AlertDialog.Builder(ExpressActivity.this)
                    .setTitle("扫描结果")
                    .setView(view1)
                    .setPositiveButton("复制文本", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            cmb.setText(tvResult.getText());
                            SnackbarUtils.show(ExpressActivity.this, "复制成功");
                        }
        });
        }*/



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
                        SnackbarUtils.show(ExpressActivity.this, getString(R.string.no_permission, Permissions.STORAGE_DESC, "保存二维码图片"));
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





       

    @Override
    public boolean onItemLongClick(AdapterView<?> view, View arg1, final int position, long arg3) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tips))
                .setMessage(getResources().getString(
                        R.string.sure_delete_history))
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       helper.deleteUser(userList.get(position).getYdnum());
                        userList = helper.getALllUser();
                        String name = userList.get(1).toString();
                        String tel = userList.get(2).toString();
                        String address = userList.get(3).toString();
                        String info = name + " " + tel + " " + address;
                        //将信息加密
                        DESUtil desUtil = new DESUtil();
                        String encrypt = desUtil.getEnc(info);
                        //加密信息生成二维码
                        final Bitmap bitmap = ZxingCode.Create2DCode(encrypt);
                        adapter = new UserAdapter(ExpressActivity.this, userList,bitmap);//把所有从List中取出的数据放到adapter
                        userinfo.setAdapter(adapter);

                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
        return true;
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        drawerLayout.closeDrawers();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                item.setChecked(false);
            }
        }, 500);
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_qrcode:
                intent.setClass(this, QRCodeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_about:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

   /* @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        if (System.currentTimeMillis() - mExitTime > 2000) {
            mExitTime = System.currentTimeMillis();
            SnackbarUtils.show(this, R.string.click2exit);
        } else {
            finish();
        }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //根据返回的resultCode判断是通过哪种操作返回的，并提示相关信息；
        switch (requestCode){
            case 0:
                if (resultCode==2)
                    Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                if (resultCode==3)
                    Toast.makeText(this,"已删除",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (resultCode==RESULT_OK)
                    Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                break;
        }
        /**
         * 如果这里仅仅使用adapter.notifyDataSetChanged()是不会刷新界面ListView的，
         * 因为此时adapter中传入的userList并没有给刷新，即adapter也没有被刷新，所以你可以
         * 重新获取userList后再改变adapter，我这里通过调用onCreate()重新刷新了整个界面
         */
        helper = new DatabaseHelper(this);
        userList = helper.getALllUser();
        String name = userList.get(1).toString();
        String tel = userList.get(2).toString();
        String address = userList.get(3).toString();
        String info = name + " " + tel + " " + address;
        //将信息加密
        desUtil = new DESUtil();
        String encrypt = desUtil.getEnc(info);
        //加密信息生成二维码
        final Bitmap bitmap = ZxingCode.Create2DCode(encrypt);
        adapter = new UserAdapter(this, userList,bitmap);//把所有从List中取出的数据放到adapter
        userinfo.setAdapter(adapter);
        //将数据加载到userinfo列表上

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
