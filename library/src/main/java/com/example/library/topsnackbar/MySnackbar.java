package com.example.library.topsnackbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.library.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by faim on 20/3/18.
 */

public class MySnackbar {

    //    Snackbar display gravity
    public static final int GRAVITY_TOP = 0;
    public static final int GRAVITY_BOTTOM = 1;
    public static final int GRAVITY_CENTER = 2;

    //    Snackbar image position
    public static final int IMAGE_LEFT = 3;
    public static final int IMAGE_RIGHT = 4;

    //    Snackbar duration
    public static final int DURATION_LENGTH_LONG = 5;
    public static final int DURATION_LENGTH_SHORT = 6;
    public static final int DURATION_LENGTH_INDEFINITE = 7;

    private static TSnackbar tSnackbar;
    private static SnackbarClickListener clickListener;
    private static View viewSnackbar;
    private static MySnackbar mySnackbar = null;
    private Edit edit = null;

    private MySnackbar() {
    }

    public static MySnackbar create(Context context, String message, @SnackbarGravityType int snackbarGravity, int snackbarDuration) {
        tSnackbar = TSnackbar.make(((Activity) context).getWindow().getDecorView().getRootView().findViewById(android.R.id.content), message, getDuration(snackbarDuration));
        viewSnackbar = tSnackbar.getView();
        setSnackbarGravityType(snackbarGravity);
        if (mySnackbar == null)
            mySnackbar = new MySnackbar();

        mySnackbar.edit().setBackgroundColor(Color.BLACK);
        mySnackbar.edit().setTextColor(Color.WHITE);
        return mySnackbar;
    }

    private static void setSnackbarGravityType(int gravity) {
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (gravity == GRAVITY_TOP) {
            layoutParams.gravity = Gravity.TOP;
        } else if (gravity == GRAVITY_BOTTOM) {
            layoutParams.gravity = Gravity.BOTTOM;
        } else if (gravity == GRAVITY_CENTER) {
            layoutParams.gravity = Gravity.CENTER;
        }
        viewSnackbar.setLayoutParams(layoutParams);
    }

    private static int getDuration(int duration) {
        if (duration == DURATION_LENGTH_INDEFINITE)
            return TSnackbar.LENGTH_INDEFINITE;
        else if (duration == DURATION_LENGTH_LONG)
            return TSnackbar.LENGTH_LONG;
        else
            return TSnackbar.LENGTH_SHORT;
    }

    public void show() {
        if (tSnackbar != null)
            tSnackbar.show();
    }

    public Edit edit() {
        if (edit == null)
            edit = new Edit();
        return edit;
    }

    @IntDef({GRAVITY_TOP, GRAVITY_BOTTOM, GRAVITY_CENTER})
    private @interface SnackbarGravityType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IMAGE_LEFT, IMAGE_RIGHT})
    private @interface ImageGravityType {
    }

    @IntDef({DURATION_LENGTH_LONG, DURATION_LENGTH_SHORT, DURATION_LENGTH_INDEFINITE})
    private @interface Duration {
    }

    public interface SnackbarClickListener {
        void onSnackbarButtonClick(View view);
    }

    public class Edit {

        private Edit() {
        }

        public Edit setBackgroundColor(int backgroundColor) {
            viewSnackbar.setBackgroundColor(backgroundColor);
            return edit;
        }

        public Edit setTextColor(int textColor) {
            TextView tvSnackbar = viewSnackbar.findViewById(R.id.snackbar_text);
            tvSnackbar.setTextColor(textColor);
            return edit;
        }

        public Edit setActionButton(String buttonName, int buttonColor, SnackbarClickListener snackbarClickListener) {
            if (snackbarClickListener != null)
                clickListener = snackbarClickListener;
            tSnackbar.setAction(buttonName, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onSnackbarButtonClick(v);
                }
            });
            if (buttonColor != -1)
                tSnackbar.setActionTextColor(buttonColor);

            return edit;
        }

        public Edit setImage(int imageId, int imageGravity) {
            if (imageGravity == IMAGE_LEFT) {
                tSnackbar.setIconLeft(imageId, 24);
            } else if (imageGravity == IMAGE_RIGHT) {
                tSnackbar.setIconRight(imageId, 24);
            }
            tSnackbar.setIconPadding(8);

            return edit;
        }

        public void show() {
            MySnackbar.this.show();
        }

    }
}
