package com.example.cookapptheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpAccountActivity extends AppCompatActivity {

    View decorView; // hide system bar and navigation system bar

    EditText edtNameSignUp, edtEmailSignUp, edtPasswordSignUp;
    TextView btnSignUp;

    String urlSignUp = "http://192.168.1.108/recipeapp/user/registerAccount.php"; //192.168.1.108
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "^.{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_account);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i == 0) { //visibility == 0
                    decorView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Sign up");
        // Back Arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        AnhXa();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SignUpAccountActivity.this, "aaaa", Toast.LENGTH_SHORT).show();
                String nameSignUp =edtNameSignUp.getText().toString().trim();
                String emailSignUp = edtEmailSignUp.getText().toString().trim();
                String passwordSignUp = edtPasswordSignUp.getText().toString().trim();

                if(nameSignUp.isEmpty() && emailSignUp.isEmpty() && passwordSignUp.isEmpty()){
//                    Toast.makeText(SignUpAccountActivity.this, "Please input all fields", Toast.LENGTH_SHORT).show();

                    edtNameSignUp.setError("Please insert Name");
                    edtEmailSignUp.setError("Please insert Email");
                    edtPasswordSignUp.setError("Please insert Password");
                }
                else if (nameSignUp.isEmpty()){
                    edtNameSignUp.setError("Please insert Name");
                }
                else if (emailSignUp.isEmpty()){
                    edtEmailSignUp.setError("Please insert Email");
                }
                else if (passwordSignUp.isEmpty()){
                    edtPasswordSignUp.setError("Please insert Password");
                }
                else {
                    if(emailSignUp.matches(emailPattern) && passwordSignUp.matches(passwordPattern)){
                        SignUpAccount(urlSignUp);
                    }
                    else if(!passwordSignUp.matches(passwordPattern)){
                        Toast.makeText(SignUpAccountActivity.this, "Password length is at least 8 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(SignUpAccountActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

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

    private void SignUpAccount(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Success")){ // nếu response = chuỗi "Success" trên PHP
                            Toast.makeText(SignUpAccountActivity.this, "Register successful", Toast.LENGTH_SHORT).show();

//                            Intent intent = new Intent(SignUpAccountActivity.this, FragmentActivity.class);
//                            intent.putExtra("name", edtNameSignUp.getText().toString().trim());
//                            intent.putExtra("email", edtNameSignUp.getText().toString().trim());
//                            startActivity(intent);

                            finish();

                        } else if(response.equals("Email already exists")){
                            Toast.makeText(SignUpAccountActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("AAA", "Lỗi: \n" + response);
                            Toast.makeText(SignUpAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpAccountActivity.this, "Error system!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Lỗi: \n" + error);
                    }
                }){// đẩy dữ liệu lên database

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("nameUser", edtNameSignUp.getText().toString().trim());
                params.put("emailUser", edtEmailSignUp.getText().toString().trim());
                params.put("passwordUser", edtPasswordSignUp.getText().toString().trim());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void AnhXa(){
        edtNameSignUp = (EditText) findViewById(R.id.editTextSignUpName);
        edtEmailSignUp = (EditText) findViewById(R.id.editTextSignInEmail);
        edtPasswordSignUp = (EditText) findViewById(R.id.editTextSignUpPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
    }
}
