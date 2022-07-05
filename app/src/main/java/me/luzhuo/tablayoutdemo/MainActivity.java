package me.luzhuo.tablayoutdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.luzhuo.lib_core.ui.adapter.ViewPagerAdapter;
import me.luzhuo.lib_tablayout.TabLayout;
import me.luzhuo.lib_tablayout.callback.OnTabSelectedListener;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static List<ViewPagerAdapter.ViewPagerBean> fragments = new ArrayList<>();

    static {
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "阿双方均是"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "fsadf"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "上刊登"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "思考"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "的交罚款桑德菲杰"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "阿双方均是"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "HalloWord"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        com.google.android.material.tabs.TabLayout tabLayout2 = findViewById(R.id.tabLayout2);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(this, fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(fragments.size());

        tabLayout.setupWithViewPager(viewPager);
        tabLayout2.setupWithViewPager(viewPager);

        tabLayout.setTitles(new String[]{"阿双方均是", "fsadf", "上刊登", "思考", "的交罚款桑德菲杰", "阿双方均是", "HalloWord"});
        //tabLayout.setCurrentTab(2);

        tabLayout.setTabLayoutListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (position == 0 || position == 4) {
                    // 设置白色的文字, 白色的指示器
                    tabLayout.setSelectedTextColor(0xFFFEFEFE);
                    tabLayout.setNormalTextColor(0xE6FEFEFE);
                    tabLayout.setIndicatorColor(0xFFFFFFFF);
                } else {
                    // 设置黑色的文字, 渐变的指示器
                    tabLayout.setSelectedTextColor(0xFF161616);
                    tabLayout.setNormalTextColor(0xFF3A3A3A);
                    tabLayout.setIndicatorColors(0xFFEA5F34, 0xFFEB6C3A, 0xFFED9342);
                }
                Log.e(TAG, "onTabSelected: " + position);
            }

            @Override
            public void onTabUnselected(int position) {
                Log.e(TAG, "onTabUnselected: " + position);
            }

            @Override
            public void onTabReselected(int position) {
                Log.e(TAG, "onTabReselected: " + position);
            }
        });

        TabLayout tabLayout3 = findViewById(R.id.tabLayout3);
        tabLayout3.setupWithViewPager(viewPager);
    }
}