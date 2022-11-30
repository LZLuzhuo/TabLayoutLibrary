package me.luzhuo.tablayoutdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.luzhuo.lib_core.ui.adapter.ViewPagerAdapter;
import me.luzhuo.lib_tablayout.TabLayout;

// 指示器的样式
public class IndicatorStyleActivity extends AppCompatActivity {
    private static List<ViewPagerAdapter.ViewPagerBean> fragments = new ArrayList<>();

    public static void start(Context context) {
        context.startActivity(new Intent(context, IndicatorStyleActivity.class));
    }

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_indicator);

        TabLayout tabLayoutStyle0 = findViewById(R.id.tabLayout_style0);
        TabLayout tabLayoutStyle1 = findViewById(R.id.tabLayout_style1);
        TabLayout tabLayoutStyle2 = findViewById(R.id.tabLayout_style2);
        TabLayout tabLayoutStyle3 = findViewById(R.id.tabLayout_style3);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(this, fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(fragments.size());

        tabLayoutStyle0.setupWithViewPager(viewPager);
        tabLayoutStyle1.setupWithViewPager(viewPager);
        tabLayoutStyle2.setupWithViewPager(viewPager);
        tabLayoutStyle3.setupWithViewPager(viewPager);
    }
}
