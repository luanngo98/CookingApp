package com.example.cookapptheme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class FragmentActivity extends AppCompatActivity {

    View decorView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i == 0) { //visibility == 0
                    decorView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHomePage()).commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

//            Intent intent = getIntent();
//            Integer extraID = intent.getIntExtra("id", 123);
//            String extraName = intent.getStringExtra("name");
//            String extraEmail = intent.getStringExtra("email");

            HashMap<String, String> user = sessionManager.getUserDetail();
            int id = Integer.parseInt(user.get(sessionManager.ID));
            String name = user.get(sessionManager.NAME);
            String email = user.get(sessionManager.EMAIL);
            String image = user.get(sessionManager.IMAGE);

//            Bundle bundle = new Bundle();
//            bundle.putInt("idUser", extraID);
//            bundle.putString("nameUser", extraName);
//            bundle.putString("emailUser", extraEmail);

//            bundle.putInt("idUser", id);
//            bundle.putString("nameUser", name);
//            bundle.putString("emailUser", email);

            // để add fragment -> FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            // để tương tác với fragment -> FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = null;

            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    fragment = new FragmentHomePage();

                    break;
                case R.id.nav_bookmark:
                    fragment = new FragmentBookMark();
                    break;
                case R.id.nav_add:
                    fragment = new FragmentAddRecipe();
                    break;
                case R.id.nav_chat:
                    fragment = new FragmentChat();
                    break;
                case R.id.nav_profile:
                    fragment = new FragmentProfile();
                    // truyền dữ liệu
//                    fragment.setArguments(bundle); // truyền vào Bundle
                    break;
            }
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            return true;
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hideSystemBar());
        }
    }

    private int hideSystemBar(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
}
