package com.project.lorvent.bridgecall.activity;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.project.lorvent.bridgecall.R;
import com.project.lorvent.bridgecall.adapter.ContactListAdapter;
import com.project.lorvent.bridgecall.utils.Contacts;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


import java.util.ArrayList;

public class ContactsListSearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    ContactListAdapter mAdapter;
    ArrayList<Contacts> contactsArrayList;
    RecyclerView recyclerView;
    private static final int CURSOR_LOADER_ID = 0;
    MaterialSearchView searchView;
    String cursorFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);
        searchView=(MaterialSearchView)findViewById(R.id.search_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Select Contacts");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        contactsArrayList=new ArrayList<>();
        mAdapter = new ContactListAdapter(this,"Search");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
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
                getSupportLoaderManager().restartLoader(0, null, ContactsListSearchActivity.this);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
        return new CursorLoader(this,
                baseUri,
                null,
                select,
                null,
                ContactsContract.Contacts.DISPLAY_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {

            mAdapter.setCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setCursor(null);
    }
    @Override
    protected void onResume() {

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
        super.onResume();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();

            }
        }
        return super.onOptionsItemSelected(item);
    }
}
