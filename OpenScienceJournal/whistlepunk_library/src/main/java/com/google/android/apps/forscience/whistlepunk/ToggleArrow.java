/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.google.android.apps.forscience.whistlepunk;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.widget.ImageButton;

public class ToggleArrow extends ImageButton {
    private int mStringToBecomeActive;
    private int mStringToBecomeInactive;
    private String mName;
    private boolean mIsFocusable = true;

    public ToggleArrow(Context context) {
        super(context);
        init();
    }

    public ToggleArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToggleArrow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ToggleArrow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more_white_24dp));
    }

    @Nullable
    public TouchDelegate makeTouchDelegate() {
        Resources resources = getResources();
        Rect delegateArea = new Rect();
        getHitRect(delegateArea);
        delegateArea.left += resources.getDimensionPixelSize(R.dimen.toggle_btn_delegate_add);
        delegateArea.right += resources.getDimensionPixelOffset(R.dimen.toggle_btn_delegate_add);
        return new TouchDelegate(delegateArea, this);
    }

    public void setIsFocusable(boolean focusable) {
        mIsFocusable = focusable;
    }

    /**
     * Sets the action strings for the toggle arrow.
     * @param stringToBecomeActive The ID of the string to use when user action activates us.
     * @param stringToBecomeInactive The ID of the string to use when user action deactivates us.
     */
    public void setActionStrings(int stringToBecomeActive, int stringToBecomeInactive) {
        mStringToBecomeActive = stringToBecomeActive;
        mStringToBecomeInactive = stringToBecomeInactive;
    }

    /**
     * Sets the action strings of the toggle arrow and a name string which is used to format those
     * action strings.
     * @param stringToBecomeActive The ID of the string to use when user action activates us,
     *                             and which contains a string formatting param for our name.
     * @param stringToBecomeInactive The ID of the string to use when user action deactivates us,
     *                               and which contains a string formatting param for our name.
     * @param name The name of the thing to activate / deactivate with this toggle arrow.
     */
    public void setActionStrings(int stringToBecomeActive, int stringToBecomeInactive,
            String name) {
        setActionStrings(stringToBecomeActive, stringToBecomeInactive);
        mName = name;
    }

    public void setActive(boolean isBecomingActive, boolean animate) {
        if (mIsFocusable) {
            Resources resources = getResources();
            int stringId = isBecomingActive ? mStringToBecomeInactive : mStringToBecomeActive;
            // If it has a name field, then setActionStrings was called with the name param, and we
            // can expect those strings to be string-formatting strings that take the name.
            if (!TextUtils.isEmpty(mName)) {
                setContentDescription(resources.getString(stringId, mName));
            } else {
                setContentDescription(resources.getString(stringId));
            }
        } else {
            setContentDescription("");
        }
        float desiredRotation = isBecomingActive ? 0 : -180;
        if (animate) {
            animate()
                    .rotation(desiredRotation)
                    .setDuration(SensorCardPresenter.ANIMATION_TIME_MS)
                    .start();
        } else {
            setRotation(desiredRotation);
        }
    }
}
