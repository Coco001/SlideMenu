package com.gkk.slidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 自定义view,用于包含两个子view
 */
public class SlideView extends ViewGroup{

    private View leftView;
    private View rightView;
    private int leftWidth;
    private Scroller scroller;
    private float downX;
    private float downY;
    private boolean isLeftShow = false;

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context); //用于模拟数据变化
    }

    /**
     * 布局加载完成调用
     */
    @Override
    protected void onFinishInflate() {
        //xml文件加载完成之后调用
        leftView = getChildAt(0);//获取左侧孩子的xml布局文件
        rightView = getChildAt(1);//获取右侧孩子的xml布局文件

        LayoutParams params = leftView.getLayoutParams();//获取左侧布局的参数
        leftWidth = params.width;
        super.onFinishInflate();
    }

    /**
     * 用于设置控件的大小，如果需要设置子控件的大小，需要在此方法中调用子控件的measure()方法
     * @param widthMeasureSpec 父容器希望自己的宽度
     * @param heightMeasureSpec 父容器希望自己的高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 测量左侧
        int leftWidthMeasureSpec = MeasureSpec.makeMeasureSpec(leftWidth,MeasureSpec.EXACTLY);
        leftView.measure(leftWidthMeasureSpec, heightMeasureSpec);
        // 测量右侧
        rightView.measure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));//设置自己的宽高
    }

    /**
     * 设置自身的高
     * @param measureSpec 自身的高度
     * @return 设置之后的高度
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);//测量模式
        int size = MeasureSpec.getSize(measureSpec);//大小
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 200;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    /**
     * 设置自身的宽
     * @param measureSpec 自身的宽
     * @return 设置之后自身的宽
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);//
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 200;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //给孩子设置布局
        int leftLeft = leftView.getMeasuredWidth();
        leftLeft = -leftLeft;
        int leftHeight = leftView.getMeasuredHeight();
        leftView.layout(leftLeft, 0, 0, leftHeight);

        int rightWidth = rightView.getMeasuredWidth();
        int rightHeight = rightView.getMeasuredHeight();
        rightView.layout(0, 0, rightWidth, rightHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();

                if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
                    // 水平方向移动
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();

                int diffX = (int) (downX - moveX + 0.5f);// 四舍五入

                int scrollX = getScrollX() + diffX;

                if (scrollX < 0 && scrollX < -leftView.getMeasuredWidth()) {//从左往右滑，滑动距离大于左侧布局的宽度
                    // 从左往右滑动
                    scrollTo(-leftView.getMeasuredWidth(), 0);
                } else if (scrollX > 0) {//从右侧往左侧滑动
                    scrollTo(0, 0);
                } else {
                    // 标准滑动
                    scrollBy(diffX, 0);
                }
                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                // 松开时的逻辑
                // 判断是要去打开，要去关闭
                int width = leftView.getMeasuredWidth();
                int currentX = getScrollX();
                float middle = -width / 2f;
                switchMenu(currentX <= middle);//判断是否显示左侧布局
                break;
        }
        return true;
    }

    private void switchMenu(boolean showLeft) {
        isLeftShow = showLeft;
        int width = leftView.getMeasuredWidth();
        int currentX = getScrollX();
        if (!showLeft) {
            // 关闭
            // scrollTo(0, 0);
            int startX = currentX;
            int startY = 0;
            int endX = 0;
            int endY = 0;
            int dx = endX - startX;// 增量的值
            int dy = endY - startY;
            int duration = Math.abs(dx) * 10;// 时长
            if (duration >= 600) {
                duration = 600;
            }
            // 模拟数据变化
            scroller.startScroll(startX, startY, dx, dy, duration);
        } else {
            // 打开
            // scrollTo(-width, 0);
            int startX = currentX;
            int startY = 0;
            int endX = -width;
            int endY = 0;
            int dx = endX - startX;// 增量的值
            int dy = endY - startY;
            int duration = Math.abs(dx) * 10;// 时长
            if (duration >= 600) {
                duration = 600;
            }
            // 模拟数据变化
            scroller.startScroll(startX, startY, dx, dy, duration);
        }
        invalidate();// UI刷新---> draw() -->drawChild() --> computeScroll()
    }
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            // 更新位置
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }
    public void toggle() {
        switchMenu(!isLeftShow);
    }
}
