package com.example.pruebatecnica.utils.shared;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class IdAsteroidUtility {

    static final String ID_ASTEROID = "ID_ASTEROID";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setId(Context ctx, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(ID_ASTEROID, "" + value);
        editor.commit();
    }

    public static String getId(Context ctx) {
        return getSharedPreferences(ctx).getString(ID_ASTEROID, "");
    }

}