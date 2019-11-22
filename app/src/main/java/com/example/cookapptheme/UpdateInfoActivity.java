package com.example.cookapptheme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateInfoActivity extends AppCompatActivity {

    private final String URL_READ = "http://10.30.50.118/recipeapp/user/readDetail.php"; //192.168.1.108
    private final String URL_UPDATE = "http://10.30.50.118/recipeapp/user/updateInfoUser.php";
    private final String URL_UPLOAD = "http://10.30.50.118/recipeapp/user/uploadImage.php";

    private static final String TAG = UpdateInfoActivity.class.getSimpleName(); //getting the info

    View decorView;

    Button btnUpdate;
    EditText edtName, edtEmail;
    ImageButton imgBtnFolder, imgBtnCamera;
    CircleImageView imgProfile;

    String getID;

    final int REQUEST_CODE_FOLDER = 123;
    private Bitmap bitmap;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i == 0) { //visibility == 0
                    decorView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });

        btnUpdate = findViewById(R.id.buttonUpdateInfo);
        edtName = findViewById(R.id.editTextName);
        edtEmail = findViewById(R.id.editTextEmail);
        imgBtnFolder = findViewById(R.id.imageButtonFolder);
        imgProfile = findViewById(R.id.profile_image);

        sessionManager = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Update information");
        // Back Arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(sessionManager.ID);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                if (name.equals("")) {
                    edtName.setError("Input value");
                } else {
                    UpdateInfo(URL_UPDATE);
                }
            }
        });

        imgBtnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();

//                Intent intent = new Intent(Intent.ACTION_PICK);
////                intent.setType("image/*");
////                startActivityForResult(intent, REQUEST_CODE_FOLDER);
////
////                ActivityCompat.requestPermissions(
////                        UpdateInfoActivity.this,
////                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
////                        REQUEST_CODE_FOLDER
////                );
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

    private void getDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String image = object.getString("image").trim();

                                    edtName.setText(name);
                                    edtEmail.setText(email);

//                                    sessionManager.EditProfile(name);


                                    // Convert image from String format back into Bitmap
                                    if(image.equalsIgnoreCase("null")){
                                        imgProfile.setImageResource(R.drawable.ic_launcher_background);
                                    }
                                    else{
//                                        byte[] b = Base64.decode(getStringImage(bitmap), Base64.DEFAULT);
//                                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//                                        imgProfile.setImageBitmap(bitmap);

                                        sessionManager.EditImageProfile(image);
                                        Picasso.get().load(image).into(imgProfile);


//                                        final String encodedString = "data:image/jpg;base64, ....";
//                                        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
//                                        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
//                                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UpdateInfoActivity.this, "Error Reading Detail " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateInfoActivity.this, "Error Reading Detail " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getID);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetail();

    }

    private void UpdateInfo(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Success")) {
                            sessionManager.EditProfile(edtName.getText().toString().trim());
                            Toast.makeText(UpdateInfoActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
//                            finish();
                        } else {
                            Toast.makeText(UpdateInfoActivity.this, "Error" + response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateInfoActivity.this, "Error system: " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idUpdate", getID);
                params.put("nameUpdate", edtName.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQUEST_CODE_FOLDER);


//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, REQUEST_CODE_FOLDER);

        ActivityCompat.requestPermissions(
                UpdateInfoActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_FOLDER
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            // Lấy/  Đọc dữ liệu trong đường dẫn để đổ hình ra
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            try {
//                InputStream inputStream = getContentResolver().openInputStream(uri);
//
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                imgProfile.setImageBitmap(bitmap);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            UploadPicture(getID, getStringImage(bitmap));
        }
    }

    private void UploadPicture(final String id, final String image) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, "[" + response + "]");
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

//                                byte[] b = Base64.decode(image, Base64.DEFAULT);
//                                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//                                imgProfile.setImageBitmap(bitmap);

//                                String image = jsonObject.getString("image").trim();
//                                sessionManager.EditImageProfile(image);

                                getDetail();
                                Toast.makeText(UpdateInfoActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(UpdateInfoActivity.this, "Try again! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateInfoActivity.this, "Try again! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("imageProfile", image);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // chuyển Bitmap sang String
    public String getStringImage(Bitmap bitmap) {

        // chuyển về kiểu mảng byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Bitmap.CompressFormat: định dạng hình ảnh, quality: chất lượng hình ảnh
        // chứa dữ liệu
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        //Sử dụng mã hóa nhị phân Base64 để chuyển sang dạng String
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT); //encodeToString: mã hóa sang kiểu String

        return encodeImage;
    }
}
