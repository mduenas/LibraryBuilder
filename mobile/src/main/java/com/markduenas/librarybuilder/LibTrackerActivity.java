package com.markduenas.librarybuilder;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.markduenas.librarybuilder.db.GoogleBooks.GoogleBookInfo;
import com.markduenas.librarybuilder.db.GoogleBooks.VolumeInfo;
import com.markduenas.librarybuilder.db.ISBNDb.ISBNDbInfo;
import com.markduenas.librarybuilder.util.IntentIntegrator;
import com.markduenas.librarybuilder.util.IntentResult;
import com.markduenas.librarybuilder.util.Scanner;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;



public class LibTrackerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    public static TextView mainContent;
    public static ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_tracker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainContent = (TextView)findViewById(R.id.tvIsbnInfo);
        imageView = (ImageView)findViewById(R.id.imageView);
        if (imageView != null) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (mainContent != null) {

            savedInstanceState.putString("bookInfo", mainContent.getText().toString());
            if (imageView != null && imageView.getDrawable() != null) {

                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image = stream.toByteArray();
                savedInstanceState.putByteArray("image", image);
            }
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {

            if (mainContent != null) {
                mainContent.setText(savedInstanceState.getString("bookInfo"));
            }

            if (imageView != null) {
                byte[] image = savedInstanceState.getByteArray("image");
                if (image != null && image.length > 0) {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                }
            }

        }
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        String contents = "";
        if (requestCode == IntentIntegrator.REQUEST_CODE) {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null && scanResult.getContents() != null) {
                // handle scan result
                //CommonUtils.makeShortToast(BRItemActivity.this, String.format("You scanned: %s", scanResult.getContents()));
                updateBarcodeFragment(scanResult.getContents());
            }
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void updateBarcodeFragment(String barcode) {

        if (mainContent != null) {
            mainContent.setText("Searching for book...");
        }

        GetISBNInfo getISBNInfo = new GetISBNInfo();
        getISBNInfo.execute(String.format("http://isbndb.com/api/v2/json/W8INIP1P/books?q=%s", barcode));

        GoogleInfo googleBookInfo = new GoogleInfo();
        googleBookInfo.execute(String.format("https://www.googleapis.com/books/v1/volumes?q=isbn:%s", barcode));
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

            if (mainContent != null) {
                mainContent.setText(mainContent.getText() + "\n" + result);
                if (isbnDbInfo != null) {
                    mainContent.setText(mainContent.getText() + "\nTitle: " + isbnDbInfo.data.get(0).title);
                    mainContent.setText(mainContent.getText() + "\nAuthor: " + isbnDbInfo.data.get(0).authorData.get(0).name);
                }
            }
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

            if (mainContent != null) {
                mainContent.setText(mainContent.getText() + "\n" + result);

                if (googleBookInfo != null) {

                    if (googleBookInfo.items != null && googleBookInfo.items.size() > 0) {
                        mainContent.setText(mainContent.getText() + "\nTitle: " + googleBookInfo.items.get(0).volumeInfo.title);
                        mainContent.setText(mainContent.getText() + "\nAuthor: " + googleBookInfo.items.get(0).volumeInfo.authors);
                    }

                    if (bytes != null && bytes.length > 0) {
                        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    }
                }
            }
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
