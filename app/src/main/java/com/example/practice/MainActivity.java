 package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.Model.Logininfo;
import com.example.practice.Remote.MyAPI;
import com.example.practice.Remote.RetrofitClient;

import dmax.dialog.SpotsDialog;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

 public class MainActivity extends AppCompatActivity {

    MyAPI myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    EditText edit_user, edit_pass;
    TextView tv_register;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAPI = RetrofitClient.getInstance().create(MyAPI.class);

        edit_user = findViewById(R.id.edit_user_name);
        edit_pass = findViewById(R.id.edit_password);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tvRegister);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new SpotsDialog.Builder()
                        .setContext(MainActivity.this)
                        .build();
                //dialog.show();

                Logininfo user = new Logininfo(edit_user.getText().toString(), edit_pass.getText().toString(), "", "");

                compositeDisposable.add(myAPI.loginUser(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Throwable {
                            Log.e("string s ->", s);
                              if(!s.contains("wrong") && !s.contains("exist")){
                                  Intent i = new Intent(MainActivity.this, StoryActivity.class);
                                  i.putExtra("username", edit_user.getText().toString());
                                  i.putExtra("password", edit_pass.getText().toString());
                                  startActivity(i);
                                  Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                  Log.e("string s ->", s);
                            }
                            else{
                                if(s.contains("wrong"))
                                    Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(MainActivity.this, "Wrong UserName", Toast.LENGTH_SHORT).show();
                            }
                           // dialog.dismiss();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Throwable {
                            //dialog.dismiss();
                            Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }));
            }
        });

        Log.e("TAG", "Message");
    }

     @Override
     protected void onStop() {
         super.onStop();
         compositeDisposable.clear();
     }
 }
