package com.example.pruebatecnica.utils.shared;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class IdUtility {

    static final String ID = "ID";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setId(Context ctx, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(ID, "" + value);
        editor.commit();
    }

    public static String getId(Context ctx) {
        return getSharedPreferences(ctx).getString(ID, "");
    }

}