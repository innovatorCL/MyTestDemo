package com.cn21.innovator.mytestdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

  ImageView imageView;
  Button button;
  TextView textView;

  private Handler handler = new Handler();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    imageView = findViewById(R.id.image);

    button = findViewById(R.id.button);
    textView = findViewById(R.id.text);


    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        testOkHttp();
      }
    });
  }

  private void testOkHttp() {

    //构造 OkHttpClient
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    //构造请求信息
    Request request = new Request.Builder()
            .get()  //Method GET
            .url("https://www.baidu.com")
            .build();

    okHttpClient.newCall(request)
            //发起异步请求
            .enqueue(new Callback() {
              @Override
              public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
              }

              @Override
              public void onResponse(okhttp3.Call call, Response response) throws IOException {
                //成功拿到响应
                final int code = response.code();
                Log.i("TAG","响应码："+code);
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                  Log.i("TAG","响应头部，"+responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                ResponseBody body = response.body();
                final String string = body.string();
                Log.i("TAG","响应实体，"+ string);
                handler.post(new Runnable() {
                  @Override
                  public void run() {
                    textView.setText("code: "+code+",body: "+string);
                  }
                });

              }
            });

  }
}

