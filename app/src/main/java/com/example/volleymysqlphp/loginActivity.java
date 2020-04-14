package com.example.volleymysqlphp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class loginActivity extends AppCompatActivity {

    EditText etUserNameLg,etPasswordLg;
    Button btnLogin;
    TextView tvCredentials;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SharedPrefManager.getInstance(this).userLoggedIn())
        {
            finish();
            startActivity(new Intent(this,com.example.volleymysqlphp.profile.class));

        }

        etUserNameLg=findViewById(R.id.etUsernameLg);
        etPasswordLg=findViewById(R.id.etPasswordLg);
        btnLogin=findViewById(R.id.btnLogin);
        tvCredentials=findViewById(R.id.tvCredentials);
        progressDialog=new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username=etUserNameLg.getText().toString().trim();
                final String password=etPasswordLg.getText().toString().trim();
                progressDialog.setMessage("Signing you in....");
                progressDialog.show();
                StringRequest request= new StringRequest(Request.Method.POST, constants.USER_LOGIN,
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            if(object.getBoolean("error"))
                            {
                                progressDialog.dismiss();
                                Toast.makeText(loginActivity.this, object.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                SharedPrefManager.getInstance(getApplicationContext()).
                                        userLogin(object.getString("email"),object.getString("username"));
                                startActivity(new Intent(getApplicationContext(),com.example.volleymysqlphp.profile.class));
                                loginActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(loginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map=new HashMap<>();
                        map.put("username",username);
                        map.put("password",password);
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

                MySingleton.getInstance(loginActivity.this).addToRequestQueue(request);
            }
        });

    }
}
