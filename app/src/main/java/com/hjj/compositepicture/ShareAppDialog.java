package com.hjj.compositepicture;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by hjj on 17/12/28.
 */

public class ShareAppDialog implements View.OnClickListener {

    private Context mContext;
    private View mDialogView;
    private Dialog mDialog;
    private LinearLayout lly_weixin_circle;
    private LinearLayout lly_weixin;
    private LinearLayout lly_weibo;
    private RelativeLayout rly_photo;
    private RelativeLayout rly_url;
    private ImageView iv_select_photo;
    private ImageView iv_photo_bg;
    private ImageView iv_select_url;
    private ImageView iv_icon;
    private TextView tv_url_des;
    private ImageView iv_close;
    private Bitmap iconBitmap;
    private String shareImageUrl;
    private View top_view;
    private File imageFile;


    public ShareAppDialog(Context context) {
        this.mContext = context;
        mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_app_share, null);

        iniDialog();
    }


    private void iniDialog() {

        mDialog = new Dialog(mContext, R.style.dialog);
        mDialog.setContentView(mDialogView);

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        params.width = width;
        mDialog.setCanceledOnTouchOutside(true);

        dialogWindow.setWindowAnimations(R.style.sharedialogWindowAnim); // 设置窗口弹出动画


        lly_weixin_circle = (LinearLayout) mDialogView.findViewById(R.id.lly_weixin_circle);
        lly_weixin = (LinearLayout) mDialogView.findViewById(R.id.lly_weixin);
        lly_weibo = (LinearLayout) mDialogView.findViewById(R.id.lly_weibo);
        rly_photo = (RelativeLayout) mDialogView.findViewById(R.id.rly_photo);
        rly_photo = (RelativeLayout) mDialogView.findViewById(R.id.rly_photo);
        rly_url = (RelativeLayout) mDialogView.findViewById(R.id.rly_url);
        iv_select_photo = (ImageView) mDialogView.findViewById(R.id.iv_select_photo);
        iv_photo_bg = (ImageView) mDialogView.findViewById(R.id.iv_photo_bg);
        iv_select_url = (ImageView) mDialogView.findViewById(R.id.iv_select_url);
        iv_icon = (ImageView) mDialogView.findViewById(R.id.iv_icon);
        tv_url_des = (TextView) mDialogView.findViewById(R.id.tv_url_des);
        iv_close = (ImageView) mDialogView.findViewById(R.id.iv_close);
        top_view = (View) mDialogView.findViewById(R.id.top_view);


        lly_weixin_circle.setOnClickListener(this);
        lly_weixin.setOnClickListener(this);
        lly_weibo.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_select_photo.setOnClickListener(this);
        iv_select_url.setOnClickListener(this);
        rly_photo.setOnClickListener(this);
        rly_url.setOnClickListener(this);
        iv_photo_bg.setOnClickListener(this);
        top_view.setOnClickListener(this);


        iv_select_photo.setSelected(true);
        iv_select_url.setSelected(false);
        rly_url.setBackgroundColor(Color.WHITE);
        rly_photo.setBackgroundColor(mContext.getResources().getColor(R.color.gray));


        iv_icon.setImageResource(R.mipmap.ic_launcher);
        tv_url_des.setText("我在知耳等你来！");


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    iconBitmap = Glide.with(mContext).load(R.mipmap.ic_launcher).asBitmap().centerCrop().into(300, 300).get();


                    mHandler.sendEmptyMessage(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();




    }


    android.os.Handler mHandler = new android.os.Handler(new android.os.Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 100) {

                String url = "https://dev.img.zhiervip.com/official/share/7120bf239ca3624fbde517a17cbac791.jpg";

                if (!TextUtils.isEmpty(url)) {

                    Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {


                            if (resource != null && iconBitmap != null) {

                                Bitmap imageBitmap = BitmapUtils.setBitmapImage(mContext, resource, iconBitmap, "我是姓名");
                                iv_photo_bg.setImageBitmap(imageBitmap);

                                imageFile = BitmapUtils.saveBitmapFile(imageBitmap);


                            }

                        }
                    });


                }


            }

            return false;
        }
    });


    public void show() {
        if (mDialog != null) {
            if (mContext instanceof Activity) {
                if (((Activity) mContext).isFinishing()) {
                    return;
                }
            }
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lly_weibo:
                dismiss();
                break;

            case R.id.lly_weixin_circle:

                dismiss();

                break;

            case R.id.lly_weixin:

                dismiss();

                break;

            case R.id.iv_close:
                dismiss();
                break;

            case R.id.iv_select_url://选择链接
            case R.id.rly_url:

                iv_select_url.setSelected(true);
                iv_select_photo.setSelected(false);

                rly_photo.setBackgroundColor(Color.WHITE);
                rly_url.setBackgroundColor(mContext.getResources().getColor(R.color.gray));


                break;

            case R.id.iv_select_photo://选择图片
            case R.id.rly_photo:

                iv_select_photo.setSelected(true);
                iv_select_url.setSelected(false);
                rly_url.setBackgroundColor(Color.WHITE);
                rly_photo.setBackgroundColor(mContext.getResources().getColor(R.color.gray));


                break;





            case R.id.top_view:
                dismiss();
                break;

        }
    }


}
