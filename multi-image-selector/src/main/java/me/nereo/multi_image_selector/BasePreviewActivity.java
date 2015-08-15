package me.nereo.multi_image_selector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import me.nereo.multi_image_selector.view.HackyViewPager;
import me.nereo.multi_image_selector.view.PhotoPreview;

/**
 * 项目名称：MultiImageSelector
 * 类描述：
 * 创建人：Edanel
 * 创建时间：15/8/6 下午6:18
 * 修改人：Edanel
 * 修改时间：15/8/6 下午6:18
 * 修改备注：
 */
public class BasePreviewActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {


    private HackyViewPager viewPager;
    private Toolbar toolbar;
    private ArrayList<String> mSelectPath;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initView();
        initEvent();
    }

    private void initView() {
        viewPager = (HackyViewPager) findViewById(R.id.vp_base_app);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        currentItem = getIntent().getIntExtra("currentItem",0);

    }

    private void initEvent() {
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.addOnPageChangeListener(this);

        mSelectPath = getIntent().getStringArrayListExtra(MultiImageSelectorFragment.EXTRA_RESULT);

//        Toast.makeText(this, mSelectPath.size() + "", Toast.LENGTH_SHORT).show();
        bindData();

    }


    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            if (mSelectPath != null) {
                return mSelectPath.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            Log.i("", "isViewFromObject");
            return view == object;
        }

        @Override
        public View instantiateItem( ViewGroup container,  int position) {

            PhotoPreview photoPreview = new PhotoPreview(getApplicationContext());
            container.addView(photoPreview);
            photoPreview.loadImage(mSelectPath.get(position));
            photoPreview.setOnClickListener(photoItemClickListener);
            Log.i("", "instantiateItem");
            return photoPreview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i("", "destroyItem");
        }


    };
    private View.OnClickListener photoItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    protected void bindData() {
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(currentItem);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        updatePercent();

    }

    protected void updatePercent() {
        //TODO 更新显示当前页面位置
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
