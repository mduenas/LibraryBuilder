package com.markduenas.librarybuilder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.markduenas.librarybuilder.api.GoogleBooks.GoogleBookInfo;
import com.markduenas.librarybuilder.api.GoogleBooks.Item;
import com.markduenas.librarybuilder.api.GoogleBooks.VolumeInfo;
import com.markduenas.librarybuilder.api.ISBNDb.AuthorDatum;
import com.markduenas.librarybuilder.api.ISBNDb.Datum;
import com.markduenas.librarybuilder.api.ISBNDb.ISBNDbInfo;
import com.markduenas.librarybuilder.db.Book;
import com.markduenas.librarybuilder.db.LibraryDBHelper;
import com.markduenas.librarybuilder.util.IntentIntegrator;
import com.markduenas.librarybuilder.util.IntentResult;
import com.markduenas.librarybuilder.util.Scanner;
import com.markduenas.librarybuilder.widget.ItemListAdapter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class LibTrackerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static int VIEW_LIST = 0;
    private static int VIEW_SEARCH = 1;
    private static int VIEW_BOOK = 2;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    LibraryDBHelper dbHelper = null;

    CharSequence mTitle;

    class SearchItem {
        String title;
        String author;
        String isbn13;
        String isbn10;
        byte[] image;
    }

    String mCurrentISBN;
    Book mBook;
    List<Book> bookList;
    List<SearchItem> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_tracker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBook != null) {
                    String field = "";
                    String value = "";
                    if (mBook.isbn13 != null && !mBook.isbn13.isEmpty()) {
                        field = "isbn13";
                        value = mBook.isbn13;
                    } else if (mBook.isbn10 != null && !mBook.isbn10.isEmpty()) {
                        field = "isbn10";
                        value = mBook.isbn10;
                    }
                    if (field.isEmpty() || value.isEmpty()) {

                        dbHelper.insertSingleDatabaseRowNoAppId(Book.class, mBook);
                        Snackbar.make(view, String.format("%s has been added to your library!", mBook.title), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        selectItem(VIEW_LIST);
                    } else {

                        List<Book> temp = dbHelper.getDatabaseListFiltered(Book.class, field, value);
                        if (temp.size() == 0) {

                            // insert the book
                            dbHelper.insertSingleDatabaseRowNoAppId(Book.class, mBook);
                            Snackbar.make(view, String.format("%s has been added to your library!", mBook.title), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            selectItem(VIEW_LIST);
                        } else {

                            // possible duplicate?
                        }
                    }

                } else {
                    Snackbar.make(view, "You must scan or create a book first", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeDb();

        selectItem(VIEW_LIST);
    }

    private void initializeDb() {

        dbHelper = LibraryDBHelper.createInstance(LibTrackerActivity.this);
        if (!dbHelper.tableExists(Book.class)) {

            dbHelper.createTable(Book.class);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // save instance state for book view
//        if (mainContent != null) {
//
//            savedInstanceState.putString("bookInfo", mainContent.getText().toString());
//            if (imageView != null && imageView.getDrawable() != null) {
//
//                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
//                byte[] image = stream.toByteArray();
//                savedInstanceState.putByteArray("image", image);
//            }
//        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // restore instance state for book view
//        if (savedInstanceState != null) {
//
//            if (mainContent != null) {
//
//                mainContent.setText(savedInstanceState.getString("bookInfo"));
//            }
//
//            if (imageView != null) {
//
//                byte[] image = savedInstanceState.getByteArray("image");
//                if (image != null && image.length > 0) {
//
//                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
//                }
//            }
//        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lib_tracker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Scanner.acquireCameraScan(LibTrackerActivity.this);
        } else if (id == R.id.nav_search) {

            selectItem(0);

        } else if (id == R.id.nav_my_books) {

            selectItem(1);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(ContentFragment.ARG_VIEW_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        populateViews(position);

        // Highlight the selected item, update the title, and close the drawer
        //mDrawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        //mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void populateViews(int position) {

        switch (position) {

            case 0: //VIEW_SEARCH


                break;
            case 1:  //VIEW_LIST
                // populate the book list

                break;
            case 2:  //VIEW_BOOK

                break;
        }
    }

    /**
     * Fragment that appears in the "content_frame"
     */
    public static class ContentFragment extends Fragment {

        public static final String ARG_VIEW_NUMBER = "view_number";

        public static final int ARG_LIST = 0;
        public static final int ARG_SEARCH = 1;
        public static final int ARG_BOOK = 2;


        public ContentFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            LibTrackerActivity mainActivity = (LibTrackerActivity)getActivity();
            View rootView = null;
            int i = getArguments().getInt(ARG_VIEW_NUMBER);

            if (i == ARG_LIST) {

                // inflate the list view and populate it
                rootView = inflater.inflate(R.layout.fragment_list, container, false);
                TextView empty = (TextView)rootView.findViewById(android.R.id.empty);
                ListView listView = (ListView)rootView.findViewById(R.id.listBooks);
                if (listView != null) {
                    LibraryDBHelper dbHelper = LibraryDBHelper.createInstance(mainActivity);
                    List<Book> bookList = dbHelper.getDatabaseListNoAppId(Book.class);
                    if (bookList != null) {
                        Book[] bookArray = new Book[bookList.size()];
                        int counter = 0;
                        for (Book b :
                                bookList) {
                            bookArray[counter] = b;
                            counter++;
                        }
                        ItemListAdapter adapter = new ItemListAdapter(mainActivity, R.layout.item_list_item, bookArray);
                        listView.setEmptyView(empty);
                        listView.setAdapter(adapter);
                    }
                }

            } else if (i == ARG_BOOK) {

                // inflate the book view and populate it
                rootView = inflater.inflate(R.layout.fragment_book, container, false);
                // populate the search list
                TextView info = (TextView)rootView.findViewById(R.id.tvIsbnInfo);
                if (info != null) {
                    if (mainActivity.mBook != null) {
                        String isbnInfo = String.format("Title: %s\n", mainActivity.mBook.title);
                        isbnInfo += String.format("Author: %s\n", mainActivity.mBook.author);
                        isbnInfo += String.format("Description: %s\n", mainActivity.mBook.description);
                        isbnInfo += String.format("Publisher: %s\n", mainActivity.mBook.publisher);
                        isbnInfo += String.format("Pages: %d\n", mainActivity.mBook.pages);
                        isbnInfo += String.format("Language: %s\n", mainActivity.mBook.language);
                        isbnInfo += String.format("Category: %s\n", mainActivity.mBook.category);
                        isbnInfo += String.format("Publication: %s\n", mainActivity.mBook.publication);
                        info.setText(isbnInfo);

                    } else {
                        info.setText("Searching for book...");
                    }
                }
                ImageView image = (ImageView)rootView.findViewById(R.id.imageViewBook);
                if (image != null) {
                    if (mainActivity.mBook != null) {
                        if (mainActivity.mBook.thumbNail != null && mainActivity.mBook.thumbNail.length > 0) {
                            image.setImageBitmap(BitmapFactory.decodeByteArray(mainActivity.mBook.thumbNail, 0, mainActivity.mBook.thumbNail.length));
                        } else {
                            image.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                        }
                    } else {
                        image.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                    }
                }

            } else if (i == ARG_SEARCH) {

                // inflate the search view and populate it
                rootView = inflater.inflate(R.layout.fragment_search_list, container, false);
                TextView empty = (TextView)rootView.findViewById(android.R.id.empty);
                ListView listView = (ListView)rootView.findViewById(android.R.id.list);
                if (listView != null) {
                    if (mainActivity.searchList != null) {
                        ItemListAdapter adapter = new ItemListAdapter(mainActivity, R.layout.item_list_item, (Book[]) mainActivity.searchList.toArray());
                        listView.setEmptyView(empty);
                        listView.setAdapter(adapter);
                    }
                }
            }

            return rootView;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        String contents = "";
        if (requestCode == IntentIntegrator.REQUEST_CODE) {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null && scanResult.getContents() != null) {
                // handle scan result
                selectItem(2);
                mBook = new Book();
                mCurrentISBN = scanResult.getContents();
                GetISBNInfo getISBNInfo = new GetISBNInfo();
                getISBNInfo.execute(String.format("http://isbndb.com/api/v2/json/W8INIP1P/books?q=%s", mCurrentISBN));
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    class GetISBNInfo extends AsyncTask<String, Void, String> {

        ISBNDbInfo isbnDbInfo = null;
        @Override
        protected String doInBackground(String... urls) {

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(urls[0])
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonData = responses.body().string();
                Gson gson = new Gson();
                isbnDbInfo = gson.fromJson(jsonData, ISBNDbInfo.class);
                return String.format("Found %d from ISBNDb", isbnDbInfo.resultCount);

            } catch (IOException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mBook == null) {
                mBook = new Book();
            }
            if (mBook != null) {
                if (isbnDbInfo != null) {
                    Datum datum = isbnDbInfo.data.get(0);
                    if (datum != null) {
                        mBook.title = datum.title;
                        mBook.isbn10 = datum.isbn10;
                        mBook.isbn13 = datum.isbn13;
                        mBook.category = datum.subjectIds.get(0);
                        mBook.description = datum.physicalDescriptionText;
                        mBook.publisher = datum.publisherName;
                        mBook.publication = datum.publisherText;
                        mBook.language = datum.language;
                        //mBook.pages = datum.pages?
                        AuthorDatum authorDatum = datum.authorData.get(0);
                        mBook.author = authorDatum.name;

                    }
                }
            }

            GoogleInfo googleBookInfo = new GoogleInfo();
            googleBookInfo.execute(String.format("https://www.googleapis.com/books/v1/volumes?q=isbn:%s", mCurrentISBN));
        }
    }

    class GoogleInfo extends AsyncTask<String, Void, String> {

        GoogleBookInfo googleBookInfo = null;
        byte[] bytes;

        @Override
        protected String doInBackground(String... urls) {

            try {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(urls[0])
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonData = responses.body().string();
                Gson gson = new Gson();
                googleBookInfo = gson.fromJson(jsonData, GoogleBookInfo.class);

                if (googleBookInfo != null) {

                    if(googleBookInfo.items != null && googleBookInfo.items.size() > 0) {

                        VolumeInfo volumeInfo = googleBookInfo.items.get(0).volumeInfo;
                        if (volumeInfo != null && volumeInfo.imageLinks != null) {

                            if (volumeInfo.imageLinks.thumbnail != null && !volumeInfo.imageLinks.thumbnail.isEmpty()) {

                                String url = volumeInfo.imageLinks.thumbnail;
                                bytes = imageByter(url);
                            }
                        }
                    }
                }

                return String.format("Found %d items from Google book info", googleBookInfo.totalItems);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mBook == null) {
                mBook = new Book();
            }
            if (mBook != null) {

                if (googleBookInfo != null) {

                    if (googleBookInfo.items != null && googleBookInfo.items.size() > 0) {

                        Item item = googleBookInfo.items.get(0);
                        if (item != null && item.volumeInfo != null) {

                            if (mBook.title == null) {

                                mBook.title = item.volumeInfo.title;
                            }
                            if (mBook.author == null) {

                                mBook.author = item.volumeInfo.authors.get(0);
                            }

                            mBook.pages = item.volumeInfo.pageCount;
                            if (item.volumeInfo.categories != null && item.volumeInfo.categories.size() > 0) {

                                mBook.category = item.volumeInfo.categories.get(0);
                            }
                        }
                        //mainContent.setText(mainContent.getText() + "\nTitle: " + googleBookInfo.items.get(0).volumeInfo.title);
                        //mainContent.setText(mainContent.getText() + "\nAuthor: " + googleBookInfo.items.get(0).volumeInfo.authors);
                    }

                    if (bytes != null && bytes.length > 0) {

                        mBook.thumbNail = bytes.clone();
                    }
                }
            }
            selectItem(2);
        }

        protected byte[] imageByter(String strurl) {

            try {

                URL url = new URL(strurl);
                InputStream is = (InputStream) url.getContent();
                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                while ((bytesRead = is.read(buffer)) != -1) {

                    output.write(buffer, 0, bytesRead);
                }
                return output.toByteArray();

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return null;
            } catch (IOException e) {

                e.printStackTrace();
                return null;
            }
        }
    }

    class GetAmazonISBNInfo extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(urls[0])
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonData = responses.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
