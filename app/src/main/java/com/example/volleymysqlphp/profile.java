package com.example.volleymysqlphp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class profile extends AppCompatActivity {
    TextView tvPrEmail,tvPrUser;
    Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(!SharedPrefManager.getInstance(this).userLoggedIn())
        {
            finish();
            startActivity(new Intent(this,com.example.volleymysqlphp.MainActivity.class));

        }

        tvPrEmail=findViewById(R.id.tvPrEmail);
        tvPrUser=findViewById(R.id.tvPrUser);
        btnLogout=findViewById(R.id.btnLogout);
        String username=SharedPrefManager.getInstance(getApplicationContext()).getUsername();
        String email=SharedPrefManager.getInstance(getApplicationContext()).getEmail();
        tvPrEmail.setText(email);
        tvPrUser.setText(username);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getApplicationContext()).Logout();
                finish();
                startActivity(new Intent(getApplicationContext(),com.example.volleymysqlphp.MainActivity.class));
            }
        });


    }
}
