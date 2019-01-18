package com.example.library.util;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;


public class KeyboardUtility {

    public static void eventOpenClose(final Context context, final View view, final OnKeyboardChangeEventListener onKeyboardChangeEventListener) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (onKeyboardChangeEventListener != null) {
                    Rect rect = new Rect();
                    view.getWindowVisibleDisplayFrame(rect);
                    int screenHeight = view.getRootView().getHeight();
                    int keypadHeight = screenHeight - rect.bottom;

                    if (keypadHeight > screenHeight * 0.15) {
                        onKeyboardChangeEventListener.onKeyboardChange(true);
                    } else {
                        onKeyboardChangeEventListener.onKeyboardChange(false);
                    }
                }
            }
        });
    }

    interface OnKeyboardChangeEventListener {
        void onKeyboardChange(boolean isVisible);
    }
}
