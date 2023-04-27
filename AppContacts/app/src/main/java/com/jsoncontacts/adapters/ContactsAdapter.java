package com.jsoncontacts.adapters;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsoncontacts.MainActivity;
import com.jsoncontacts.R;
import com.jsoncontacts.models.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends BaseAdapter {
    Context context;
    ListView listView;
    ArrayList<Contact> contacts;

    public ContactsAdapter(ArrayList<Contact> elts, ListView listView, Context context) {
        this.contacts = elts;
        this.context = context;
        this.listView = listView;
    }

    public int getCount() {
        return contacts.size();
    }

    public Object getItem(int i) {
        return contacts.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Contact contact = contacts.get(i);
        View view1 = layoutInflater.inflate(R.layout.single_contact, null);
        TextView name = view1.findViewById(R.id.title);
        TextView details = view1.findViewById(R.id.description);
        Button call = view1.findViewById(R.id.call);
        Button sms = view1.findViewById(R.id.sms);

        Button delete = view1.findViewById(R.id.delete);

        Button update = view1.findViewById(R.id.update);
        call.setTag(contact);
        sms.setTag(contact);
        delete.setTag(contact);

        update.setTag(contact);

        name.setText(contact.getFirst_name() + " " + contact.getLast_name());
        details.setText(contact.getJob() + "\n" + contact.getPhone() + "\n" + contact.getEmail());

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = (Contact) view.getTag();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getPhone()));
                context.startActivity(intent);
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contact.getPhone(), null)));
            }
        });

      delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = (Contact) view.getTag();
                contacts.remove(contact);
                ContactsAdapter.this.notifyDataSetChanged();
            }
        });

       update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = (Contact) view.getTag();

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.manage_contact);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText name = dialog.findViewById(R.id.name);
                EditText email = dialog.findViewById(R.id.email);
                EditText job = dialog.findViewById(R.id.job);
                EditText phone = dialog.findViewById(R.id.phone);
                Button create = dialog.findViewById(R.id.create);

                create.setText("Update");

                name.setText(contact.getFirst_name() + " " + contact.getLast_name());
                email.setText(contact.getEmail());
                job.setText(contact.getJob());
                phone.setText(contact.getPhone());

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(name.getText().toString().trim()) || TextUtils.isEmpty(email.getText().toString().trim() )|| TextUtils.isEmpty(job.getText().toString().trim()) || TextUtils.isEmpty(phone.getText().toString().trim())) {
                            Toast.makeText(context, "Please fill in all the fields correctly !", Toast.LENGTH_SHORT).show();
                        } else {
                            contact.setFirst_name(name.getText().toString().trim().split(" ")[0]);
                            contact.setLast_name(name.getText().toString().trim().split(" ")[1]);
                            contact.setPhone(phone.getText().toString().trim());
                            contact.setEmail(email.getText().toString().trim());
                            contact.setJob(job.getText().toString().trim());

                            ContactsAdapter.this.notifyDataSetChanged();

                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
        return view1;
    }

}
