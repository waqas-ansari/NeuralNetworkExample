package com.waqaansari.neuralnetworkexample.customization;

/**
 * Created by KFMWA916 on 4/24/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Linta Ansari on 6/28/2016.
 */
public class ExpandedListView extends ListView {
    private android.view.ViewGroup.LayoutParams params;
    private int old_count = 0;

    public ExpandedListView(Context context) {
        super(context);
    }

    public ExpandedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandedListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        params = getLayoutParams();
        if (getCount() != old_count) {
            old_count = getCount();
            params.height = getCount() * (old_count > 0 ? getChildAt(0).getHeight()+5 : 0);
            setLayoutParams(params);
        }
        super.onDraw(canvas);
    }
}