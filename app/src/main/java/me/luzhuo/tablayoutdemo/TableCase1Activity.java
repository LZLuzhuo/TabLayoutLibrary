package me.luzhuo.tablayoutdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.animation.ArgbEvaluatorCompat;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.luzhuo.lib_core.ui.adapter.ViewPagerAdapter;
import me.luzhuo.lib_tablayout.TabLayout;

/**
 * 滑动时, 背景色跟着渐变
 */
public class TableCase1Activity extends AppCompatActivity {
    private static final String TAG = TableCase1Activity.class.getSimpleName();
    private static List<ViewPagerAdapter.ViewPagerBean> fragments = new ArrayList<>();

    public static void start(Context context) {
        context.startActivity(new Intent(context, TableCase1Activity.class));
    }

    static {
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xCCb71c1c), "阿双方均是"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xCC880e4f), "fsadf"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xCC4a148c), "上刊登"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xCC311b92), "思考"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xCC1a237e), "的交罚款桑德菲杰"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xCC004d40), "阿双方均是"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xCCbf360c), "HalloWord"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout_case1);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        View background_color = findViewById(R.id.background_color);
        viewPager.setAdapter(new ViewPagerAdapter(this, fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 5) {
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
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int scrolledPosition = position; // 0 -> 1 / 1 -> 0
                float currentPositionOffset = positionOffset; // 0.0 -> 0.99 / 0.99 -> 0.0
                Log.e(TAG, "scrolledPosition: " + scrolledPosition + " currentPositionOffset: " + currentPositionOffset);

                int startPosition = Math.max(scrolledPosition, 0);
                int endPosition = Math.min(scrolledPosition + 1, (fragments.size() - 1));
                int evaluate = ArgbEvaluatorCompat.getInstance().evaluate(positionOffset,
                        ((BackgroundColor)fragments.get(startPosition).fragment).getCurrentColor()/*colors.get(startPosition)*/,
                        ((BackgroundColor)fragments.get(endPosition).fragment).getCurrentColor()/*colors.get(endPosition)*/);
                background_color.setBackgroundColor(evaluate);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }
}