package com.project.lorvent.bridgecall.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.lorvent.bridgecall.utils.Contacts;
import com.project.lorvent.bridgecall.R;
import com.project.lorvent.bridgecall.adapter.ContactListAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    ContactListAdapter mAdapter;
    ArrayList<Contacts> contactsArrayList;
    RecyclerView recyclerView;
    MaterialSearchView searchView;
    String cursorFilter;
    //SimpleCursorAdapter adapter;
    //private ContactsListAdapter mContactAdapter;

    ListView lvContacts;
    private static final int CURSOR_LOADER_ID = 0;
    public ContactListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contact_list, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        setHasOptionsMenu(true);
        contactsArrayList=new ArrayList<>();
        mAdapter = new ContactListAdapter(getActivity(),"");
        searchView = (MaterialSearchView)getActivity().findViewById(R.id.search_view);

        // By simpleCursorAdapter
   /*     String[] from = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.CONTACT_STATUS,ContactsContract.Contacts.PHOTO_URI};

        int[] to = new int[]{R.id.name, R.id.contact,R.id.contact_image};
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.indi_view_contact_person, null, from, to, 0);
        lvContacts = (ListView)view.findViewById(R.id.lvContacts);
        lvContacts.setDivider(null);
        lvContacts.setAdapter(adapter);*/


       // By Custom cursor Adapter
           /* mContactAdapter=new ContactsListAdapter(getActivity(), null, 0, CURSOR_LOADER_ID);
            lvContacts = (ListView)view.findViewById(R.id.lvContacts);
        lvContacts.setDivider(null);
        lvContacts.setAdapter(mContactAdapter);*/


           //By Recyclerview Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        searchView.setMenuItem(menuItem);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cursorFilter = !TextUtils.isEmpty(newText) ? newText : null;
                getLoaderManager().restartLoader(0, null, ContactListFragment.this);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
     /*   String select = "((" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " NOTNULL) AND ("
                + ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " != '' ))";*/
        Uri baseUri;
        if (cursorFilter != null) {
            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(cursorFilter));
        } else {
            baseUri = ContactsContract.Contacts.CONTENT_URI;
        }
        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";

        return new CursorLoader(getActivity(),
                baseUri,
                null,
                select,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
            mAdapter.setCursor(data);
           // adapter.swapCursor(data);
           // mContactAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setCursor(null);
        //adapter.swapCursor(null);
        //mContactAdapter.swapCursor(null);

    }
    @Override
    public void onResume() {
        super.onResume();
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
    }
}
