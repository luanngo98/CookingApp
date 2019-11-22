package com.example.cookapptheme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String ID = "USER_ID";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String IMAGE = "IMAGE";




    public SessionManager(Context context) {
        this.context = context;

        // khởi tạo đối tượng kiểu SharedPreferences
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void createSession(String id, String name, String email, String image){
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(IMAGE, image);
        editor.apply();
    }

    public void RemoveByKey(String key){
        editor.remove(key);
        editor.apply();
    }

    public void EditProfile(String name){
        editor.putString(NAME, name);
        editor.apply();
    }

    public void EditImageProfile(String image){
//        editor.putString(IMAGE, "null");
        editor.remove(IMAGE);
        editor.putString(IMAGE, image);
        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLogin()){
            Intent intent = new Intent(context, SignInAccountActivity.class);
            context.startActivity(intent);
            ((FragmentActivity)context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(IMAGE, sharedPreferences.getString(IMAGE, null));

        return user;
    }

    public void logOut(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, SignInAccountActivity.class);
        context.startActivity(intent);
        ((FragmentActivity)context).finish();
    }
}
