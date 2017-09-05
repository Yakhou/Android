package yj.me.express.zxing.encoding;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * @author Ryan Tang
 */
public final class EncodingHandler {

    /**
     * 根据字符串生成二维码图片
     *
     * @param str    要生成的字符串
     * @param length 生成的图片边长
     * @return 生成的图片
     * @throws WriterException
     */
    public static Bitmap createQRCode(String str, int length) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //参数依次代表要生成二维码的数据，编码格式，二维码宽度，高度，设置参数
        BitMatrix matrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, length, length, hints);
        //生成bitmap图片，设有黑白两色
        int[] pixels = new int[length * length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                if (matrix.get(x, y)) {//返回值为true，设为黑色。x为列，y为行
                    pixels[y * length + x] = Color.BLACK;
                } else {
                    pixels[y * length + x] = Color.WHITE;
                }
            }
        }
        // 创建一张bitmap图片，采用最高的效果显示
        Bitmap bitmap = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        // 将上面的二维码颜色数组传入，生成图片颜色
        bitmap.setPixels(pixels, 0, length, 0, 0, length, length);
        return bitmap;
    }
}
