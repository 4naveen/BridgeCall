package com.project.lorvent.bridgecall.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.lorvent.bridgecall.activity.AddBridgeDetailsActivity;
import com.project.lorvent.bridgecall.utils.BridgeListItem;
import com.project.lorvent.bridgecall.utils.DatabaseHandler;
import com.project.lorvent.bridgecall.R;
import com.project.lorvent.bridgecall.adapter.BridgeCallListAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BridgeCallFragment extends Fragment {
    ListView listView;
    BridgeCallListAdapter bridgeCallListAdapter;
    private static final int REQ_EDIT = 100;
    DatabaseHandler db;
    ArrayList<BridgeListItem> bridgeListItems;
    public BridgeCallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_bridge_call, container, false);
        listView = (ListView) view.findViewById(R.id.bridge_numbers_listview);

        db = new DatabaseHandler(getActivity());
        bridgeListItems = db.getAllBridgeDetails();

        bridgeCallListAdapter = new BridgeCallListAdapter(getActivity(), bridgeListItems);
        listView.setAdapter(bridgeCallListAdapter);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), AddBridgeDetailsActivity.class);
                  startActivityForResult(i,0);

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
            {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("requestcode","0");
                new AsyncTaskRunner().execute();}
                break;
            }


       /*     case REQ_EDIT:
                if (resultCode == Activity.RESULT_OK) {

                    new AsyncTaskBridgeEdit().execute();

                }
                break;*/

        }
    }


    private class AsyncTaskRunner extends AsyncTask<Void, Integer, ArrayList<BridgeListItem>> {
        boolean running;
        ProgressDialog progressDialog;
        int progressStatus = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = true;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Adding details");
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
        protected void onPostExecute(ArrayList<BridgeListItem> response) {

            bridgeCallListAdapter = new BridgeCallListAdapter(getActivity(), response);
            listView.setAdapter(bridgeCallListAdapter);
            bridgeCallListAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

}
