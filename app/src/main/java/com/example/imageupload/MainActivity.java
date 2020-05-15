package com.example.imageupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int IMG_REQUEST =777 ;

    Button chosebtn,uploadbtn;
    EditText img_title;
    ImageView imageView;
    Bitmap bitmap;
    JsonPlaceApi jsonPlaceApi;

    LinearLayout linearLayout;

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelayout);


//        img_title=(EditText)findViewById(R.id.imagetitleid);
//        imageView=(ImageView)findViewById(R.id.imageid);
//        chosebtn=(Button) findViewById(R.id.chosebynid);
//        uploadbtn=(Button)findViewById(R.id.uploadimgbtnid);
//        chosebtn.setOnClickListener(this);
//        uploadbtn.setOnClickListener(this);

        linearLayout=(LinearLayout)findViewById(R.id.linearlayoutid);

        tableLayout=new TableLayout(this);

        displaytable();




    }

    //dynamic view adding


    public  void displaytable(){

        tableLayout.setLayoutParams(new TableLayout.LayoutParams
                (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));

        for(int count=0;count<20;count++) {

            TableRow row=new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams
                    (TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

            TextView textView=new TextView(this);
            textView.setText("test:"+count);

            textView.setLayoutParams(new TableRow.LayoutParams
                    (TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
            TextView textView2=new TextView(this);
            textView2.setText("test:"+count);

            textView2.setLayoutParams(new TableRow.LayoutParams
                    (TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

            row.addView(textView,0);
            row.addView(textView2,1);
            tableLayout.addView(row);

        }

        linearLayout.addView(tableLayout);


    }














    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.chosebynid:
                selectImage();
                break;

            case R.id.uploadimgbtnid:
                uploadImage();
                break;
        }

    }

    private  void selectImage(){

        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                img_title.setVisibility(View.VISIBLE);
                uploadbtn.setEnabled(true);
                chosebtn.setEnabled(false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private  String imageToString(){

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        Bitmap bitmap = null;
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);


    }




    private  void uploadImage(){
        // Bitmap image=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //  imageView.getDrawable();
      //  String Image= imageToString();
        String Title= img_title.getText().toString();

//        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://127.0.0.1/Android")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        jsonPlaceApi=ApiClient.getApiClient().create(JsonPlaceApi.class);

        Call<ImageClass> call= jsonPlaceApi.uploadImage(Title);
        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {

                ImageClass imageClass=response.body();
                Toast.makeText(getApplicationContext(),"server response:"+imageClass.getResponse(),Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }


}
