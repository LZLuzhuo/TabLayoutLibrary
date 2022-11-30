package me.luzhuo.tablayoutdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.luzhuo.lib_tablayout.TabLayout;

// 只使用TabLayout, 不使用ViewPager
public class BaseNoViewPagerActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, BaseNoViewPagerActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_viewpager);

        TabLayout tabLayout = findViewById(R.id.luzhuo_tabLayout);
        tabLayout.setTitles(new String[]{"阿双方均是", "fsadf", "上刊登"});
        tabLayout.setCurrentTab(1);
    }
}
