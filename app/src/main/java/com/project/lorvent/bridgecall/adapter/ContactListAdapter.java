package com.project.lorvent.bridgecall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.lorvent.bridgecall.activity.CallActivity;
import com.project.lorvent.bridgecall.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 5/16/2017.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private Cursor cursor;
    private Context context;
    Cursor phones;
    String calling_from;

    public ContactListAdapter(Context context,String calling_from) {
        this.context = context;
        this.calling_from=calling_from;
    }
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
       // cursor.moveToNext();
        notifyDataSetChanged();
    }
    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.indi_view_contact_person, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactListAdapter.ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.name.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                holder.number.setText(phoneNumber);
                String image_uri=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (image_uri == null) {
                    holder.contact_image.setImageResource(R.drawable.user);
                }
                if (image_uri != null) {
                    holder.contact_image.setImageURI(Uri.parse(image_uri));
                }
            }phones.close();
        }



    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,number;
        CircleImageView contact_image;

        public ViewHolder(View itemView) {
            super(itemView);
            contact_image=(CircleImageView)itemView.findViewById(R.id.contact_image);
            name=(TextView)itemView.findViewById(R.id.name);
            number=(TextView)itemView.findViewById(R.id.contact);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            cursor.moveToPosition(getAdapterPosition());
            String phoneNumber="";
            String image_uri="";
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    image_uri=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                }phones.close();
            }
           if (calling_from.equalsIgnoreCase("search"))
           {
               Intent myIntent = new Intent();
               ((Activity)context).setResult(Activity.RESULT_OK, myIntent);
               myIntent.putExtra("selectedNumber",phoneNumber);
               if (image_uri!=null){
                   myIntent.putExtra("image_uri",image_uri);
               }
               myIntent.putExtra("selectedName",cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
               ((Activity)context).finish();
           }
           else {
               Intent i=new Intent(context, CallActivity.class);
               i.putExtra("selectedNumber",phoneNumber);
               if (image_uri!=null){
                   i.putExtra("image_uri",image_uri);
               }
               i.putExtra("selectedName",cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(i);
           }

        }
    }
}
