package com.extended.uploadimage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dedykuncoro.uploadimage.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button buttonChoose;
    Button buttonChoose1;
    Button buttonChoose2;
    Button buttonChoose3;
    Button buttonChoose4;
    FloatingActionButton buttonUpload;
    Toolbar toolbar;
    EditText txt_name;
    EditText txt_name1;
    EditText txt_name2;
    EditText txt_name3;

    Bitmap bitmap,bitmap1,bitmap2,bitmap3,bitmap4, decoded,decoded1,decoded2,decoded3,decoded4;
    int success;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100

    private static final String TAG = MainActivity.class.getSimpleName();

    /* 10.0.2.2 adalah IP Address localhost Emulator Android Studio. Ganti IP Address tersebut dengan
    IP Address Laptop jika di RUN di HP/Genymotion. HP/Genymotion dan Laptop harus 1 jaringan! */
    private String UPLOAD_URL = "http://192.168.43.15/android/upload_image/upload.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "ktp";
    private String KEY_IMAGE1 = "ijazah";
    private String KEY_IMAGE2 = "skck";
    private String KEY_IMAGE3 = "sks";
    private String KEY_IMAGE4 = "lainnya";
    private String KEY_NAME = "nama";
    private String KEY_NAME1 = "ttl";
    private String KEY_NAME2 = "alamat";
    private String KEY_NAME3 = "no";



    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonChoose1 = (Button) findViewById(R.id.buttonChoose1);
        buttonChoose2 = (Button) findViewById(R.id.buttonChoose2);
        buttonChoose3 = (Button) findViewById(R.id.buttonChoose3);
        buttonChoose4 = (Button) findViewById(R.id.buttonChoose4);
        buttonUpload = (FloatingActionButton) findViewById(R.id.buttonUpload);

        txt_name = (EditText) findViewById(R.id.editText);
        txt_name1 = (EditText) findViewById(R.id.editText1);
        txt_name2 = (EditText) findViewById(R.id.editText2);
        txt_name3 = (EditText) findViewById(R.id.editText3);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        buttonChoose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        buttonChoose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        buttonChoose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        buttonChoose4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                kosong();

                            } else {
                                Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_IMAGE1, getStringImage(decoded1));
                params.put(KEY_IMAGE2, getStringImage(decoded2));
                params.put(KEY_IMAGE3, getStringImage(decoded3));
                params.put(KEY_IMAGE4, getStringImage(decoded4));
                params.put(KEY_NAME, txt_name.getText().toString().trim());
                params.put(KEY_NAME1, txt_name1.getText().toString().trim());
                params.put(KEY_NAME2, txt_name2.getText().toString().trim());
                params.put(KEY_NAME3, txt_name3.getText().toString().trim());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
                setToImageView(getResizedBitmap(bitmap1, 512));
                setToImageView(getResizedBitmap(bitmap2, 512));
                setToImageView(getResizedBitmap(bitmap3, 512));
                setToImageView(getResizedBitmap(bitmap4, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kosong() {
        txt_name.setText(null);
        txt_name1.setText(null);
        txt_name2.setText(null);
        txt_name3.setText(null);

    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        decoded1 = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        decoded2 = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        decoded3 = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        decoded4 = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
