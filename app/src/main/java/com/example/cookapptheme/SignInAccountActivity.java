package com.example.cookapptheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInAccountActivity extends AppCompatActivity {

    View decorView;

    EditText edtEmailSignIn, edtPasswordSignIn;
    TextView btnSignIn;

    String urlLogin = "http://10.30.50.118/recipeapp/user/loginNhap.php"; //192.168.1.108

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_account);

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

        getSupportActionBar().setTitle("Sign in");
        // Back Arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);

        AnhXa();


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailSignIn = edtEmailSignIn.getText().toString().trim();
                String passwordSignIn = edtPasswordSignIn.getText().toString().trim();

                if (emailSignIn.isEmpty() && passwordSignIn.isEmpty()) {
                    edtEmailSignIn.setError("Please insert Email");
                    edtPasswordSignIn.setError("Please insert Password");
                }
                else if (passwordSignIn.isEmpty()){
                    edtPasswordSignIn.setError("Please insert Password");
                }
                else if(emailSignIn.isEmpty()){
                    edtEmailSignIn.setError("Please insert Email");
                }
                else {
                    if(emailSignIn.matches(emailPattern)){
                        SignInAccount(urlLogin);
                    }
                    else{
                        Toast.makeText(SignInAccountActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        tbShowPass.setVisibility(View.GONE);
//        edtPasswordSignIn.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//
//        edtPasswordSignIn.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(edtPasswordSignIn.getText().length()>0){
//                    tbShowPass.setVisibility(View.VISIBLE);
//                }else {
//                    tbShowPass.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        tbShowPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(tbShowPass.getText() == "SHOW"){
//                    tbShowPass.setText("HIDE");
//                    edtPasswordSignIn.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    edtPasswordSignIn.setSelection(edtPasswordSignIn.length());
//                }else {
//                    tbShowPass.setText("SHOW");
//                    edtPasswordSignIn.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                    edtPasswordSignIn.setSelection(edtPasswordSignIn.length());
//                }
//            }
//        });
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

    private void SignInAccount(String url) {

        // tạo dialog chờ xử lí
        final ProgressDialog dialog = new ProgressDialog(SignInAccountActivity.this);
        dialog.setMessage("Wait while loading...");
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        if(response.equals("<br>Login successful</br>")){
//                            // bỏ dialog
//                            dialog.dismiss();
//
//                            // nếu response = chuỗi "Success" trên PHP
//                            Toast.makeText(SignInAccountActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(SignInAccountActivity.this, FragmentActivity.class);
//
//                            startActivity(intent);
//                            finish();
//                        }else {
//
//                            dialog.dismiss();
//
//                            Toast.makeText(SignInAccountActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
//                            Log.d("AAA", "Lỗi: \n" + response);
//                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {
                                // bỏ dialog
                                dialog.dismiss();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("ID").trim();
                                    String name = object.getString("Name").trim();
                                    String email = object.getString("Email").trim();
                                    String image = object.getString("Image").trim();

                                    sessionManager.createSession(id, name, email, image);

//                                    Toast.makeText(SignInAccountActivity.this, "Login Successful "
//                                                    + "\nID: " + id
//                                                    + "\nYour name: " + name
//                                                    + "\nEmail: " + email
//                                                    + "\nImage: " + image,
//                                            Toast.LENGTH_SHORT).show();

                                    Toast.makeText(SignInAccountActivity.this, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignInAccountActivity.this, FragmentActivity.class);
//                                    intent.putExtra("id", id);
//                                    intent.putExtra("name", name);
//                                    intent.putExtra("email", email);

                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                dialog.dismiss();

                                Toast.makeText(SignInAccountActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                                Log.d("AAA", "Lỗi: \n" + response);

                            }
                        } catch (JSONException e) {
                            Log.d("AAA", "Lỗi: \n" + e);
                            dialog.dismiss();
                            Toast.makeText(SignInAccountActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(SignInAccountActivity.this, "Error system: " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "Lỗi: \n" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("emailLogin", edtEmailSignIn.getText().toString().trim());
                params.put("passwordLogin", edtPasswordSignIn.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void AnhXa() {
        edtEmailSignIn = (EditText) findViewById(R.id.editTextSignInEmail);
        edtPasswordSignIn = (EditText) findViewById(R.id.editTextSignInPassword);
        btnSignIn = findViewById(R.id.buttonSignIn);
    }
}
