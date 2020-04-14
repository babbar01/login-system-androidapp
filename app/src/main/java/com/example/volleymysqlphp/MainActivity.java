package com.example.volleymysqlphp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    Button btnCreate;
    EditText etUsername,etEmail,etPassword;
    TextView tvLogin;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).userLoggedIn())
        {
            finish();
            startActivity(new Intent(this,com.example.volleymysqlphp.profile.class));

        }
        btnCreate=findViewById(R.id.btnCreate);
        etUsername=findViewById(R.id.etUsernameLg);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        tvLogin=findViewById(R.id.tvLogin);


        progressdialog=new ProgressDialog(this);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username=etUsername.getText().toString().trim();
                final String Password=etPassword.getText().toString().trim();
                final String email=etEmail.getText().toString().trim();
                progressdialog.setMessage("Creating User....");
                progressdialog.show();
                StringRequest request=new StringRequest(Request.Method.POST, constants.USER_REGISTER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    progressdialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "hey error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map=new HashMap<>();
                        map.put("username",username);
                        map.put("password",Password);
                        map.put("email",email);
                        return map;
                    }
                };

                /// certificate error removal for exception ssl
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                } };
                SSLContext sc = null;
                try {
                    sc = SSLContext.getInstance("SSL");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,com.example.volleymysqlphp.loginActivity.class);
                startActivity(intent);
            }
        });

    }
}
