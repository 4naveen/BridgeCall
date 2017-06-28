package com.project.lorvent.bridgecall.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.project.lorvent.bridgecall.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sam_chordas on 7/23/15.
 */
public class ContactsListAdapter extends CursorAdapter {

    private static final String LOG_TAG = ContactsListAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public static class ViewHolder {
        TextView name,number;
        CircleImageView contact_image;

        public ViewHolder(View view){
            contact_image=(CircleImageView)view.findViewById(R.id.contact_image);
            name=(TextView)view.findViewById(R.id.name);
            number=(TextView)view.findViewById(R.id.contact);
        }
    }

    public ContactsListAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        int layoutId = R.layout.indi_view_contact_person;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String image_uri;
        viewHolder.name.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                viewHolder.number.setText(phoneNumber);
                image_uri=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (image_uri == null) {
                    viewHolder.contact_image.setImageResource(R.drawable.user);
                }
                if (image_uri != null) {
                    viewHolder.contact_image.setImageURI(Uri.parse(image_uri));
                }
            }phones.close();
        }
    }
}
