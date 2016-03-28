package com.ssf.bookingnow;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonnalMadeActivity extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener {

    private static final String TAG = PersonnalMadeActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.nestedscrollview)
    NestedScrollView nestedScrollView;

    @Bind(R.id.headview)
    FrameLayout headLayout;

    private LinearLayout nestedlayout;

    private int nestedChildCount = 0;

    private int headChildCount = 0;

    private int headChildHeight = 0;


    private float offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personnal_made);

        ButterKnife.bind(this);

        setUpToolbar();

        nestedScrollView.setOnScrollChangeListener(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        nestedlayout = (LinearLayout) nestedScrollView.getChildAt(0);
        nestedChildCount = nestedlayout.getChildCount();
        headChildCount = headLayout.getChildCount();
        headChildHeight = getLayoutChildHeight(headLayout);
    }

    private int getLayoutChildHeight(ViewGroup headLayout) {
        int height = 0;
        for (int i = 0; i < headLayout.getChildCount(); i++) {
            height += headLayout.getChildAt(i).getHeight();
        }
        return height;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 轮动监听
     *
     * @param v
     * @param scrollX
     * @param scrollY
     * @param oldScrollX
     * @param oldScrollY
     */
    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        float cursor = scrollY + offset;
        int scrollYItem = getYItem(cursor);
        View view = null;
        switch (scrollYItem) {
            case 0:
                view = headLayout.getChildAt(3);
                offset = calculateItemOffset(view, scrollYItem, cursor, 0);
                break;
            case 1:
                if (oldScrollY > scrollY) {
                    setLastTranslation(4);
                }
                break;
            case 2:
                view = headLayout.getChildAt(3);
                offset = calculateItemOffset(view, scrollYItem, cursor, 1);
                View view4 = headLayout.getChildAt(4);
                view4.setTranslationY(offset - view.getHeight());
                if (oldScrollY > scrollY) {
                    setLastTranslation(2);
                }
                break;
            case 3:
                view = headLayout.getChildAt(2);
                offset = calculateItemOffset(view, scrollYItem, cursor, 1);
                if (oldScrollY > scrollY) {
                    setLastTranslation(1);
                } else {
                    setLastTranslationY(3, 2);
                    setLastTranslationY(4, 1);
                }
                break;
            case 4:
                view = headLayout.getChildAt(1);
                offset = calculateItemOffset(view, scrollYItem, cursor, 1);
                if (oldScrollY > scrollY) {
                    setLastTranslation(0);
                } else {
                    setLastTranslationY(2, 3);
                }
                break;
            case 5:
                view = headLayout.getChildAt(0);
                offset = calculateItemOffset(view, scrollYItem, cursor, 1);
                setLastTranslationY(1, 4);
                setLastItemHeight();
                break;

        }
        if (null != view) {
            view.setTranslationY(offset);
        }

        if (scrollY == 0) {
            setLastTranslation(3);
            setLastTranslation(4);
        }
    }


    private void setLastTranslationY(int item, int y) {
        View view = headLayout.getChildAt(item);
        view.setTranslationY(view.getHeight() * y);
    }

    private void setLastTranslation(int item) {
        View view = headLayout.getChildAt(item);
        view.setTranslationY(-view.getHeight());
    }

    private float calculateItemOffset(View view, int scrollYItem, float scrollY, int tranCount) {
        int scrollHeight = nestedlayout.getChildAt(scrollYItem).getHeight();
        int viewHeight = view.getHeight();
        float offset = scrollY / scrollHeight * viewHeight;
        return offset - viewHeight * tranCount;
    }

    /**
     * 设置最后一个Item的高
     */
    private void setLastItemHeight() {
        View view = nestedlayout.getChildAt(nestedlayout.getChildCount() - 1);
        int headHeight = headLayout.getHeight();
        int lastHeight = headHeight - headChildHeight;
        if (lastHeight != view.getHeight() && view instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) view;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
            params.height = lastHeight;
            layout.setLayoutParams(params);
        }
    }

    /**
     * 获取第一个Item
     *
     * @param y
     * @return
     */
    private int getYItem(float y) {
        int itemHeight = 0;
        for (int i = 0; i < nestedChildCount; i++) {
            int height = nestedlayout.getChildAt(i).getHeight();
            if (y >= itemHeight && y < itemHeight + height) {
                return i;
            } else {
                itemHeight += height;
            }
        }
        return 0;
    }

}
