package com.uts_pbp_d_9229.view;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.uts_pbp_d_9229.R;
import com.uts_pbp_d_9229.api.CategoryAPI;
import com.uts_pbp_d_9229.api.CityAPI;
import com.uts_pbp_d_9229.api.DestinationAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostFragment extends Fragment {

    private ArrayList<String> categories = new ArrayList<>(), cities = new ArrayList<>();
    private ArrayAdapter<String> adapterCategory, adapterCity;
    private View view;
    private AutoCompleteTextView spinnerCategory, spinnerCity;
    private LinearLayout btnKamera;
    private ImageView ivPicture;
    private Button btnPost;
    private TextInputEditText txtName, txtLocation, txtDetail;
    private String selectedCategory, selectedCity, selected;
    private static final int PERMISSION_CODE = 1000;
    private Bitmap bitmap;
    private Uri selectedImage = null;
    private SharedPreferences preference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);

        setAtribut();
        getCategories();
        getCities();

        return view;
    }

    private void setAtribut() {
        preference = PreferenceManager.getDefaultSharedPreferences(view.getContext());

        spinnerCategory = view.findViewById(R.id.spinner_category);
        spinnerCity     = view.findViewById(R.id.spinner_city);
        btnKamera       = view.findViewById(R.id.iv_galery);
        ivPicture       = view.findViewById(R.id.iv_picture);
        txtName         = view.findViewById(R.id.txtName);
        txtLocation     = view.findViewById(R.id.txtLocation);
        txtDetail       = view.findViewById(R.id.txtDetail);
        btnPost         = view.findViewById(R.id.btnPost);

        txtName.setText("");
        txtLocation.setText("");
        txtDetail.setText("");

        adapterCategory = new ArrayAdapter<>(view.getContext(), R.layout.dropdown_menu_popup_item, categories);
        spinnerCategory.setAdapter(adapterCategory);

        adapterCity = new ArrayAdapter<>(view.getContext(), R.layout.dropdown_menu_popup_item, cities);
        spinnerCity.setAdapter(adapterCity);

        btnKamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View view = layoutInflater.inflate(R.layout.pilih_media, null);

                final AlertDialog alertD = new AlertDialog.Builder(view.getContext()).create();

                Button btnKamera = (Button) view.findViewById(R.id.btnKamera);
                Button btnGaleri = (Button) view.findViewById(R.id.btnGaleri);

                btnKamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="kamera";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)==
                                    PackageManager.PERMISSION_DENIED ||
                                    getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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
                            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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

        spinnerCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (String) parent.getItemAtPosition(position);
            }
        });

        spinnerCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = (String) parent.getItemAtPosition(position);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
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
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    if(selected.equals("kamera"))
                        openCamera();
                    else
                        openGallery();
                }else{
                    Toast.makeText(getContext() ,"Permision denied",Toast.LENGTH_SHORT).show();
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
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            ivPicture.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
        else if(resultCode == RESULT_OK && requestCode == 2)
        {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ivPicture.setImageBitmap(bitmap);
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
        bitmap.compress(Bitmap.CompressFormat.JPEG,20, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodeImage;
    }

    public void getCategories() {
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CategoryAPI.URL_GET_ALL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!categories.isEmpty())
                        categories.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id          = Integer.parseInt(jsonObject.optString("id"));
                        String name     = jsonObject.optString("name");

                        categories.add(name);
                    }
                    adapterCategory.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(stringRequest);
    }

    public void getCities() {
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CityAPI.URL_GET_ALL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!cities.isEmpty())
                        cities.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id          = Integer.parseInt(jsonObject.optString("id"));
                        String name     = jsonObject.optString("name");
                        String picture  = jsonObject.optString("picture");

                        cities.add(name);
                    }
                    adapterCity.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(stringRequest);
    }

    public void addPost(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, DestinationAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("Success"))
                    {

                    }

                    System.out.println(response);

                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", preference.getString("userId", null));
                params.put("city_name", selectedCity);
                params.put("category_name", selectedCategory);
                params.put("name", txtName.getText().toString());
                params.put("location", txtLocation.getText().toString());
                params.put("details", txtDetail.getText().toString());

                if(bitmap != null){
                    String imageData = imageToString(bitmap);
                    params.put("picture", imageData);
                }

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        queue.add(stringRequest);
    }


    public void callParentMethod(){
        getActivity().onBackPressed();
    }
}