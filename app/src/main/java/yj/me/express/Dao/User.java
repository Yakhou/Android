package yj.me.express.Dao;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by yangj on 2017/4/9.
 */

public class User implements Serializable {
    private String ydnum;
    private String name;
    private String tel;
    private String address;
    private Bitmap bitmap;
    public User(){}
    public User(String ydnum){
        this.ydnum=ydnum;
    }
    public User(String ydnum,String name,String tel,String address){
        this.ydnum=ydnum;
        this.name=name;
        this.tel=tel;
        this.address=address;
    }
    public User(String ydnum,String name,String tel,String address,Bitmap bitmap){
        this.ydnum=ydnum;
        this.name=name;
        this.tel=tel;
        this.address=address;
        this.bitmap=bitmap;
    }
    public User(Bitmap bitmap){
        this.bitmap=bitmap;
    }
    public void setBitmap(Bitmap bitmap){this.bitmap=bitmap;}

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setYdnum(String ydnum) {
        this.ydnum = ydnum;
    }

    public Bitmap getBitmap(){return bitmap;}

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getYdnum() {
        return ydnum;
    }
}
