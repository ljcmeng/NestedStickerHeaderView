package ljc.example.com.nestedscrollview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class NestedStickerHeaderView extends LinearLayout implements NestedScrollingParent2 {

    private int mMaxScrollTop = -1;
    private int mOriginBottom = -1;

    private static final String TAG = "NestedScrollView2";

    private View header;
    private RecyclerView recyclerView;

    public NestedStickerHeaderView(Context context) {
        super(context);
    }

    public NestedStickerHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        header = getChildAt(0);
        recyclerView = (RecyclerView) getChildAt(1);
    }

    public void setMaxScrollTop(int maxScrollTop) {
        this.mMaxScrollTop = maxScrollTop;
    }

    public void setOriginBottom(int originBottom) {
        this.mOriginBottom = originBottom;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes, int type) {
        //只处理垂直方向的滑动
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target,
        @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, @ViewCompat.NestedScrollType int type) {
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
        int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type) {
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //如果未初始化，则内部自己计算
        if (mMaxScrollTop == -1) {
            mMaxScrollTop = 0;
        }

        if (mOriginBottom == -1) {
            View header = getChildAt(0);
            LinearLayout.LayoutParams params = (LayoutParams) header.getLayoutParams();
            mOriginBottom = params.topMargin + header.getMeasuredHeight() + params.bottomMargin;
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
        @ViewCompat.NestedScrollType int type) {
        LinearLayoutManager linearLayoutManager =
            (LinearLayoutManager) recyclerView.getLayoutManager();
        //recycleview 是否滑到顶部
        boolean isFirstItemVisible =
            linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;

        //向上滑动，且头部没有滑倒最大高度,此时让header滑动
        if (dy > 0 && header.getBottom() > mMaxScrollTop) {
            int maxOffset = header.getBottom() - mMaxScrollTop;
            int offset = Math.min(maxOffset, dy);
            header.offsetTopAndBottom(offset * -1);

            recyclerView.layout(recyclerView.getLeft(), recyclerView.getTop() - offset,
                recyclerView.getRight(), recyclerView.getBottom());
            consumed[1] = offset;
        }
        //向下滑动，且recycleview已经滑倒顶部， header没有滑倒原始位置前，让头部滑动
        else if (dy < 0 && isFirstItemVisible && header.getBottom() < mOriginBottom) {
            int offset = Math.min(mOriginBottom - header.getBottom(), dy * -1);
            header.offsetTopAndBottom(offset);

            recyclerView.layout(recyclerView.getLeft(), recyclerView.getTop() + offset,
                recyclerView.getRight(), recyclerView.getBottom());
            consumed[1] = -1 * offset;
        }
    }
}
