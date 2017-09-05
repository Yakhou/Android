package yj.me.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import yj.me.express.Dao.DatabaseHelper;
import yj.me.express.Dao.User;
import yj.me.express.R;

/**
 * Created by yangj on 2017/4/9.
 */

public class RegisteActivity extends BaseActivity {
    private TextView ydnum;
    private EditText name,tel,address;
    private Button submit;
    private DatabaseHelper helper;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registe);
        ydnum = (TextView)findViewById(R.id.ydnum);
        name = (EditText) findViewById(R.id.name);
        tel = (EditText) findViewById(R.id.tel);
        address = (EditText) findViewById(R.id.address);
        submit = (Button)findViewById(R.id.submit);
        helper = new DatabaseHelper(this);
        Calendar calendar = Calendar.getInstance();
        String ydnumstr=String.valueOf(calendar.getTime().getTime());
        ydnum.setText(ydnumstr);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textName = name.getText().toString();
                final String textTel = tel.getText().toString();
                final String textAddress = address.getText().toString();
                User userinfo=new User(ydnum.getText().toString(),textName,textTel,textAddress);
                helper.addUser(userinfo);
                if (TextUtils.isEmpty(textName)||TextUtils.isEmpty(textTel)||TextUtils.isEmpty(textAddress)) {
                    Toast.makeText(RegisteActivity.this, "请输入必要信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    Toast.makeText(RegisteActivity.this, "信息录入成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisteActivity.this,ExpressActivity.class);
                    startActivity(intent);
                }

                //文本加密
               // String text = textName+" "+textTel+" "+textAddress;
               // DESUtil desUtil = new DESUtil();
               // String encrypt = desUtil.getEnc(text);
//              //生成二维码
               // final Bitmap bitmap = ZxingCode.Create2DCode(encrypt);
               // userinfo.setBitmap(bitmap);
                //提交后转到ExpressActivity页面，信息放入listView中
               // intent=new Intent(RegisteActivity.this,UserAdapter.class);
               // intent.putExtra("bitmap",userinfo.getBitmap());
            }

        });

    }

}
