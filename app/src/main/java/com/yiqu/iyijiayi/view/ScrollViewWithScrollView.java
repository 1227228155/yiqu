package com.yiqu.iyijiayi.view;

import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/2/14.
 */

public class ScrollViewWithScrollView extends ScrollView {

    public ScrollViewWithScrollView(android.content.Context context,
                                    android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}