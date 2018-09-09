package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class BrowseActivity extends AppCompatActivity {

    private TextView mTextMessage;

//    private TextView statusMessage;
//    private TextView barcodeValue;

    private static final int RC_BARCODE_CAPTURE = 9001;

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static int size = 0;
    List<String> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_browse);

        getSupportActionBar().setTitle("Browse by category"); // for set actionbar title
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // TODO Auto-generated method stub
            int id = item.getItemId();
            if (id == android.R.id.home) {
                finish();
            }
            return super.onOptionsItemSelected(item);
        */

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //mTextMessage = (TextView) findViewById(R.id.message);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_browse:
                        //mTextMessage.setText(R.string.title_browse);
//                    Intent browseIntent = new Intent(BrowseActivity.this, BrowseActivity.class);
//                    startActivity(browseIntent);
                        return true;
                    case R.id.navigation_upload:
                        Intent intent = new Intent(BrowseActivity.this, UploadActivity.class);
                        //intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());

                        //Make AutoFocus always True
                        startActivity(intent);
                        return true;
                    case R.id.navigation_myBooks:
                        //mTextMessage.setText(R.string.title_myBooks);
                        Log.d("aldsk", "myBooks intent");
                        Intent myBooksIntent = new Intent(BrowseActivity.this, MyBooksActivity.class);
                        startActivity(myBooksIntent);
                        return true;
                    case R.id.navigation_messages:
                        //mTextMessage.setText(R.string.title_browse);
                        Intent messageIntent = new Intent(BrowseActivity.this, MessagesActivity.class);
                        startActivity(messageIntent);
                        return true;
                    case R.id.navigation_home:
                        //mTextMessage.setText(R.string.title_browse);
                        Intent dashBoardIntent = new Intent(BrowseActivity.this, DashBoardActivity.class);
                        startActivity(dashBoardIntent);
                        return true;


                }
                return false;
            }
        });


        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        for(int i = 0; i < 3; i++) {
            if(i != 0) {
                menuItem = menu.getItem(i);
                menuItem.setChecked(false);
            }
        }


        categories = new ArrayList<>();

        db.collection("Categories").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Beginning of db oncomplete");
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                categories.add(document.getId());
                            }

                            String[] categoriesArray = new String [task.getResult().size()];
                            for(int i = 0; i < categories.size(); i++){
                                categoriesArray[i] = categories.get(i);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, categoriesArray) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);

                                    TextView textView = (TextView) view.findViewById(android.R.id.text1);

                                    /*YOUR CHOICE OF COLOR*/
                                    textView.setTextColor(Color.BLUE);
                                    return view;

                                }
                            };

//                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(
//                                    this, android.R.layout.simple_list_item_1, listItems){
//
//                                @Override
//                                public View getView(int position, View convertView, ViewGroup parent) {
//                                    View view =super.getView(position, convertView, parent);
//
//                                    TextView textView=(TextView) view.findViewById(android.R.id.text1);
//
//                                    /*YOUR CHOICE OF COLOR*/
//                                    textView.setTextColor(Color.BLUE);
//
//                                    return view;
//                                }
//                            };
                            ListView listView = (ListView) findViewById(R.id.bookList);

                            listView.setAdapter(adapter);
//
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String category = (String)((TextView)view).getText();
                                    Log.d(TAG, category);

                                    //intent for activity to display book details
                                    Intent intent = new Intent(BrowseActivity.this, BookCategoryDisplayActivity.class);
                                    intent.putExtra("category", category);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

//        String[] categoriesArray = new String [size];
//        for(int i = 0; i < categories.size(); i++){
//            categoriesArray[i] = categories.get(i);
//        }
//
//        for(String category: categoriesArray) {
//            Log.d(TAG, "Category: " + category);
//        }
//
////        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
////                android.R.layout.simple_list_item_1, categoriesArray);
////        ListView listView = (ListView) findViewById(R.id.bookList);
//        for(String category: categoriesArray) {
//            Log.d(TAG, "Category: " + category);
//        }
////        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String category = view.toString();
//                Log.d(TAG, category);
//
//                //intent for activity to display book details
//            }
//        });

        Log.d(TAG, "End of BrowseActivity");
        findViewById(R.id.navigation_browse).performClick();
    }
}





