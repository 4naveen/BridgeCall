package com.project.lorvent.bridgecall.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.project.lorvent.bridgecall.utils.BridgeListItem;
import com.project.lorvent.bridgecall.utils.DatabaseHandler;
import com.project.lorvent.bridgecall.R;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class BridgeCallListAdapter extends BaseAdapter {
    EditText name,number;
    private Context context;
    private ArrayList<BridgeListItem> bridgeListItems;
    LayoutInflater layoutInflater;
    private static final int REQ_EDIT = 100;
    Button buttonOk,buttonCancel;
    DatabaseHandler db;
    TextInputLayout input_name, input_number;
    TextView textname,textnumber,before_dial,after_dial;
    public BridgeCallListAdapter(Context context, ArrayList<BridgeListItem> bridgeListItems) {
        this.context = context;
        this.bridgeListItems = bridgeListItems;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return bridgeListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return bridgeListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtNumber;
        TextView textViewDefault;
        ImageView imageViewOptions;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.bridge_call_list_itemview, null);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tv_bridge_call_name);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.tv_bridge_call_number);
            viewHolder.imageViewOptions = (ImageView) convertView.findViewById(R.id.imageview_list_item_options);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(bridgeListItems.get(position).getName());
        viewHolder.txtNumber.setText(bridgeListItems.get(position).getNumber());
   /*     for (BridgeListItem item:bridgeListItems) {
            Log.i("itemname",item.getName());
            Log.i("itemnumber",item.getNumber());
            Log.i("itemid", String.valueOf(item.getId()));
            Log.i("itempos", String.valueOf(position));

        }*/
        viewHolder.imageViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // displayPopupWindow(v, position);
                PopupMenu pop=new PopupMenu(context,viewHolder.imageViewOptions);
                pop.getMenuInflater().inflate(R.menu.menu_main,pop.getMenu());
                pop.setGravity(1);
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId())
                      {
                          case R.id.edit:{
                              MaterialDialog dialog1=new MaterialDialog.Builder(context)
                                      .title("Edit Bridge Details")
                                      .customView(R.layout.edit_dialog, true)
                                      .positiveText("Save")
                                      .autoDismiss(false)
                                      .positiveColorRes(R.color.colorPrimary)
                                      .onPositive(new MaterialDialog.SingleButtonCallback() {
                                          @Override
                                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                              if (name.getText().toString().isEmpty()) {
                                                  input_name.setError("please enter bridge name");
                                                  return;
                                              }
                                              else if (number.getText().toString().isEmpty()) {
                                                  input_number.setError("please enter bridge number");
                                                  return;
                                              }
                                              else {
                                                  dialog.dismiss();
                                              }
                                              db = new DatabaseHandler(context);
                                              int row=db.updateBridgeItem(bridgeListItems.get(position), name.getText().toString(),number.getText().toString());
                                              if (row>0)
                                              {
                                                  Toast.makeText(context, "Saved successfully", Toast.LENGTH_LONG).show();
                                                  new AsyncTaskBridgeEdit().execute();

                                              }
                                              dialog.dismiss();
                                          }
                                      })
                                      .negativeColorRes(R.color.colorPrimary)
                                      .negativeText("CANCEL")
                                      .onNegative(new MaterialDialog.SingleButtonCallback() {
                                          @Override
                                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                              dialog.dismiss();

                                          }
                                      })
                                      .show();

                              View view = dialog1.getCustomView();
                              if (view != null) {
                                  name=(EditText)dialog1.getCustomView().findViewById(R.id.name);
                                  number=(EditText)dialog1.getCustomView().findViewById(R.id.number);
                                  input_name = (TextInputLayout) dialog1.getCustomView().findViewById(R.id.input_layout_name);
                                  input_number = (TextInputLayout) dialog1.getCustomView().findViewById(R.id.input_layout_number);

                                  name.addTextChangedListener(new TextWatcher() {
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

                                  number.addTextChangedListener(new TextWatcher() {
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
                              break;
                          }
                          case R.id.delete:{
                              new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                      .setTitleText("Are you sure?")
                                      .setContentText("Won't be able to recover this file!")
                                      .setConfirmText("Yes,delete it!")
                                      .setCancelText("cancel")
                                      .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                          @Override
                                          public void onClick(SweetAlertDialog sweetAlertDialog) {
                                              db = new DatabaseHandler(context);
                                             int row= db.deleteBridgeListItem(bridgeListItems.get(position));
                                              if (row>0)
                                              {sweetAlertDialog.dismissWithAnimation();
                                                  Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_SHORT).show();
                                              }
                                              Log.i("clicked pos", String.valueOf(position));
                                              bridgeListItems.remove(position);
                                              notifyDataSetChanged();
                                          }
                                      })
                                      .show();
                              break;

                          }
                          case R.id.details:{
                              db = new DatabaseHandler(context);
                              BridgeListItem bridgeListItem= db.readBridgeItem(bridgeListItems.get(position));

                              MaterialDialog dialog1=new MaterialDialog.Builder(context)
                                      .title("Bridge Details")
                                      .customView(R.layout.details_dialog, true)
                                      .positiveText("ok")
                                      .autoDismiss(false)
                                      .positiveColorRes(R.color.colorPrimary)
                                      .onPositive(new MaterialDialog.SingleButtonCallback() {
                                          @Override
                                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                              dialog.dismiss();
                                          }
                                      })
                                      .show();

                              View view = dialog1.getCustomView();
                              if (view != null) {
                                  textname=(TextView)dialog1.getCustomView().findViewById(R.id.name);
                                  textnumber=(TextView)dialog1.getCustomView().findViewById(R.id.number);
                                  before_dial=(TextView)dialog1.getCustomView().findViewById(R.id.before_dial);
                                  after_dial=(TextView)dialog1.getCustomView().findViewById(R.id.after_dial);
                                  textname.setText(bridgeListItem.getName());
                                  textnumber.setText(bridgeListItem.getNumber());
                                  before_dial.setText(bridgeListItem.getBefore_dial());
                                  after_dial.setText(bridgeListItem.getAfter_dial());

                              }
                              break;

                          }
                      }

                        return true;
                    }
                });
                pop.show();
            }
        });
        return convertView;
    }


    private class AsyncTaskBridgeEdit extends AsyncTask<Void, Integer, ArrayList<BridgeListItem>> {

        boolean running;
        ProgressDialog progressDialog;
        int progressStatus = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            running = true;
            bridgeListItems.clear();
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Saving details");
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
        protected ArrayList<BridgeListItem> doInBackground(Void... params) {

          bridgeListItems = db.getAllBridgeDetails();
            int i = 5;
            while (running & progressStatus < 5) {
                try {
                    progressStatus++;
                    publishProgress(progressStatus);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (i-- == 0) {
                    running = false;
                }
                publishProgress(i);

            }
            return bridgeListItems;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(progressStatus);
        }

        @Override
        protected void onPostExecute(ArrayList<BridgeListItem> aVoid) {
            super.onPostExecute(aVoid);
           // bridgeListItems = db.getAllBridgeDetails();
            notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }
}


