package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.practice.Model.Logininfo;
import com.example.practice.Remote.MyAPI;
import com.example.practice.Remote.RetrofitClient;

import dmax.dialog.SpotsDialog;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    MyAPI myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    EditText edit_user, edit_pass;
    Button btn_create;

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myAPI = RetrofitClient.getInstance().create(MyAPI.class);

        edit_user = findViewById(R.id.edit_user_name);
        edit_pass = findViewById(R.id.edit_password);
        btn_create = findViewById(R.id.btn_login);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new SpotsDialog.Builder()
                        .setContext(RegisterActivity.this)
                        .build();
                //dialog.show();

                Logininfo user = new Logininfo(edit_user.getText().toString(), edit_pass.getText().toString(), "", "");

                compositeDisposable.add(myAPI.registerUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Throwable {
                                if(s.equals("Registered Successfully!")){
                                    finish();
                                }
                                Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                                //dialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                //dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("error kya", throwable.getMessage());
                            }
                        }));
            }
        });
    }
}
