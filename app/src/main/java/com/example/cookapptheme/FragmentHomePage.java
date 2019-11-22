package com.example.cookapptheme;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FragmentHomePage extends Fragment {

    String URL_GET_ALL_RECIPE = "http://10.30.50.118/recipeapp/getAllRecipe.php";

    ListView listViewAllRecipe;
    ArrayList<AllRecipeByUser> arrayAllRecipe;
    HomePageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        listViewAllRecipe = view.findViewById(R.id.listViewAllRecipe);

        arrayAllRecipe = new ArrayList<>();
        adapter = new HomePageAdapter(getActivity(), R.layout.recipe_layout, arrayAllRecipe);

        listViewAllRecipe.setTextFilterEnabled(true);
        listViewAllRecipe.setAdapter(adapter);

        getAllRecipe();

        return view;
    }

    private void getAllRecipe() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_GET_ALL_RECIPE, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("FragmentHomePage", response.toString());
//                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        arrayAllRecipe.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                arrayAllRecipe.add(new AllRecipeByUser(
                                        jsonObject.getString("user_name"),
                                        jsonObject.getString("user_image"),
                                        jsonObject.getString("recipe_name"),
                                        jsonObject.getString("recipe_image"),
                                        jsonObject.getString("recipe_summary"),
                                        jsonObject.getString("recipe_date_time")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
