package me.nereo.multi_image_selector.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.R;
import me.nereo.multi_image_selector.SelectedDataConfig;
import me.nereo.multi_image_selector.bean.Image;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 */
public class ImageGridAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private LayoutInflater mInflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<Image> mImages = new ArrayList<>();
    private List<Image> mSelectedImages = new ArrayList<>();


    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;

    private SelectCallback mSelectCallback;

    public ImageGridAdapter(Context context, boolean showCamera) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }

    public ImageGridAdapter(Context context, boolean showCamera, SelectCallback selectCallback) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        this.mSelectCallback = selectCallback;
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }

    /**
     * 显示选择指示器
     *
     * @param b
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b) {
        if (showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     *
     * @param image
     */
    public void select(Image image) {
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
        } else {
            mSelectedImages.add(image);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取当前选择的图片列表
     *
     * @return
     */

    public ArrayList<String> getSelectResult() {
        if (mSelectedImages != null && mSelectedImages.size() != 0) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < mSelectedImages.size(); i++) {
                list.add(mSelectedImages.get(i).path);
            }
            return list;
        }
        return null;
    }

//    public ArrayList<String> getAllPic(){
//
//    }

    /**
     * 通过图片路径设置默认选择
     *
     * @param resultList
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        for (String path : resultList) {
            Image image = getImageByPath(path);
            if (image != null) {
                mSelectedImages.add(image);
            }
        }
        if (mSelectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }

    private Image getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (Image image : mImages) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     *
     * @param images
     */
    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 重置每个Column的Size
     *
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if (mItemSize == columnWidth) {
            return;
        }

        mItemSize = columnWidth;

        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera) {
            return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? mImages.size() + 1 : mImages.size();
    }

    @Override
    public Image getItem(int i) {
        if (showCamera) {
            if (i == 0) {
                return null;
            }
            return mImages.get(i - 1);
        } else {
            return mImages.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int type = getItemViewType(i);
        if (type == TYPE_CAMERA) {
            view = mInflater.inflate(R.layout.list_item_camera, viewGroup, false);
            view.setTag(null);
        } else if (type == TYPE_NORMAL) {
            ViewHolder holde;
            if (view == null) {
                view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                holde = new ViewHolder(view);
            } else {
                holde = (ViewHolder) view.getTag();
                if (holde == null) {
                    view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                    holde = new ViewHolder(view);
                }
            }
            if (holde != null) {
                holde.bindData(getItem(i));
            }
        }
        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if (lp.height != mItemSize) {
            view.setLayoutParams(mItemLayoutParams);
        }

        return view;
    }

    class ViewHolder {
        ImageView image;
        ImageView indicator;
        TextView count;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            indicator = (ImageView) view.findViewById(R.id.checkmark);
            count = (TextView) view.findViewById(R.id.checkmark_tv);
            view.setTag(this);
        }

        void bindData(final Image data) {
            if (data == null) return;
            // 处理单选和多选状态

            indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectCallback != null) {
                        mSelectCallback.onSelect(data);

                    }

                }
            });

            if (showSelectIndicator) {
                indicator.setVisibility(View.VISIBLE);

                if (mSelectedImages.contains(data)) {
                    // 设置选中状态
                    indicator.setImageResource(R.drawable.ic_chat_addimage_pressed);
                    image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                    if (!SelectedDataConfig.mSelected.containsKey(data.path)) {
                        SelectedDataConfig.mSelected.put(data.path, mSelectedImages.size());
                        count.setText((mSelectedImages.lastIndexOf(data) + 1) + "");
                    } else {
                        count.setText((mSelectedImages.lastIndexOf(data) + 1) + "");
                    }


                } else {
                    // 未选择
                    indicator.setImageResource(R.drawable.ic_chat_addimage);
                    image.clearColorFilter();
                }
            } else {
                indicator.setVisibility(View.GONE);

            }
            File imageFile = new File(data.path);

            if (mItemSize > 0) {
                // 显示图片
                Picasso.with(mContext)
                        .load(imageFile)
                        .placeholder(R.drawable.default_error)
                                //.error(R.drawable.default_error)
                        .resize(mItemSize, mItemSize)
                        .centerCrop()
                        .into(image);

            }
        }
    }

    public interface SelectCallback {
        void onSelect(Image image);
    }
}
