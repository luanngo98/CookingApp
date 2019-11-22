package com.example.cookapptheme;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentAddRecipe extends Fragment {

    private static final int REQUEST_CODE_FOLDER = 123;
    private static final int REQUEST_CODE_FOLDER_INSTRUCTION = 456;
    ImageView imgViewHinhMonAn, imgViewInstruction;
    TextView txtChonMonAn;
    EditText edtTenMonAn, edtMoTaMonAn, edtThemNguyenLieu, edtThemCachLam;
    ListView listViewNguyenLieu, listViewCachLam;
    Button btnThemNguyenLieu, btnThemCachLam, btnThemMonAn;
    ImageButton imgButtonInstruction;

    View view;
    SessionManager sessionManager;
    private Bitmap bitmapImageRecipe, bitmapInstruction;
    String getID;

    ArrayList<Ingredient> arrayIngredient; // tạo mảng chứa nguyên liệu
    ArrayList<Instruction> arrayInstruction;
    IngredientAdapter ingredientAdapter;
    InstructionAdapter instructionAdapter;

    String URL_ADD_RECIPE = "http://192.168.1.108/recipeapp/recipe/addRecipe.php";
    String URL_ADD_INGREDIENT = "http://192.168.1.108/recipeapp/recipe/addIngredient.php";
    String URL_ADD_INSTRUCTION = "http://192.168.1.108/recipeapp/recipe/addInstruction.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        AnhXa();

        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(sessionManager.ID);

        // set imgViewInstruction ảnh dạng bitmap
        bitmapImageRecipe = BitmapFactory.decodeResource(getResources(), R.drawable.gallery_256x256);
        imgViewHinhMonAn.setImageBitmap(bitmapImageRecipe);

        bitmapInstruction = BitmapFactory.decodeResource(getResources(), R.drawable.gallery_256x256);
        imgViewInstruction.setImageBitmap(bitmapInstruction);

        txtChonMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        // ==== THÊM INGREDIENT VÀO LIST
        // khởi tạo mảng Ingredient
        arrayIngredient = new ArrayList<>();

        // tạo Adapter Ingredient
        ingredientAdapter = new IngredientAdapter(getActivity(), R.layout.dong_nguyen_lieu, arrayIngredient);
        listViewNguyenLieu.setAdapter(ingredientAdapter);

        btnThemNguyenLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingredient = edtThemNguyenLieu.getText().toString().trim();
                if (ingredient.isEmpty()) {
                    edtThemNguyenLieu.setError("Input something here");
                } else {
                    arrayIngredient.add(new Ingredient(ingredient));
                    edtThemNguyenLieu.setText("");
                    ingredientAdapter.notifyDataSetChanged();
                }

            }
        });
        // ==============================

        // === THÊM INSTRUCTION VÀO LIST
        // khởi tạo mảng Instruction
        arrayInstruction = new ArrayList<>();

        // tạo Adapter Instruction
        instructionAdapter = new InstructionAdapter(getActivity(), R.layout.dong_cach_lam, arrayInstruction);
        listViewCachLam.setAdapter(instructionAdapter);

        btnThemCachLam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String instruction = edtThemCachLam.getText().toString().trim();

                if (instruction.isEmpty()) {
                    edtThemCachLam.setError("Input something here");
                }
                else {
                    // chuyển data imageview -> byte[]
                    // 1. lấy dữ liệu hình
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgViewInstruction.getDrawable();
                    // 2. chuyển từ BitmapDrawable sang Bitmap
                    bitmapInstruction = bitmapDrawable.getBitmap();
                    // 3. chuyển về kiểu mảng byte
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    bitmapInstruction.compress(Bitmap.CompressFormat.JPEG, 100, byteArray); // Bitmap.CompressFormat: định dạng hình ảnh, quality: chất lượng hình ảnh
                    // 4. chứa dữ liệu
                    byte[] image = byteArray.toByteArray();

                    arrayInstruction.add(new Instruction(instruction, image));

                    edtThemCachLam.setText("");

                    // set imgViewInstruction ảnh dạng bitmap
                    bitmapInstruction = BitmapFactory.decodeResource(getResources(), R.drawable.gallery_256x256);
                    imgViewInstruction.setImageBitmap(bitmapInstruction);

                    instructionAdapter.notifyDataSetChanged();
                }

            }
        });
        // =============================

        imgButtonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK); // mở bộ nhớ phần duyệt hình => Intent.ACTION_PICK
                intent.setType("image/*"); // mở nơi chứa hình
                startActivityForResult(intent, REQUEST_CODE_FOLDER_INSTRUCTION);
            }
        });

        btnThemMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Do you want check info again before add new recipe?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.setNegativeButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(edtTenMonAn.getText().toString().trim().isEmpty()
                                || arrayIngredient.isEmpty()
                                || arrayInstruction.isEmpty()){
                            edtTenMonAn.setError("Please input recipe name");
                            edtThemNguyenLieu.setError("Please input ingredient");
                            edtThemCachLam.setError("Please input instruction");
                        }
                        else {
                            AddNewRecipe(getID, getStringImage(bitmapImageRecipe));
                        }
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQUEST_CODE_FOLDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

//            try {
//                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                imgViewHinhMonAn.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            try {
                bitmapImageRecipe = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                imgViewHinhMonAn.setImageBitmap(bitmapImageRecipe);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_CODE_FOLDER_INSTRUCTION && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

//            try {
//                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                imgViewHinhMonAn.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            try {
                bitmapInstruction = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                imgViewInstruction.setImageBitmap(bitmapInstruction);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AddNewRecipe(final String id, final String image) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Adding a new recipe...");
        progressDialog.show();


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_RECIPE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            Log.i("FragmentAddRecipe", "[" + response + "]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                String recipeID = jsonObject.getString("id");
                                AddNewIngredient(recipeID);
                                AddNewInstruction(recipeID);

                                bitmapImageRecipe = BitmapFactory.decodeResource(getResources(), R.drawable.gallery_256x256);
                                imgViewHinhMonAn.setImageBitmap(bitmapImageRecipe);

                                edtTenMonAn.setText("");
                                edtMoTaMonAn.setText("");

                                arrayIngredient.clear();
                                ingredientAdapter.notifyDataSetChanged();

                                arrayInstruction.clear();
                                instructionAdapter.notifyDataSetChanged();

                                bitmapInstruction = BitmapFactory.decodeResource(getResources(), R.drawable.gallery_256x256);
                                imgViewInstruction.setImageBitmap(bitmapInstruction);

                                Toast.makeText(getContext(), "Add recipe successful!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Try again!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "System error!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", edtTenMonAn.getText().toString().trim());
                params.put("image", image);
                params.put("summary", edtMoTaMonAn.getText().toString().trim());
                params.put("user_id", id);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void AddNewIngredient(final String id) {

        for (int i = 0; i < arrayIngredient.size(); i++) {
            final String name = arrayIngredient.get(i).getIngredientName();

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_INGREDIENT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("FragmentAddRecipe", "[" + response + "]");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
//                                    Toast.makeText(getContext(), "Add ingredients successfull", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Try againt!\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Try again!\n" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("ingredient_name", name);
                    params.put("recipe_id", id);

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void AddNewInstruction(final String id) {
        for (int i = 0; i < arrayInstruction.size(); i++) {
            final String instructionName = arrayInstruction.get(i).getDescribtion();

            byte[] imageInstruction = arrayInstruction.get(i).getDescribetionImage();
            final String encodeImageInstruction = Base64.encodeToString(imageInstruction, Base64.DEFAULT);

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_INSTRUCTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("FragmentAddRecipe", "[" + response + "]");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
//                                    Toast.makeText(getContext(), "Add ingredients successfull", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Try again!\n" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Try again!\n" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name_instruction", instructionName);
                    params.put("image_instruction", encodeImageInstruction);
                    params.put("recipe_id", id);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
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

    private void AnhXa() {
        imgViewHinhMonAn = view.findViewById(R.id.imageViewRecipe);
        txtChonMonAn = view.findViewById(R.id.textViewRecipeFolder);
        edtTenMonAn = view.findViewById(R.id.editTextRecipeName);
        edtMoTaMonAn = view.findViewById(R.id.editTextSummary);
        edtThemNguyenLieu = view.findViewById(R.id.editTextAddIngredient);
        edtThemCachLam = view.findViewById(R.id.editTextAddInstruction);
        listViewNguyenLieu = view.findViewById(R.id.listViewIngredient);
        listViewCachLam = view.findViewById(R.id.listViewInstruction);
        btnThemNguyenLieu = view.findViewById(R.id.buttonAddIngredient);
        btnThemCachLam = view.findViewById(R.id.buttonAddInstruction);
        btnThemMonAn = view.findViewById(R.id.buttonAddRecipe);
        imgViewInstruction = view.findViewById(R.id.imageViewInstruction);
        imgButtonInstruction = view.findViewById(R.id.imageButtonInstruction);
    }
}
