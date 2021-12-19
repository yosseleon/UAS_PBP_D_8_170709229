package com.uts_pbp_d_9229;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.uts_pbp_d_9229.dao.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText txtName, txtEmail, txtHp, txtAddress, txtPass, txtRePass;
    private TextView tvlogin;
    private Button btnRegister;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    final String passwordpattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        setAttribut();
        init();
    }

    private void setAttribut() {
        txtName     = findViewById(R.id.txtName);
        txtEmail    = findViewById(R.id.txtEmail);
        txtHp       = findViewById(R.id.txtHp);
        txtAddress  = findViewById(R.id.txtAddress);
        txtPass     = findViewById(R.id.txtPassword);
        txtRePass   = findViewById(R.id.txtPasswordUlang);
        tvlogin     = findViewById(R.id.tvlogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void init() {
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtName.getText().equals(""))
                    txtName.setError("Input must be filled !");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtEmail.getText().toString().equals(""))
                    txtEmail.setError("Input must be filled !");
                else if(!txtEmail.getText().toString().trim().matches(emailPattern))
                    txtEmail.setError("Email not valid !");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtEmail.getText().toString().equals(""))
                    txtEmail.setError("Input must be filled !");
                else if(!txtEmail.getText().toString().trim().matches(emailPattern))
                    txtEmail.setError("Email not valid !");
            }
        });

        txtHp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtHp.getText().toString().equals(""))
                    txtHp.setError("Input must be filled !");
                else if(txtHp.getText().length() < 6 || txtHp.getText().length() > 13)
                    txtHp.setError("Phone number must consist of 6-13 characters !");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtAddress.getText().toString().equals(""))
                    txtAddress.setError("Input must be filled !");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtPass.getText().toString().equals(""))
                    txtPass.setError("Input must be filled !");
                else if(txtPass.getText().toString().length() < 8)
                    txtPass.setError("Password must consist min 8 characters !");
                else if(!txtPass.getText().toString().trim().matches(passwordpattern))
                    txtPass.setError("Password must consists of uppercase letters, " +
                            "lowercase letters, numbers and special characters. !");
                else if(!txtRePass.getText().toString().equals("") &&
                        !txtPass.getText().toString().equals(txtRePass.getText().toString()))
                    txtPass.setError("Password not match !");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtRePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtRePass.getText().toString().equals(""))
                    txtRePass.setError("Input must be filled !");
                else if(txtRePass.getText().toString().length() < 8)
                    txtRePass.setError("Password must consist min 8 characters !");
                else if(!txtRePass.getText().toString().trim().matches(passwordpattern))
                    txtRePass.setError("Password must consists of uppercase letters, " +
                            "lowercase letters, numbers and special characters. !");
                else if(!txtPass.getText().toString().equals("") &&
                        !txtPass.getText().toString().equals(txtRePass.getText().toString()))
                    txtRePass.setError("Password not match !");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtName.getText().toString().equals("") && !txtEmail.getText().toString().equals("") &&
                        !txtHp.getText().toString().equals("") || !txtAddress.getText().toString().equals("") &&
                        !txtPass.getText().toString().equals("") && !txtRePass.getText().toString().equals("")){

                    register(new User("", txtName.getText().toString(), txtEmail.getText().toString(),
                            txtHp.getText().toString(), txtAddress.getText().toString(), "",
                            txtPass.getText().toString()));
                }
            }
        });
    }

    public void register(User user){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Register Account");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("Success"))
                    {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }

                    Toast.makeText(RegisterActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", user.getName());
                params.put("email", user.getEmail());
                params.put("nohp", user.getNoHp());
                params.put("address", user.getAddress());
                params.put("password", user.getPassword());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(stringRequest);
    }
}