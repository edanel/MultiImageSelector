package me.nereo.multi_image_selector.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.File;
import java.io.IOException;

import me.nereo.multi_image_selector.BitmapTransform;
import me.nereo.multi_image_selector.R;
import me.nereo.multi_image_selector.photoview.PhotoView;

/**
 * 项目名称：MultiImageSelector
 * 类描述：
 * 创建人：Edanel
 * 创建时间：15/8/7 上午10:09
 * 修改人：Edanel
 * 修改时间：15/8/7 上午10:09
 * 修改备注：
 */
public class PhotoPreview extends LinearLayout implements OnClickListener {

    private PhotoView ivContent;
    private OnClickListener l;
    private Context context;

    public PhotoPreview(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);
        ivContent = (PhotoView) findViewById(R.id.iv_content_vpp);
        ivContent.setOnClickListener(this);
//        initImageLoader();

    }

    public PhotoPreview(Context context, AttributeSet attrs) {
        this(context);
    }

    public PhotoPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context);
    }

    public void getImageBitmap(String path) {
        final int MAX_WIDTH = 500;
        final int MAX_HEIGHT = 500;
        int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

        try {
            Picasso.with(context).load(new File(path)).transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                    .resize(size, size).centerCrop().get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadImage(final String path) {
        final int MAX_WIDTH = 500;
        final int MAX_HEIGHT = 500;
        int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

        Picasso.with(context).load(new File(path)).transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .resize(size, size)
                .centerCrop().into(ivContent, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("", "SUCCESS");
                ivContent.clearColorFilter();
            }

            @Override
            public void onError() {
                Log.d("", "ERROR");
            }
        });


//        ImageLoader.getInstance().loadImage("file://"+path, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                ivContent.setImageBitmap(loadedImage);
////                pbLoading.setVisibility(View.GONE);
//            }
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
////                ivContent.setImageDrawable(getResources().getDrawable(R.drawable.ic_picture_loadfailed));
////                pbLoading.setVisibility(View.GONE);
//            }
//        });
    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        this.l = l;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_content_vpp && l != null) {
            l.onClick(ivContent);
        }
    }
}
