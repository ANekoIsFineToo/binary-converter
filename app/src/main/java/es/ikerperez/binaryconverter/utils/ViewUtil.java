package es.ikerperez.binaryconverter.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 30/09/2016.
 */

public class ViewUtil {

    public static void openKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity, View view) {
        hideKeyboard(activity);

        view.requestFocus();
    }

    public static void setupHideKeyboard(final Activity activity,
                                         View view, @Nullable View rootView) {
        if (rootView == null) {
            rootView = view;
        }

        if (!(view instanceof EditText)) {
            final View finalRootView = rootView;
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(activity, finalRootView);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupHideKeyboard(activity, innerView, rootView);
            }
        }
    }
}
