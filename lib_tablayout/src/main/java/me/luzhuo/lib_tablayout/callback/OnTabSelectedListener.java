package me.luzhuo.lib_tablayout.callback;

public interface OnTabSelectedListener {
    /**
     * 选中标签
     */
    public void onTabSelected(int position);

    /**
     * 取消标签
     */
    public void onTabUnselected(int position);

    /**
     * 重复选中标签
     */
    public void onTabReselected(int position);
}
