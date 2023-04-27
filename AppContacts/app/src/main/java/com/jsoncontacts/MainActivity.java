package com.jsoncontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsoncontacts.Service.ContactService;
import com.jsoncontacts.adapters.ContactsAdapter;
import com.jsoncontacts.models.Contact;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String image,firstName,lastName,phone,email;
    ContactsAdapter contactsAdapter;
    String url = "http://192.168.1.5:8082/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        boolean isItFirstRun = prefs.getBoolean("isItFirstRun", true);


       ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.CALL_PHONE
                },
                1);

        ArrayList<Contact> liste = new ArrayList<>();



        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.manage_contact);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText name = dialog.findViewById(R.id.name);
                EditText email = dialog.findViewById(R.id.email);
                EditText job = dialog.findViewById(R.id.job);
                EditText phone = dialog.findViewById(R.id.phone);
                Button create = dialog.findViewById(R.id.create);

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(name.getText().toString().trim()) || TextUtils.isEmpty(email.getText().toString().trim() )|| TextUtils.isEmpty(job.getText().toString().trim()) || TextUtils.isEmpty(phone.getText().toString().trim())) {
                            Toast.makeText(MainActivity.this, "Please fill in all the fields correctly !", Toast.LENGTH_SHORT).show();
                        } else {
                            Contact contact = new Contact();
                            contact.setFirst_name(name.getText().toString().trim().split(" ")[0]);
                            contact.setLast_name(name.getText().toString().trim().split(" ")[1]);
                            contact.setJob(job.getText().toString().trim());
                            contact.setEmail(email.getText().toString().trim());
                            contact.setPhone(phone.getText().toString().trim());
                            AddContact(contact);
                            contactsAdapter.notifyDataSetChanged();

                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        loadData();

    }


    public  void loadData(){
        Retrofit retrofit = new Retrofit.Builder( ).baseUrl(url).addConverterFactory(GsonConverterFactory.create( )).build( );
      // System.out.println(retrofit.toString());
        ContactService api = retrofit.create(ContactService.class);
       // System.out.println(api.toString());
        Call<List<Contact>> call = api.getAllContacts();
       // System.out.println(call.toString());
        ListView listView = findViewById(R.id.list);
        call.enqueue(new Callback<List<Contact>>( ) {


            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                List<Contact> data = response.body( );
                Log.d("MyActivity", "Making API call...");
                for (Contact contact:data){
                    System.out.println(" lah awthah");
                    System.out.println(contact.getFirst_name());
                }
                contactsAdapter = new ContactsAdapter( Listin(data),listView, MainActivity.this);
                listView.setAdapter(contactsAdapter);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                System.out.println(call.toString());
                System.out.println("erreur a hamid");
                Log.e("MyActivity", "API call failed", t);

            }
        });

    }

    public void AddContact(Contact contact){

        Retrofit retrofit = new Retrofit.Builder( ).baseUrl(url).addConverterFactory(GsonConverterFactory.create( )).build( );
        ContactService api = retrofit.create(ContactService.class);
        api.createContact(contact).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("data_updated", true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                Toast.makeText(getApplicationContext(), "Inserted Successfully.... ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error.... " , Toast.LENGTH_SHORT).show();

            }
        });
    }
    ArrayList Listin(List<Contact> l) {
        ArrayList<Contact> maliste = new ArrayList<>( );
        for (int i = 0; i < l.size( ); i++) {
            Contact c=new Contact(l.get(i));
            maliste.add(c);
        }
        return maliste;
    }

}