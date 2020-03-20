package com.liner.familytracker.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Api;
import com.liner.familytracker.R;

public class SlidingPanelLayout extends ViewGroup implements GestureDetector.OnGestureListener {
    private boolean mCanSwipeToHide;
    private boolean mCancelPanelDispatch;
    private GestureDetector mGestureDetector;
    private boolean mIsGestureHandled;
    private boolean mIsGestureOngoing;
    private boolean mIsHidden;
    private boolean mIsLaidOut;
    private boolean mIsMotionOngoing;
    private boolean mIsPanelMotion;
    private boolean mIsScrollingDown;
    private boolean mIsScrollingUp;
    private View mMainView;
    private View mPanelView;
    private float mParallax;
    private RecyclerView mRecyclerView;
    private int mRecyclerViewScrollY;
    private int mRestingPanelHeight;
    private ScrollView mScrollView;
    private SlidingPanelScroller mScroller;
    private final Runnable mSetViewsTranslationYRunnable = new Runnable() {
        public void run() {
            setViewsTranslationY();
        }
    };

    public void onPanelHidden() {
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public SlidingPanelLayout(Context context) {
        super(context);
        initialize(context);
    }

    public SlidingPanelLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
        obtainAttributes(context, attributeSet);
    }

    public SlidingPanelLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context);
        obtainAttributes(context, attributeSet);
    }

    @TargetApi(21)
    public SlidingPanelLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize(context);
        obtainAttributes(context, attributeSet);
    }

    private void initialize(Context context) {
        mGestureDetector = new GestureDetector(context, this);
        mScroller = new SlidingPanelScroller(context);
    }

    private void obtainAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SlidingPanelLayout, 0, 0);
            try {
                mCanSwipeToHide = obtainStyledAttributes.getBoolean(R.styleable.SlidingPanelLayout_sp_canSwipeToHide, mCanSwipeToHide);
                mRestingPanelHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SlidingPanelLayout_sp_minimumPanelHeight, mRestingPanelHeight);
                mParallax = obtainStyledAttributes.getFloat(R.styleable.SlidingPanelLayout_sp_parallax, mParallax);
            } finally {
                obtainStyledAttributes.recycle();
            }
        }
    }

    public void setRestingPanelHeight(int i) {
        mRestingPanelHeight = i;
        setupScroller();
        setViewsTranslationY();
    }

    public void setScrollableView(ScrollView scrollView) {
        mScrollView = scrollView;
        mScrollView.scrollTo(0, 0);
        if (mRecyclerView != null) {
            mRecyclerView = null;
        }
    }

    public void setScrollableView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerViewScrollY = 0;
        mRecyclerView.smoothScrollToPosition(0);
        if (mScrollView != null) {
            mScroller = null;
        }
    }

    public void hide() {
        mIsHidden = true;
        mIsPanelMotion = false;
        if (mIsLaidOut) {
            mScroller.scrollTo(getHeight());
            setViewsTranslationY();
        } else {
            mScroller.setPosition((float) getHeight());
        }
        resetScrollableViewPosition();
    }

    private void resetScrollableViewPosition() {
        if (mScrollView != null) {
            mScrollView.scrollTo(0, 0);
        } else if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public void showResting() {
        mIsHidden = false;
        if (mIsLaidOut) {
            mScroller.scrollToRestingPosition();
            setViewsTranslationY();
            return;
        }
        mScroller.setPosition((float) mScroller.getRestingPosition());
    }

    public void showExpanded() {
        mIsHidden = false;
        if (mIsLaidOut) {
            mScroller.scrollTo(0);
            setViewsTranslationY();
            return;
        }
        mScroller.setPosition(0.0f);
    }

    public void showAt(float f) {
        mScroller.scrollTo((int) (((float) mScroller.getRestingPosition()) * (1.0f - f)));
        setViewsTranslationY();
    }

    public void scrollToRestingPosition() {
        mScroller.scrollToRestingPosition();
        resetScrollableViewPosition();
        setViewsTranslationY();
    }

    public boolean isScrolled() {
        return mScroller.getOffsetFromRestingPosition() < 0;
    }

    public boolean isHidden() {
        return mIsHidden;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), i), getDefaultSize(getSuggestedMinimumHeight(), i2));
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            getChildAt(i3).measure(i, i2);
        }
    }

    @Override
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5 = i4 - i2;
            int i6 = i3 - i;
            mMainView = getChildAt(0);
            mMainView.layout(0, 0, i6, i5);
            mPanelView = getChildAt(1);
            mPanelView.layout(0, 0, i6, i5);
            mIsLaidOut = true;
            setupScroller();
            setViewsTranslationY();
            return;

        //throw new RuntimeException(SlidingPanelLayout.class.getSimpleName() + " should have exactly two children");
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setupScroller();
    }

    private void setupScroller() {
        int i;
        mScroller.setRestingPosition(getHeight() - mRestingPanelHeight);
        if (mCanSwipeToHide) {
            mScroller.setMaximumPosition(getHeight());
        } else {
            mScroller.setMaximumPosition(mScroller.getRestingPosition());
        }
        if (mIsLaidOut) {
            if (mScrollView != null && mScrollView.getChildCount() == 1) {
                i = Math.max(mPanelView.getHeight(), mScrollView.getChildAt(0).getHeight());
            } else if (mRecyclerView != null) {
                i = Api.BaseClientBuilder.API_PRIORITY_OTHER;
            } else {
                i = mPanelView.getHeight();
            }
            mScroller.setMinimumPosition(getHeight() - i);
        }
        if (mIsHidden) {
            mScroller.setPosition((float) getHeight());
        }
    }

    public void setViewsTranslationY() {
        if (mParallax <= 0.0f || mScroller.getOffsetFromRestingPosition() >= 0 || mScroller.getCurrentPosition() < 0) {
            mMainView.setTranslationY(0.0f);
        } else {
            mMainView.setTranslationY((float) ((int) (((float) mScroller.getOffsetFromRestingPosition()) * mParallax)));
        }
        if (mScroller.computeScrollOffset()) {
            post(mSetViewsTranslationYRunnable);
        } else if (mIsGestureOngoing && !mIsMotionOngoing) {
            onGestureStop();
        }
        float currentPosition = (float) mScroller.getCurrentPosition();
        mPanelView.setTranslationY((float) ((int) Math.max(0.0f, currentPosition)));
        if (mScrollView != null) {
            mScrollView.scrollTo(0, Math.max(0, (int) (-currentPosition)));
        } else if (mRecyclerView == null) {
        } else {
            if (currentPosition < 0.0f || mRecyclerViewScrollY > 0) {
                int min = (int) (((float) (-mRecyclerViewScrollY)) - Math.min(0.0f, currentPosition));
                if (canScrollVertically()) {
                    mRecyclerView.scrollBy(0, min);
                    mRecyclerViewScrollY += min;
                } else if (mIsScrollingUp) {
                    mScroller.setPosition(Math.max(0.0f, currentPosition));
                    mRecyclerViewScrollY = 0;
                } else if (mIsScrollingDown && mScroller.isInFling()) {
                    mScroller.forceFinished(true);
                    onGestureStop();
                }
            }
        }
    }

    private boolean canScrollVertically() {
        if (mIsScrollingDown) {
            return mRecyclerView.canScrollVertically(1);
        }
        if (mIsScrollingUp) {
            return mRecyclerView.canScrollVertically(-1);
        }
        return false;
    }

    private boolean onGestureStop() {
        mIsGestureOngoing = false;
        mIsScrollingDown = false;
        mIsScrollingUp = false;
        if (!mScroller.isOverScrolled()) {
            return false;
        }
        int restingPosition = mScroller.getRestingPosition() + (int) ((((float) 70) * getContext().getResources().getDisplayMetrics().density) + 0.5f);
        if (mCanSwipeToHide && mScroller.getCurrentPosition() >= restingPosition) {
            hide();
            onPanelHidden();
            return true;
        } else if (mIsHidden) {
            return true;
        } else {
            scrollToRestingPosition();
            return true;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            mIsPanelMotion = !mIsHidden && isOverPanel(motionEvent);
            if (mIsPanelMotion) {
                mIsMotionOngoing = true;
                mIsGestureHandled = false;
            }
        }
        if (mIsPanelMotion) {
            mGestureDetector.onTouchEvent(motionEvent);
            motionEvent.offsetLocation(0.0f, (float) (-Math.max(0, mScroller.getCurrentPosition())));
            if (!mIsGestureHandled) {
                mPanelView.dispatchTouchEvent(motionEvent);
            } else if (mCancelPanelDispatch && motionEvent.getAction() != 0) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setAction(3);
                mPanelView.dispatchTouchEvent(obtain);
                mCancelPanelDispatch = false;
            }
        } else {
            mMainView.dispatchTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action == 1 || action == 3) {
            if (mIsPanelMotion && !mScroller.isInFling()) {
                onGestureStop();
            }
            mIsMotionOngoing = false;
            mIsPanelMotion = false;
        }
        return true;
    }

    private boolean isOverPanel(MotionEvent motionEvent) {
        return motionEvent.getY() >= ((float) mScroller.getCurrentPosition());
    }

    public boolean onDown(MotionEvent motionEvent) {
        mScroller.forceFinished(true);
        mIsGestureOngoing = true;
        return false;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        setGestureHandled();
        boolean z = false;
        mIsScrollingDown = f2 > 0.0f;
        if (f2 < 0.0f) {
            z = true;
        }
        mIsScrollingUp = z;
        mScroller.scrollBy(f2);
        setViewsTranslationY();
        return true;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        setGestureHandled();
        mScroller.fling((int) f2);
        setViewsTranslationY();
        return true;
    }

    private void setGestureHandled() {
        if (!mIsGestureHandled) {
            mCancelPanelDispatch = true;
            mIsGestureHandled = true;
        }
    }

    public void onLongPress(MotionEvent motionEvent) {
        setGestureHandled();
    }

    public int getRestingPanelHeight() {
        return mRestingPanelHeight;
    }
}