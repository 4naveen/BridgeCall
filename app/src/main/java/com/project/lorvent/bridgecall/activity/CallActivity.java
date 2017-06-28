package com.project.lorvent.bridgecall.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.lorvent.bridgecall.R;
import com.project.lorvent.bridgecall.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class CallActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    TextView textViewPickContact,textViewSelectedContact,textViewSelectedBridgeName;
    String selectedContact,selectName,selectedSpinnerBridgeNumber=null;
    public static final int MY_PERMISSIONS_REQUEST_CALL = 101;
    Spinner spinnerBridgeNumbers;
    List<String> spinnerArray;
    DatabaseHandler db;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    ImageView contact_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
         collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
         collapsingToolbarLayout.setTitle("Dial");

        textViewPickContact=(TextView)findViewById(R.id.tv_select_contact);
        textViewPickContact.setPaintFlags(textViewPickContact.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        contact_image=(ImageView)findViewById(R.id.contact_image);
        textViewSelectedContact=(TextView)findViewById(R.id.tv_selected_contact);
        textViewSelectedBridgeName=(TextView)findViewById(R.id.tv_selected_name_bridge);
        spinnerBridgeNumbers=(Spinner)findViewById(R.id.spinner_bridge_number);
        spinnerArray =  new ArrayList<String>();
        db = new DatabaseHandler(getApplicationContext());
        arrayList=new ArrayList<String>();
        textViewSelectedContact.setText(getIntent().getStringExtra("selectedNumber"));
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("selectedName"));
        if (getIntent().getStringExtra("image_uri")!=null){
            contact_image.setImageURI(Uri.parse(getIntent().getStringExtra("image_uri")));

        }
        new AsyncTaskRunner().execute();
        textViewPickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // textViewSelectedContact.setText("");
                Intent intent = new Intent(getApplicationContext(), ContactsListSearchActivity.class);
                startActivityForResult(intent,0);

            }
        });
        spinnerBridgeNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpinnerBridgeNumber=spinnerBridgeNumbers.getSelectedItem().toString();
                Log.d("Selected item",selectedSpinnerBridgeNumber);
                new AsyncTaskBridgeName().execute(selectedSpinnerBridgeNumber);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        MyPhoneListener phoneListener = new MyPhoneListener();

        TelephonyManager telephonyManager =(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerBridgeNumbers.getSelectedItem()==null) {

                  /*  AlertDialog.Builder builder = new AlertDialog.Builder(CallActivity.this);
                    builder.setTitle("Bridge Details");
                    builder.setMessage(" No Bridge details found, Please add bridge details to call");
                    builder.setCancelable(false);
                    builder.setPositiveButton("ok", null);

                    final AlertDialog alertDialog = builder.create();

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(final DialogInterface dialog) {

                            Button buttonOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            buttonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();
                                    finish();
                                }
                            });

                        }
                    });

                    alertDialog.show();*/
                } else {

                    if (ContextCompat.checkSelfPermission(CallActivity.this,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(CallActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL);
                    }
                    else {

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + spinnerBridgeNumbers.getSelectedItem().toString() + "" + textViewSelectedContact.getText().toString()));
                        startActivity(intent);
                    }

                }
            }
        });

    }
    private class AsyncTaskRunner extends AsyncTask<Void ,Integer ,ArrayList<String>> {

        boolean running;
        ProgressDialog progressDialog;
        int progressStatus =0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = true;
            progressDialog = new ProgressDialog(CallActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Loading Bridge details");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            progressDialog.setProgress(progressStatus);

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    running = false;
                }
            });
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            arrayList = db.getNumber();
            int i = 5;
            while(running & progressStatus<5){
                try {
                    progressStatus++;
                    publishProgress(progressStatus);
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(i-- == 0){
                    running = false;
                }
                publishProgress(i);

            }
            return arrayList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // progressDialog.setMessage(String.valueOf(values[0]));
            progressDialog.setProgress(progressStatus);
        }

        @Override
        protected void onPostExecute(ArrayList<String> aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid.size()==0) {
                progressDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(CallActivity.this);
                builder.setTitle("Bridge Details");
                builder.setMessage("No Bridge details found, Please add bridge details to call");
                builder.setCancelable(false);
                builder.setPositiveButton("ok", null);

                final AlertDialog alertDialog= builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {

                        Button buttonOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        buttonOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                                finish();
                            }
                        });

                    }
                });

                alertDialog.show();
            }
            else {
                adapter = new ArrayAdapter<String>(CallActivity.this, android.R.layout.simple_spinner_item, aVoid);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBridgeNumbers.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
    }
    private class AsyncTaskBridgeName extends AsyncTask<String,Integer ,String> {

        boolean running;
        ProgressDialog progressDialog;
        int progressStatus =0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = true;
            progressDialog = new ProgressDialog(CallActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Getting Bridge name");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            progressDialog.setProgress(progressStatus);

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    running = false;
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            selectName = db.getName(params[0]);
            int i = 5;
            while(running & progressStatus<5){
                try {
                    progressStatus++;
                    publishProgress(progressStatus);
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(i-- == 0){
                    running = false;
                }
                publishProgress(i);

            }
            return selectName;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // progressDialog.setMessage(String.valueOf(values[0]));
            progressDialog.setProgress(progressStatus);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            textViewSelectedBridgeName.setText(result);
            progressDialog.dismiss();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 0 :
                if (resultCode == Activity.RESULT_OK) {
                    selectedContact=data.getStringExtra("selectedNumber");
                    textViewSelectedContact.setText(selectedContact);
                    collapsingToolbarLayout.setTitle(data.getStringExtra("selectedName"));

                    if (data.getStringExtra("image_uri")!=null){
                        contact_image.setImageURI(Uri.parse(data.getStringExtra("image_uri")));

                    }
                }
                break;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("in permission","access");
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + spinnerBridgeNumbers.getSelectedItem().toString() + ",," + selectedContact));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(intent);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


        }
    }
    private class MyPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Toast.makeText(CallActivity.this, incomingNumber + " calls you",Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //Toast.makeText(CallActivity.this, "on call...",Toast.LENGTH_LONG).show();
                    onCall = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if (onCall == true) {
                 /*       Toast.makeText(CallActivity.this, "restart app after call",Toast.LENGTH_LONG).show();

                        Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restart);*/

                        onCall = false;
                    }
                    break;
                default:
                    break;
            }

        }
    }
}
