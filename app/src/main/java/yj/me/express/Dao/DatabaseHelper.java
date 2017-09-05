package yj.me.express.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangj on 2017/3/23.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="bishe";
    private static final String TABLE_NAME="User";
    private static final int VERSION=1;
    private static final String KEY_ID="ydnum";
    private static final String KEY_NAME="name";
    private static final String KEY_TEL="tel";
    private static final String KEY_ADDRESS="address";
    private static final String KEY_BITMAP="bitmap";
    private Dao<User, String> mHistoryDao;



    //建表语句
    private static final String CREATE_TABLE="create table "+TABLE_NAME+"("+KEY_ID+
            " integer primary key,"+KEY_NAME+" text not null,"+
            KEY_TEL+" text not null,"+KEY_ADDRESS+" text not null);";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addUser(User user){
        SQLiteDatabase db=this.getWritableDatabase();

        //使用ContentValues添加数据
        ContentValues values=new ContentValues();
        values.put(KEY_ID,user.getYdnum());
        values.put(KEY_NAME,user.getName());
        values.put(KEY_TEL,user.getTel());
        values.put(KEY_ADDRESS,user.getAddress());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public User getUser(String name){
        SQLiteDatabase db=this.getWritableDatabase();

        //Cursor对象返回查询结果
        Cursor cursor=db.query(TABLE_NAME,new String[]{KEY_ID,KEY_NAME,KEY_TEL,KEY_ADDRESS},
                KEY_NAME+"=?",new String[]{name},null,null,null,null);


       User user=null;
        //注意返回结果有可能为空
        if(cursor.moveToFirst()){
            user=new User(cursor.getString(0),cursor.getString(1), cursor.getString(2),cursor.getString(3));
        }
        return user;
    }
    public int getUserCounts(){
        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        cursor.close();

        return cursor.getCount();
    }

    //查找所有student
    public List<User> getALllUser(){
        List<User> userList=new ArrayList<User>();

        String selectQuery="SELECT * FROM "+TABLE_NAME;

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                User user=new User();
                user.setYdnum(cursor.getString(0));
                user.setName(cursor.getString(1));
                user.setTel(cursor.getString(2));
                user.setAddress(cursor.getString(3));
                //该运单号其他用户信息
                //根据用户信息生成二维码
               // String otherInfo = "select "+KEY_NAME+","+KEY_TEL+","+KEY_ADDRESS+" from "+TABLE_NAME+" where "+KEY_ID+"="+user.getYdnum();

                userList.add(user);
            }while(cursor.moveToNext());
        }
        return userList;
    }


    public  void deleteUser(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }
    public List<User> getUnCheckList() {
        List<User> unCheckList = new ArrayList<>();
        for (User info : mHistoryDao) {
            if (info.getYdnum()==null) {
                unCheckList.add(0, info);
            }
        }
        return unCheckList;
    }
}
