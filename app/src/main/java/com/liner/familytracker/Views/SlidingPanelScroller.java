package com.liner.familytracker.Views;

import android.content.Context;
import android.widget.OverScroller;
import com.google.android.gms.common.api.Api;

public class SlidingPanelScroller {
    private boolean mIsGestureOngoing;
    private boolean mIsInFling;
    private boolean mIsPositionSet;
    private int mMaximumPosition = Api.BaseClientBuilder.API_PRIORITY_OTHER;
    private int mMinimumPosition = -2147483647;
    private final OverScroller mOverScroller;
    private float mPosition = 0.0f;
    private int mRestingPosition = 0;

    public SlidingPanelScroller(Context context) {
        mOverScroller = new OverScroller(context);
        mOverScroller.setFriction(0.045f);
    }

    public void setMaximumPosition(int i) {
        mMaximumPosition = i;
        mPosition = Math.min((float) mMaximumPosition, mPosition);
    }

    public void setMinimumPosition(int i) {
        mMinimumPosition = i;
        mPosition = Math.max((float) mMinimumPosition, mPosition);
    }

    public void setRestingPosition(int i) {
        mRestingPosition = i;
        if (!mIsPositionSet) {
            setPosition((float) i);
        }
    }

    public int getRestingPosition() {
        return mRestingPosition;
    }

    public boolean isInFling() {
        if (mIsInFling && mOverScroller.isFinished()) {
            mIsInFling = false;
        }
        return mIsInFling;
    }

    public void setPosition(float f) {
        mPosition = f;
        mIsPositionSet = true;
    }

    public boolean isPositionSet() {
        return mIsPositionSet;
    }

    public void scrollBy(float f) {
        mPosition -= f;
        mPosition = Math.min((float) mMaximumPosition, mPosition);
    }

    public void scrollTo(int i) {
        forceFinished(true);
        mOverScroller.startScroll(0, (int) mPosition, 0, (int) (((float) i) - mPosition));
        mIsPositionSet = true;
        mIsGestureOngoing = true;
    }

    public void fling(int i) {
        forceFinished(true);
        mOverScroller.fling(0, (int) mPosition, 0, i, 0, 0, mMinimumPosition, mMaximumPosition);
        mIsInFling = true;
        mIsGestureOngoing = true;
    }

    public void scrollToRestingPosition() {
        scrollTo(mRestingPosition);
        mIsPositionSet = true;
    }

    public void forceFinished(boolean z) {
        if (mIsGestureOngoing) {
            mPosition = (float) mOverScroller.getCurrY();
        }
        mOverScroller.forceFinished(z);
        mIsInFling = false;
        mIsGestureOngoing = false;
    }

    public boolean computeScrollOffset() {
        return mOverScroller.computeScrollOffset();
    }

    public int getCurrentPosition() {
        if (mIsGestureOngoing) {
            mPosition = (float) mOverScroller.getCurrY();
            if (mOverScroller.isFinished()) {
                mIsGestureOngoing = false;
            }
        }
        return (int) mPosition;
    }

    public boolean isOverScrolled() {
        return mPosition > ((float) mRestingPosition);
    }

    public int getOffsetFromRestingPosition() {
        return (int) (mPosition - ((float) mRestingPosition));
    }
}