package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.practice.Model.Logininfo;
import com.example.practice.Remote.MyAPI;
import com.example.practice.Remote.RetrofitClient;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] country = { "Individual", "Govt. Organisation", "Private Organisation"};
    Button btn;
    Button story;
    MyAPI myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String name = "";
    String password = "";
    EditText link;
    List<String> LINKS = null;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Bundle b = getIntent().getExtras();
        link = findViewById(R.id.et4);


            if(b != null) {
                name = b.getString("username");
                password = b.getString("password");
            }
            myAPI = RetrofitClient.getInstance().create(MyAPI.class);
            Spinner spin = (Spinner) findViewById(R.id.spinner);
            btn = findViewById(R.id.btn);
            story = findViewById(R.id.btn_story);
            spin.setOnItemSelectedListener(this);
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(aa);



            compositeDisposable.add(myAPI.getLink()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<String>>() {
                                   @Override
                                   public void accept(List<String> strings) throws Throwable {
                                        LINKS = strings;
                                        Log.e("link aayi pls", LINKS.get(0) + " " + LINKS.size());
                                   }
                               }));

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog dialog = new SpotsDialog.Builder()
                                            .setContext(StoryActivity.this)
                                            .build();
                                    //dialog.show();
                                    String lmao = name;
                                    String pass = password;

                                    Logininfo user = new Logininfo(lmao, pass, "", link.getText().toString());

                                    compositeDisposable.add(myAPI.putUser(user)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<String>() {
                                                @Override
                                                public void accept(String s) throws Throwable {
                                                    if (s.equals("Successful")) {
                                                        finish();
                                                    }
                                                    Toast.makeText(StoryActivity.this, s, Toast.LENGTH_SHORT).show();
                                                   // dialog.dismiss();
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Throwable {
                                                   // dialog.dismiss();
                                                    Toast.makeText(StoryActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }));
                                }
                            });

                            story.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ArrayList<String> lolol = new ArrayList<>();
                                    for(int i = 0; i < LINKS.size(); i++)
                                        if(LINKS.get(i) != null) {
                                            lolol.add(LINKS.get(i));
                                            Log.e("LINKS ke " + i + " pe", LINKS.get(i));
                                        }

                                    Intent i = new Intent(StoryActivity.this, VideoActivity.class);
                                    i.putExtra("links", lolol);
                                    startActivity(i);
                                }
                            });
        }


    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
