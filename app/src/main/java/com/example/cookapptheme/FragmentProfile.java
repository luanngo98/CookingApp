package com.example.cookapptheme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class FragmentProfile extends Fragment {

    ImageView imgViewUser;
    TextView txtName, txtMyPage, txtAccSetting, txtTerms, txtFeedback, txtAbout;
    Button btnLogOut;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        sessionManager = new SessionManager(getContext());

        // Để ánh xạ trong Fragment thì cần khai báo biến View
        View view = inflater.inflate(R.layout.fragment_profile, container, false); // trả về layout chứa giao diện của fragment Profile

        imgViewUser = view.findViewById(R.id.profile_image);
        txtName = view.findViewById(R.id.textViewUserName);
        txtMyPage = view.findViewById(R.id.textViewMyPage);
        txtAccSetting = view.findViewById(R.id.textViewAccountSetting);
        txtTerms = view.findViewById(R.id.textViewTerms);
        txtFeedback = view.findViewById(R.id.textViewFeedback);
        txtAbout = view.findViewById(R.id.textViewAbout);
        btnLogOut = view.findViewById(R.id.buttonLogOut);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String nameSession = user.get(sessionManager.NAME);
        String imageSession = user.get(sessionManager.IMAGE);

        txtName.setText(nameSession);
//        Picasso.get().load(imageSession).into(imgViewUser);

        if(imageSession.equalsIgnoreCase("null")){
            imgViewUser.setImageResource(R.drawable.ic_launcher_background);
        }
        else{
//            byte[] b = Base64.decode(imageSession, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//            imgViewUser.setImageBitmap(bitmap);

            Picasso.get().load(imageSession).into(imgViewUser);
        }

        // My Page
        txtMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyPageActivity.class));
            }
        });

        // Account Setting
        txtAccSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
//                intent.putExtra("idUser", id);
//                intent.putExtra("nameUser", name);
//                intent.putExtra("emailUser", email);
                startActivity(intent);

            }
        });

        // Logout
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                sessionManager.logOut();
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String nameSession = user.get(sessionManager.NAME);
        String imageSession = user.get(sessionManager.IMAGE);

        txtName.setText(nameSession);
//        Picasso.get().load(imageSession).into(imgViewUser);
        if(imageSession.equalsIgnoreCase("null")){
            imgViewUser.setImageResource(R.drawable.ic_launcher_background);
        }
        else{
//            byte[] b = Base64.decode(imageSession, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//            imgViewUser.setImageBitmap(bitmap);

            Picasso.get().load(imageSession).into(imgViewUser);
        }
    }
}
