package ir.timurid.smarttask.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;

public class KeyboardHelper {

    public static void showKeyboard(Context context, View view) {
        if (view.requestFocus()){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void setSelectionAtEndEditText(TextInputEditText inputEditText) {
        if (inputEditText == null)
            return;

        int len = inputEditText.length();
        inputEditText.setSelection(len);
    }

}
