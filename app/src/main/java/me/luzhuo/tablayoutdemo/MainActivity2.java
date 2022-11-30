package me.luzhuo.tablayoutdemo;

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
public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = MainActivity2.class.getSimpleName();
    private static List<ViewPagerAdapter.ViewPagerBean> fragments = new ArrayList<>();
//    private static List<Integer> colors = new ArrayList<>();

    static {
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xFFb71c1c), "阿双方均是"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xFF880e4f), "fsadf"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xFF4a148c), "上刊登"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xFF311b92), "思考"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xFF1a237e), "的交罚款桑德菲杰"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xFF004d40), "阿双方均是"));
        fragments.add(new ViewPagerAdapter.ViewPagerBean(MyFragment.instance(0xFFbf360c), "HalloWord"));

//        colors.add(0xFFb71c1c);
//        colors.add(0xFF880e4f);
//        colors.add(0xFF4a148c);
//        colors.add(0xFF311b92);
//        colors.add(0xFF1a237e);
//        colors.add(0xFF004d40);
//        colors.add(0xFFbf360c);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        View background_color = findViewById(R.id.background_color);
        viewPager.setAdapter(new ViewPagerAdapter(this, fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }
}