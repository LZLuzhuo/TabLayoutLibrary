package me.luzhuo.tablayoutdemo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 基本使用-均分模式-与系统库的对比
    public void base_average_system(View view) {
        BaseAverageSystemActivity.start(this);
    }

    // 基本使用-滚动模式-与系统库的对比
    public void base_scroll_system(View view) {
        BaseScrollSystemActivity.start(this);
    }

    // 基本使用-不使用ViewPager
    public void base_no_viewpager(View view) {
        BaseNoViewPagerActivity.start(this);
    }

    // 进阶使用-指示器样式
    public void table_style_indicator(View view) {
        IndicatorStyleActivity.start(this);
    }

    // 进阶使用-渐变的指示器
    public void table_indicator2(View view) {
        Indicator2Activity.start(this);
    }

    // 进阶使用-案例-更新背景色
    public void table_case1(View view) {
        TableCase1Activity.start(this);
    }
}