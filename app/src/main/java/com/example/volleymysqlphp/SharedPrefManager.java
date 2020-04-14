package com.example.volleymysqlphp;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SharedPrefManager {
    private static SharedPrefManager instance;
    private static Context ctx;

    private static final String lOGIN_PREF="com.example.volleymysqlphp.loginPref";
    private static final String KEY_USERNAME="username";
    private static final String KEY_EMAIL="email";

    private SharedPrefManager(Context context) {
        ctx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean userLogin(String email,String username)
    {
        SharedPreferences.Editor editor= ctx.getSharedPreferences(lOGIN_PREF,Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_EMAIL,email);
        editor.commit();
        return true;

    }
    public boolean userLoggedIn()
    {
        SharedPreferences prefs= ctx.getSharedPreferences(lOGIN_PREF,Context.MODE_PRIVATE);
        if(prefs.getString(KEY_USERNAME,null)!=null)
            return true;
        return false;
    }

    public boolean Logout()
    {
        SharedPreferences.Editor editor= ctx.getSharedPreferences(lOGIN_PREF,Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        return true;
    }

    public String getUsername()
    {
        SharedPreferences prefs= ctx.getSharedPreferences(lOGIN_PREF,Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME,null);

    }

    public String getEmail()
    {
        SharedPreferences prefs= ctx.getSharedPreferences(lOGIN_PREF,Context.MODE_PRIVATE);
        return prefs.getString(KEY_EMAIL,null);
    }





}

