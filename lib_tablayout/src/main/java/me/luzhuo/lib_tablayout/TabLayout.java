/* Copyright 2022 Luzhuo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.luzhuo.lib_tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import me.luzhuo.lib_core.app.color.ColorManager;
import me.luzhuo.lib_core.ui.calculation.UICalculation;
import me.luzhuo.lib_tablayout.callback.OnTabSelectedListener;

public class TabLayout extends HorizontalScrollView {
    public TabLayout(Context context) {
        super(context);
    }
    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ColorManager color = new ColorManager(getContext());
        UICalculation ui = new UICalculation(getContext());
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TabLayout, defStyleAttr, 0);
        try {
            // viewpager
            viewpagerSmoothScroll = typedArray.getBoolean(R.styleable.TabLayout_tab_viewpager_smoothScroll, true);

            // tab
            tabSpaceEqual = typedArray.getBoolean(R.styleable.TabLayout_tab_spaceEqual, false);
            tabWidth = typedArray.getDimension(R.styleable.TabLayout_tab_width, 0);
            tabPadding = typedArray.getDimension(R.styleable.TabLayout_tab_padding, tabSpaceEqual || tabWidth > 0 ? 0 : ui.dp2px(10));

            // text
            selectedTextSize = typedArray.getDimension(R.styleable.TabLayout_tab_text_selectedTextSize, ui.sp2px(14));
            normalTextSize = typedArray.getDimension(R.styleable.TabLayout_tab_text_normalTextSize, ui.sp2px(14));
            selectedTextBold = typedArray.getBoolean(R.styleable.TabLayout_tab_text_selectedTextBold, false);
            selectedTextColor = typedArray.getColor(R.styleable.TabLayout_tab_text_selectedTextColor, color.getColorPrimary());
            normalTextColor = typedArray.getColor(R.styleable.TabLayout_tab_text_normalTextColor, color.getTextColorDefault());
            selectedTextBold = typedArray.getBoolean(R.styleable.TabLayout_tab_text_selectedTextBold, false);

            // indicator
            indicatorStyle = typedArray.getInt(R.styleable.TabLayout_tab_indicator_style, StyleUnderLine);
            indicatorWidth = typedArray.getDimension(R.styleable.TabLayout_tab_indicator_width, 0);
            indicatorHeight = typedArray.getDimension(R.styleable.TabLayout_tab_indicator_height, ui.dp2px(2));
            indicatorColor = typedArray.getColor(R.styleable.TabLayout_tab_indicator_color, color.getColorPrimary());
            int startColor = typedArray.getColor(R.styleable.TabLayout_tab_indicator_colors_start, filterColor);
            int centerColor = typedArray.getColor(R.styleable.TabLayout_tab_indicator_colors_center, filterColor);
            int endColor = typedArray.getColor(R.styleable.TabLayout_tab_indicator_colors_end, filterColor);
            indicatorColors = color2colors(startColor, centerColor, endColor);
            indicatorColorsAngle = typedArray.getInt(R.styleable.TabLayout_tab_indicator_colors_angle, 0);
            indicatorCorner = typedArray.getDimension(R.styleable.TabLayout_tab_indicator_corner, 0);
            indicatorMarginLeft = typedArray.getDimension(R.styleable.TabLayout_tab_indicator_margin_left, 0);
            indicatorMarginTop = typedArray.getDimension(R.styleable.TabLayout_tab_indicator_margin_top, 0);
            indicatorMarginRight = typedArray.getDimension(R.styleable.TabLayout_tab_indicator_margin_right, 0);
            indicatorMarginBottom = typedArray.getDimension(R.styleable.TabLayout_tab_indicator_margin_bottom, 0);

            // divider
            dividerWidth = typedArray.getDimension(R.styleable.TabLayout_tab_divider_width, 0);
            dividerColor = typedArray.getColor(R.styleable.TabLayout_tab_divider_color, color.getColorPrimary());
            dividerPadding = typedArray.getDimension(R.styleable.TabLayout_tab_divider_padding, 0);

        } finally {
            typedArray.recycle();
        }
    }

    private final static int filterColor = 1;
    @Nullable
    private int[] color2colors(int... colors) {
        if (colors.length <= 0) return null;

        // O(N^2)
        for (int p = colors.length - 1; p >= 0; p--) {
            for (int i = 0; i < p; i++) {
                if (colors[i] == filterColor) {
                    int temp = colors[i];
                    colors[i] = colors[i+1];
                    colors[i + 1] = temp;
                }
            }
        }

        // O(N)
        int effectiveSize = 0;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == filterColor) break;
            ++effectiveSize;
        }

        int[] newColors = Arrays.copyOf(colors, effectiveSize);
        if (newColors.length <= 0) return null;
        return newColors;
    }

    public static final int
            StyleNormal = 1 << 1, // ???????????????
            StyleUnderLine = 1 << 2, // ?????????
            StyleBlock = 1 << 3, // ????????????
            StyleTriangle = 1 << 4; // ?????????

    private final LinearLayout tabsContainer;
    private @Nullable ViewPager viewPager;
    private @Nullable String[] titles;
    private int currentTab = 0;
    private int tabCount = 0;
    private @Nullable OnTabSelectedListener tabLayoutListener;

    // Tab????????????, ViewPager????????????????????????, ????????????????????????
    private boolean viewpagerSmoothScroll = true;
    // Tab????????????
    private boolean tabSpaceEqual = false;
    // Tab?????????, ?????????????????????
    private float tabWidth = 0f;
    private float tabPadding = 0;

    private float selectedTextSize = 64;
    private float normalTextSize = 48;
    // ??????????????????tab??????, ???????????????
    private boolean selectedTextBold = false;
    private @ColorInt int selectedTextColor = 0xFFFF0000;
    private @ColorInt int normalTextColor = 0xFF00FF00;

    // ???????????????, ???????????????
    private int indicatorStyle = StyleTriangle;
    // ??????????????????????????????????????????
//    private boolean indicatorWidthEqualTitle = false;
    // ??????????????????: ???0????????????
    private float indicatorWidth = 0;
    // ??????????????????
    private float indicatorHeight = 0;
    // ??????????????????
    private int indicatorColor = 0x4DFF0000;
    // ?????????????????????, ?????????null, ????????????, ??????????????????; ????????????????????????
    private @Nullable int[] indicatorColors = null;
    // ????????????????????????
    private int indicatorColorsAngle = 0;
    // ??????????????????
    private float indicatorCorner = 30;
    private float indicatorMarginLeft = 0;
    private float indicatorMarginRight = 0;
    private float indicatorMarginTop = 0;
    private float indicatorMarginBottom = 0;

    // ??????????????????
    private float dividerWidth = 30;
    // ??????????????????
    private int dividerColor = 0xFF000000;
    // ?????????????????????
    private float dividerPadding = 12;
    {
        setFillViewport(true);
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        tabsContainer = new LinearLayout(getContext());
        addView(tabsContainer);
    }

    /**
     * ??????ViewPager
     */
    public void setupWithViewPager(ViewPager viewPager) {
        if (viewPager == null || viewPager.getAdapter() == null) throw new IllegalArgumentException("????????? ViewPager ??? ViewPager???adapter????????????.");

        this.currentTab = viewPager.getCurrentItem();
        this.viewPager = viewPager;
        this.viewPager.removeOnPageChangeListener(onPagerChangeListener);
        this.viewPager.addOnPageChangeListener(onPagerChangeListener);
        notifyDataSetChanged();
        updateTabSelection(currentTab);
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
        notifyDataSetChanged();
        updateTabSelection(currentTab);
    }

    private float currentPositionOffset = 0;
    private int scrolledPosition = 0;
    private final ViewPager.OnPageChangeListener onPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /*
            position:
            ?????????, ???????????????
            ?????????, ?????????-1
             */
            scrolledPosition = position;
            currentPositionOffset = positionOffset;
            scroll2CurrentTab();
            invalidate();
        }

        @Override
        public void onPageSelected(int position) {
            if (tabLayoutListener != null) {
                tabLayoutListener.onTabSelected(position);
                tabLayoutListener.onTabUnselected(currentTab);
            }
            currentTab = position;
            updateTabSelection(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    /**
     * ????????????
     * ????????????, ????????????Tab
     */
    public void notifyDataSetChanged() {
        this.tabsContainer.removeAllViews();
        if (viewPager != null) {
            this.tabCount = viewPager.getAdapter().getCount();
            for (int i = 0; i < this.tabCount; i++) {
                View tabView = View.inflate(getContext(), R.layout.tablayout_tab_text, null);
                CharSequence pageTitle = viewPager.getAdapter().getPageTitle(i);
                addTab(i, pageTitle.toString(), tabView);
            }
        } else if (titles != null) {
            this.tabCount = titles.length;
            for (int i = 0; i < this.tabCount; i++) {
                View tabView = View.inflate(getContext(), R.layout.tablayout_tab_text, null);
                String pageTitle = titles[i];
                addTab(i, pageTitle, tabView);
            }
        }
    }

    /**
     * ???????????????Tab
     */
    private void addTab(int position, String title, View tabView) {
        TextView tab_title = tabView.findViewById(R.id.tablayout_tab_title);
        if (tab_title != null && title != null) {
            tab_title.setText(title);
            tab_title.setPadding((int) tabPadding, 0, (int) tabPadding, 0);
        }

        tabView.setOnClickListener(tabClickListener);

        LinearLayout.LayoutParams tab_lp = tabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (tabWidth > 0) tab_lp = new LinearLayout.LayoutParams((int) tabWidth, LayoutParams.MATCH_PARENT);

        tabsContainer.addView(tabView, position, tab_lp);
    }

    private final OnClickListener tabClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // ????????????tab???LinearLayout????????????
            int position = tabsContainer.indexOfChild(view);
            if (position >= tabCount || position < 0) return;

            if (viewPager != null) {
                if (viewPager.getCurrentItem() != position) {
                    if (tabLayoutListener != null) {
                        tabLayoutListener.onTabSelected(position);
                        tabLayoutListener.onTabUnselected(currentTab);
                    }
                    updateTabSelection(position);
                    viewPager.setCurrentItem(position, viewpagerSmoothScroll);
                    currentTab = position;
                } else {
                    if (tabLayoutListener != null) tabLayoutListener.onTabReselected(position);
                }
            } else if (titles != null) {
                if (currentTab != position) {
                    if (tabLayoutListener != null) {
                        tabLayoutListener.onTabSelected(position);
                        tabLayoutListener.onTabUnselected(currentTab);
                    }
                    updateTabSelection(position);
                    currentTab = position;
                    scrolledPosition = position;
                    scroll2CurrentTab();
                    invalidate();
                } else {
                    if (tabLayoutListener != null) tabLayoutListener.onTabReselected(position);
                }
            }
        }
    };

    /**
     * ?????????????????????????????????
     */
    private void updateTabSelection(int tabPosition) {
        if (tabPosition >= tabCount || tabPosition < 0) return;

        for (int i = 0; i < tabCount; i++) {
            final boolean isSelected = i == tabPosition;
            View tabView = tabsContainer.getChildAt(i);
            TextView tab_title = tabView.findViewById(R.id.tablayout_tab_title);
            if (tab_title == null) return;

            tab_title.setTextColor(isSelected ? selectedTextColor : normalTextColor);
            if (selectedTextBold) tab_title.getPaint().setFakeBoldText(isSelected);
            tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, isSelected ? selectedTextSize : normalTextSize);
        }
    }

    /**
     * ??????Tab???????????????
     */
    public void setTabLayoutListener(@Nullable OnTabSelectedListener tabLayoutListener) {
        this.tabLayoutListener = tabLayoutListener;
    }

    private int mLastScrollX;
    private final Rect tabRect = new Rect(); // ??????????????????
    private final Rect indicatorRect = new Rect(); // ?????????????????????
    /**
     * ????????????Tab???????????????
     */
    private void scroll2CurrentTab() {
        if (tabCount <= 0 || tabsContainer.getChildCount() <= 0) return;

        // currentPositionOffset 0 -> 1.0
        int offset = (int) (currentPositionOffset * tabsContainer.getChildAt(scrolledPosition).getWidth());
        int newScrollX = tabsContainer.getChildAt(scrolledPosition).getLeft() + offset;

        // HorizontalScrollView ????????????Tab, ???????????????
        if (scrolledPosition > 0 || offset > 0) {
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calculateIndicatorRec();
            newScrollX += ((tabRect.right - tabRect.left) / 2);
        }
        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            scrollTo(newScrollX/*?????????*/, 0);
        }
    }

//    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private void calculateIndicatorRec() {
        View currentTabView = tabsContainer.getChildAt(this.scrolledPosition);
        if (currentTabView == null) return;
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        // ??????????????????
//        float margin = 0;
//        if (indicatorWidthEqualTitle) {
//            TextView tab_title = currentTabView.findViewById(R.id.tablayout_tab_title);
//            textPaint.setTextSize(normalTextSize);
//            float textWidth = textPaint.measureText(tab_title.getText().toString());
//            margin = (right - left - textWidth) / 2;
//        }

        if (this.scrolledPosition < tabCount - 1) {
            View nextTabView = tabsContainer.getChildAt(this.scrolledPosition + 1);
            float nextLeft = nextTabView.getLeft();
            float nextRight = nextTabView.getRight();

            left = left + currentPositionOffset * (nextLeft - left);
            right = right + currentPositionOffset * (nextRight - right);

            // ??????????????????
//            if (indicatorWidthEqualTitle) {
//                TextView tab_title = currentTabView.findViewById(R.id.tablayout_tab_title);
//                textPaint.setTextSize(normalTextSize);
//                float nextTextWidth = textPaint.measureText(tab_title.getText().toString());
//                float nextMargin = (nextRight - nextLeft - nextTextWidth) / 2;
//                margin = margin + currentPositionOffset * (nextMargin - margin);
//            }
        }

        tabRect.left = (int) left;
        tabRect.right = (int) right;

        // ?????????
        indicatorRect.left = (int) left;
        indicatorRect.right = (int) right;
//        if (indicatorWidthEqualTitle) {
//            indicatorRect.left = (int) (left + margin - 1);
//            indicatorRect.right = (int) (right - margin - 1);
//        }

        // ????????????????????????
        if (indicatorWidth > 0) {
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - indicatorWidth) / 2;
            if (this.scrolledPosition < tabCount - 1) {
                View nextTabView = tabsContainer.getChildAt(this.scrolledPosition + 1);
                indicatorLeft = indicatorLeft + currentPositionOffset * (currentTabView.getWidth() / 2 + nextTabView.getWidth() / 2);
            }
            indicatorRect.left = (int) indicatorLeft;
            indicatorRect.right = (int) (indicatorRect.left + indicatorWidth);
        }
    }

    private final Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private GradientDrawable indicatorDrawable = new GradientDrawable();
    private final Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path trianglePath = new Path();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount <= 0) return;

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // ????????????????????? |
        if (dividerWidth > 0) {
            dividerPaint.setStrokeWidth(dividerWidth);
            dividerPaint.setColor(dividerColor);
            for (int i = 0; i < tabCount; i++) {
                View tab_View = tabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab_View.getRight(), dividerPadding, paddingLeft + tab_View.getRight(), height - dividerPadding, dividerPaint);
            }
        }

        // ????????????
        calculateIndicatorRec();
        if (indicatorStyle == StyleNormal) {
            // ??????, ?????????????????????
        } else if (indicatorStyle == StyleUnderLine) {
            // ?????????
            if (indicatorHeight <= 0) return;
            if (indicatorColors != null) {
                indicatorDrawable.setColors(indicatorColors);
                indicatorDrawable.setOrientation(getOrientation(indicatorColorsAngle));
            } else {
                indicatorDrawable.setColor(indicatorColor);
            }

            indicatorDrawable.setBounds(
                    (int)(paddingLeft + indicatorMarginLeft + indicatorRect.left),
                    (int)(height - indicatorHeight - indicatorMarginBottom),
                    (int)(paddingLeft + indicatorRect.right - indicatorMarginRight),
                    (int)(height - indicatorMarginBottom));

            indicatorDrawable.setCornerRadius(indicatorCorner);
            indicatorDrawable.draw(canvas);

        } else if (indicatorStyle == StyleBlock) {
            // ????????????
            if (indicatorHeight <= 0) indicatorHeight = (int) (height - indicatorMarginTop - indicatorMarginBottom);
            if (indicatorHeight <= 0) return;

            if (indicatorCorner < 0 || indicatorCorner > indicatorHeight / 2) indicatorCorner = indicatorHeight / 2;

            if (indicatorColors != null) {
                indicatorDrawable.setColors(indicatorColors);
                indicatorDrawable.setOrientation(getOrientation(indicatorColorsAngle));
            } else {
                indicatorDrawable.setColor(indicatorColor);
            }

            indicatorDrawable.setBounds(
                    (int)(paddingLeft + indicatorMarginLeft + indicatorRect.left),
                    (int)(indicatorMarginTop),
                    (int)(paddingLeft + indicatorRect.right - indicatorMarginRight),
                    (int)(indicatorMarginTop + indicatorHeight));

            indicatorDrawable.setCornerRadius(indicatorCorner);
            indicatorDrawable.draw(canvas);

        } else if (indicatorStyle == StyleTriangle) {
            // ?????????
            if (indicatorHeight <= 0) return;

            trianglePaint.setColor(indicatorColor);
            if (indicatorColors != null) trianglePaint.setShader(new LinearGradient(paddingLeft + indicatorRect.left, height, paddingLeft + indicatorRect.right, height, indicatorColors, null, Shader.TileMode.CLAMP));
            trianglePath.reset();
            trianglePath.moveTo(paddingLeft + indicatorRect.left, height);
            trianglePath.lineTo(paddingLeft + indicatorRect.left / 2 + indicatorRect.right / 2, height - indicatorHeight);
            trianglePath.lineTo(paddingLeft + indicatorRect.right, height);
            trianglePath.close();
            canvas.drawPath(trianglePath, trianglePaint);
        }
    }

    private GradientDrawable.Orientation getOrientation(int angle) {
        switch (angle % 360) {
            case 45:
                return GradientDrawable.Orientation.BL_TR;
            case 90:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 135:
                return GradientDrawable.Orientation.BR_TL;
            case 180:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 225:
                return GradientDrawable.Orientation.TR_BL;
            case 270:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 315:
                return GradientDrawable.Orientation.TL_BR;
            case 0:
            default:
                return GradientDrawable.Orientation.LEFT_RIGHT;
        }
    }

    /**
     * ???????????????????????????
     */
    public void setNormalTextColor(@ColorInt int normalTextColor) {
        this.normalTextColor = normalTextColor;
        updateTabSelection(currentTab);
    }

    /**
     * ???????????????????????????
     */
    public void setSelectedTextColor(@ColorInt int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        updateTabSelection(currentTab);
    }

    /**
     * ????????????????????????
     */
    public void setIndicatorColor(@ColorInt int indicatorColor) {
        this.indicatorColor = indicatorColor;
        this.indicatorColors = null;
        reset();
    }

    /**
     * ???????????????????????????
     */
    public void setIndicatorColors(@ColorInt @NonNull int... indicatorColors) {
        this.indicatorColors = indicatorColors;
        reset();
    }

    private void reset() {
        trianglePaint.reset();
        indicatorDrawable = new GradientDrawable();
    }

    /**
     * ???????????????????????????
     */
    public void setNormalTextSize(int textSizePx) {
        this.normalTextSize = textSizePx;
    }

    /**
     * ???????????????????????????
     */
    public void setSelectedTextSize(int textSizePx) {
        this.selectedTextSize = textSizePx;
    }

    /**
     * ?????????????????????
     */
    public void setCurrentTab(int currentTab, boolean smoothScroll) {
        this.currentTab = currentTab;
        this.scrolledPosition = currentTab;
        if (viewPager != null) {
            viewPager.setCurrentItem(currentTab, smoothScroll);
        } else {
            updateTabSelection(currentTab);
            invalidate();
        }
    }

    public void setCurrentTab(int currentTab) {
        setCurrentTab(currentTab, viewpagerSmoothScroll);
    }
}
