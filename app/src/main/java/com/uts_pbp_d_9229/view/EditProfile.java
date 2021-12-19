package com.uts_pbp_d_9229.view;

import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.uts_pbp_d_9229.R;
import com.uts_pbp_d_9229.api.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private TextInputEditText txtName, txtEmail, txtHp, txtAddress;
    private Button btnSave, btnChangeFoto;
    private String selected;
    private ImageView ivProfile;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private static final int PERMISSION_CODE = 1000;
    private Bitmap bitmap;
    private Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().hide();

        setAtribut();
    }

    private void setAtribut() {
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();

        txtName         = findViewById(R.id.txtName);
        txtEmail        = findViewById(R.id.txtEmail);
        txtHp           = findViewById(R.id.txtHp);
        txtAddress      = findViewById(R.id.txtAddress);
        ivProfile       = findViewById(R.id.profile_image);
        btnChangeFoto   = findViewById(R.id.btnChangeFoto);
        btnSave         = findViewById(R.id.btnSave);

        txtName.setText(preference.getString("name", null));
        txtEmail.setText(preference.getString("email", null));
        txtAddress.setText(preference.getString("address", null));
        txtHp.setText(preference.getString("nohp", null));
        Glide.with(this)
                .load(preference.getString("picture", null))
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivProfile);

        btnChangeFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(EditProfile.this);
                View view = layoutInflater.inflate(R.layout.pilih_media, null);

                final AlertDialog alertD = new AlertDialog.Builder(view.getContext()).create();

                Button btnKamera = (Button) view.findViewById(R.id.btnKamera);
                Button btnGaleri = (Button) view.findViewById(R.id.btnGaleri);

                btnKamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="kamera";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(checkSelfPermission(Manifest.permission.CAMERA)==
                                    PackageManager.PERMISSION_DENIED ||
                                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                            PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openCamera();
                            }
                        }
                        else{
                            openCamera();
                        }
                        alertD.dismiss();
                    }
                });

                btnGaleri.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="galeri";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                    PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openGallery();
                            }
                        }
                        else{
                            openGallery();
                        }
                        alertD.dismiss();
                    }
                });

                alertD.setView(view);
                alertD.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
    }

    private void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    if (selected.equals("kamera"))
                        openCamera();
                    else
                        openGallery();
                } else {
                    Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            selectedImage = data.getData();
            try {
                InputStream inputStream =  getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            ivProfile.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
        else if(resultCode == RESULT_OK && requestCode == 2)
        {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ivProfile.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodeImage;
    }

    public void editProfile(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        String userId = preference.getString("userId", null);

        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_UPDATE + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("Success"))
                    {
                        JSONObject user = (JSONObject) obj.getJSONArray("data").get(0);

                        editor.putString("name", user.optString("name"));
                        editor.putString("nohp", user.optString("nohp"));
                        editor.putString("address", user.optString("address"));
                        editor.putString("picture", user.optString("picture"));
                        editor.apply();

                        Glide.with(EditProfile.this)
                                .load(preference.getString("picture", null))
                                .circleCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(ivProfile);
                        onBackPressed();
                    }

                    Toast.makeText(EditProfile.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", txtName.getText().toString());
                params.put("nohp", txtHp.getText().toString());
                params.put("address", txtAddress.getText().toString());

                if(bitmap != null){
                    String imageData = imageToString(bitmap);
                    params.put("picture", imageData);
                }

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}