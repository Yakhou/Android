package yj.me.express.util;

import android.content.Context;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* Author: Hsuan(6511631@qq.com)
*
* Date: 2015-03-10 20:09
*
* Description:对IO进行流操作的工具类，提供方法拷贝assets下的文件到data/data/(包名)/files/程序目录下
*
*/
public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
			}
		}
		return true;
	}

    /** 把assets目录下数据库拷贝到data/data/(包名)/files/程序目录下*/
    public  void copyDB(String fileName, Context context) {
        // 开启线程池来启动线程，即使半路上崩溃终止了，线程启动后也会继续复制
        ExecutorService newThreadPool = Executors.newFixedThreadPool(1);
        newThreadPool.execute(new TaskRunnable(fileName,context));
    }

    private class TaskRunnable implements Runnable {
        private String fileName;
        private Context context;
        public TaskRunnable(String fileName, Context context) {
            this.fileName=fileName;
            this.context=context;
        }

        @Override
        public void run() {
            // 只需要拷贝一次，以后加载时不需要再次拷贝
            try {
                File file = new File(context.getFilesDir(), fileName);
                if (file.exists() && file.length() > 1) {
                    // 已经有数据，不需要再次拷贝
                    Log.i("lx", "已经有数据库，不再拷贝");
                } else {
                    // 工具类中，获得的方式为：context.getResources().getAssets().open(fileName);
                    InputStream is = context.getAssets().open(fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    is.close();
                    Log.i("lx", "拷贝完毕");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("lx", "拷贝失败"+e);
            }
        }
    }
}
