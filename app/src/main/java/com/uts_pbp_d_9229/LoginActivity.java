package com.uts_pbp_d_9229;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.uts_pbp_d_9229.api.UserAPI;
import com.uts_pbp_d_9229.view.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private String splash;
    private TextInputEditText txtEmail, txtPass;
    private TextView tvRegister;
    private Button btnLogin;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        splash  = preference.getString("splash", null);

        setAttribut();
    }

    private void setAttribut(){
        txtEmail    = findViewById(R.id.txtEmail);
        txtPass     = findViewById(R.id.txtPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        tvRegister  = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String pass = txtPass.getText().toString();

                if(email.equals("") || pass.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Field must be filled.", Toast.LENGTH_SHORT).show();
                }else{
                    login(email, pass);
                }
            }
        });
    }

    private void login(String email, String password) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("Success"))
                    {
                        JSONObject user = (JSONObject) obj.getJSONArray("data").get(0);

                        editor.putString("isLogin", "ya");
                        editor.putString("userId", user.optString("userId"));
                        editor.putString("name", user.optString("name"));
                        editor.putString("email", user.optString("email"));
                        editor.putString("nohp", user.optString("nohp"));
                        editor.putString("address", user.optString("address"));
                        editor.putString("picture", user.optString("picture"));
                        editor.apply();
                        if(splash.equals("ya")) {
                            startActivity(new Intent(LoginActivity.this, SplashScreen.class));
                        }else{
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                    }

                    Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}