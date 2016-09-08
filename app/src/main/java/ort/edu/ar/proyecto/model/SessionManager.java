package ort.edu.ar.proyecto.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ort.edu.ar.proyecto.Login;
import ort.edu.ar.proyecto.MainActivity;

/**
 * Created by 41400475 on 5/8/2016.
 */
public class SessionManager implements Serializable{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_PASSWORD = "contraseña";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID = "id";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences("MyPrefs", PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String contraseña, String email, String id){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_PASSWORD, contraseña);
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_ID, id);
        editor.commit();
    }

    public int checkLogin() {
        int respuesta;
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in
            respuesta = 0;
        } else { respuesta = 1; }
        return respuesta;
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public Map<Integer, String[]> getUserDetails(){
        Map<Integer, String[]> userMap = new HashMap<Integer, String[]>();
        String[] userArray = new String[]{pref.getString(KEY_EMAIL, null), pref.getString(KEY_PASSWORD, null), pref.getString(KEY_ID, null)};
        userMap.put(100, userArray);

        return userMap;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
}
