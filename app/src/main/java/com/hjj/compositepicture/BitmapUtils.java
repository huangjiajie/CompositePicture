package com.hjj.compositepicture;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BitmapUtils {

    private final static String Tag = BitmapUtils.class.getSimpleName();


    /**
     * 组合涂鸦图片和源图片
     *
     * @param src       源图片
     * @param watermark 涂鸦图片
     * @return
     */
    public static Bitmap setBitmapImage(Context context, Bitmap src, Bitmap watermark, String userName) {


        Bitmap circleBitmap = createCircleBitmap(watermark);
        Bitmap whitheCircleBitmap = createWhitheCircleBitmap(watermark);

        // 另外创建一张图片
        Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas canvas = new Canvas(newb);

        canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src
        int width = ((src.getWidth() / 2) - (circleBitmap.getWidth() / 2));
        int height = ((src.getHeight() / 2) - (circleBitmap.getWidth() / 2));

        canvas.drawBitmap(whitheCircleBitmap, width, height - 100, null); // 白色圆形中间位置
        canvas.drawBitmap(circleBitmap, width, height - 100, null); // 涂鸦图片画到原图片中间位置


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setTextSize(dip2px(context, 12));

       //判断是否含有数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(userName);
        String strNum = m.replaceAll("").trim();

        //当有数字时，会影响居中显示，原因是数字长度比文本小
        int textPx=0;
        if (TextUtils.isEmpty(strNum)) {
             textPx = dip2px(context, 12) * userName.length();
        } else {
            textPx = dip2px(context, 12) * userName.length()-(dip2px(context, 6)*(strNum.length()));
        }

        int textWidth = (src.getWidth() / 2) - ((textPx / 2));

        canvas.drawText(userName, textWidth, height + 230, paint);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        circleBitmap.recycle();
        circleBitmap = null;

        return newb;
    }


    private static Bitmap createCircleBitmap(Bitmap resource) {
        //获取图片的宽度
        int width = resource.getWidth();
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);

        //创建一个与原bitmap一样宽度的正方形bitmap
        Bitmap circleBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //以该bitmap为低创建一块画布
        Canvas canvas = new Canvas(circleBitmap);


        //以（width/2, width/2）为圆心，width/2为半径画一个圆
        canvas.drawCircle(width / 2, width / 2, (width / 2 - 40), paint);

        //设置画笔为取交集模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //裁剪图片
        canvas.drawBitmap(resource, 0, 0, paint);

        canvas.drawARGB(0, 0, 0, 0);

        return circleBitmap;
    }


    private static Bitmap createWhitheCircleBitmap(Bitmap resource) {
        //获取图片的宽度
        int width = resource.getWidth();
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);

        //创建一个与原bitmap一样宽度的正方形bitmap
        Bitmap circleBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //以该bitmap为低创建一块画布
        Canvas canvas = new Canvas(circleBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        paint.setColor(Color.WHITE);
        //设置画笔为取交集模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        //以（width/2, width/2）为圆心，width/2为半径画一个圆
        canvas.drawCircle(width / 2, width / 2, ((width / 2) - 30), paint);

        return circleBitmap;
    }


    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return (int) (dipValue * 1 + 0.5f);
        }
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 把batmap 转file
     *
     * @param bitmap
     */
    public static File saveBitmapFile(Bitmap bitmap) {

        String filepath = "/sdcard/zhiershareappimage.jpg";
        File file = new File(filepath);//将要保存图片的路径

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }



}

