package yj.me.express.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import yj.me.express.Dao.User;
import yj.me.express.R;
import yj.me.express.util.DESUtil;
import yj.me.express.util.binding.Bind;
import yj.me.express.zxing.ZxingCode;

import static yj.me.express.R.id.bitmap;

/**
 * Created by yangj on 2017/4/10.
 */

public class UserAdapter extends BaseAdapter {
    private List<User> users;
    private Context context;
    @Bind(bitmap)
    private Bitmap image;
    private ImageView infoimg;
    private LayoutInflater mInflater;
    byte buff[] = new byte[125*250];

    public UserAdapter(Context context,List<User> users,Bitmap image) {
        super();
        this. users= users;
        this.context=context;
        this.image=image;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return  users.size();
    }

    @Override
    public Object getItem(int i) {
        return  users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        }
        infoimg= (ImageView) view.findViewById(R.id.bitmap);
        TextView tvydnum= (TextView) view.findViewById(R.id.ydnumInMain);
        TextView tvydhao= (TextView) view.findViewById(R.id.ydhao);

        tvydnum.setText("运单号：");
        tvydhao.setText(users.get(i).getYdnum());
       String info= users.get(i).getName().toString()+" "+users.get(i).getTel().toString()+" "+users.get(i).getAddress().toString();
        DESUtil desUtil = new DESUtil();
        String encrypt = desUtil.getEnc(info);
        //加密信息生成二维码
        final Bitmap imageInfo = ZxingCode.Create2DCode(encrypt);
        infoimg.setImageBitmap(imageInfo);
       // buff = Bitmap2Bytes(imageInfo);
        //infoimg.setOnClickListener(new View.OnClickListener() {
           // @Override
          //  public void onClick(View v) {
            /*    Intent intent=new Intent();
                intent.putExtra("image",buff);
                intent.setClass(context, ImageAcivity.class);
                context.startActivity(intent);
            }*/
        //});


        return view;
    }
    //将bitmap转为byte
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }






}
