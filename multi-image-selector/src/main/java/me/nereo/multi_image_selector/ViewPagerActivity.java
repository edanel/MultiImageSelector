package me.nereo.multi_image_selector;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 项目名称：MultiImageSelector
 * 类描述：
 * 创建人：Edanel
 * 创建时间：15/8/10 下午3:19
 * 修改人：Edanel
 * 修改时间：15/8/10 下午3:19
 * 修改备注：
 */
public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        setContentView(mViewPager);


    }

    static class SamplePagerAdapter extends PagerAdapter{


        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }
}
