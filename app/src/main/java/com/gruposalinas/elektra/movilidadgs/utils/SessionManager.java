package com.gruposalinas.elektra.movilidadgs.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by oemy9 on 13/03/2017.
 */

public class SessionManager {
    private Context ctx;
    public SessionManager(Context ctx) {
        this.ctx = ctx;
    }

    public void add(String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void add(String key, int value) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void add(String key, Boolean value) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean get(String key) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public int getInt(String key) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    public String getString(String key) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

}
