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

/**
 * Luzhuo的TabLayout 与 系统的TabLayout 效果做对比
 */
public class BaseAverageSystemActivity extends AppCompatActivity {
    private static List<ViewPagerAdapter.ViewPagerBean> fragments = new ArrayList<>();

    public static void start(Context context) {
        context.startActivity(new Intent(context, BaseAverageSystemActivity.class));
    }

    static {
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "阿双方均是"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "fsadf"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(new MyFragment(), "上刊登"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_average_system);

        com.google.android.material.tabs.TabLayout systemTabLayout = findViewById(R.id.system_tabLayout);
        TabLayout luzhuoTabLayout = findViewById(R.id.luzhuo_tabLayout);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(this, fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(fragments.size());

        systemTabLayout.setupWithViewPager(viewPager);
        luzhuoTabLayout.setupWithViewPager(viewPager);
    }
}
