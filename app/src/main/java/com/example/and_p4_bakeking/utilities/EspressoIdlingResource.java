package com.example.and_p4_bakeking.utilities;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class EspressoIdlingResource implements IdlingResource {

    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Nullable private volatile ResourceCallback mCallback;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;

    }

    public void setIdleState(boolean idleState) {
        mIsIdleNow.set((idleState));
        if(idleState && mCallback !=null){
            mCallback.onTransitionToIdle();
        }
    }
}
