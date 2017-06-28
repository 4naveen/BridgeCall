package com.project.lorvent.bridgecall.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.lorvent.bridgecall.R;
import com.project.lorvent.bridgecall.utils.DatabaseHandler;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddBridgeDetailsActivity extends AppCompatActivity {
    EditText editTextName, editTextNumber,editBeforeDial,editAfterDial;
    TextInputLayout input_name,input_number;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bridge_details);
        editTextName = (EditText) findViewById(R.id.name);
        editTextNumber = (EditText) findViewById(R.id.number);
        editBeforeDial = (EditText) findViewById(R.id.before_dial);
        editAfterDial = (EditText) findViewById(R.id.after_dial);
        input_name=(TextInputLayout)findViewById(R.id.input_layout_name);
        input_number=(TextInputLayout)findViewById(R.id.input_layout_number);

        save = (Button) findViewById(R.id.save);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Bridge Details");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextName.getText().toString().isEmpty())
                {
                    input_name.setError("Please enter Bridge name");
                    return;
                }

                else if (editTextNumber.getText().toString().isEmpty()){
                    input_number.setError("Please enter Bridge Number");
                    return;
                }

                else {
                    //do nothing
                }

                Intent myIntent = new Intent();
                setResult(Activity.RESULT_OK, myIntent);

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                int result = db.insertBridgeDetails(editTextName.getText().toString(), editTextNumber.getText().toString(),editBeforeDial.getText().toString(),editAfterDial.getText().toString());
                if (result > 0) {
                    new SweetAlertDialog(AddBridgeDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setContentText("Item Successfully added !")
                            .setConfirmText("ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }
                            })
                            .show();
                }


                //Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();

            }
        });
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_name.setError("");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_number.setError("");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
