package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.os.Bundle;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;


    List<String> items;

    Button btnAdd;
    Button btnClear;
    EditText edItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        edItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        btnClear = findViewById(R.id.btnClear);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);

                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);

                // Toast to give user feedback that the item was added
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_LONG).show();

                // Save items
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position " + position);
                // Create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // Pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // Display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);

        rvItems.setAdapter(itemsAdapter);

        // Puts things on the UI in a vertical way
        rvItems.setLayoutManager((new LinearLayoutManager(this)));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Someone has tapped on our button

                String todoItem = edItem.getText().toString();

                // Add to the model
                items.add(todoItem);

                // Notify the adapter that we've inserted an item
                itemsAdapter.notifyItemInserted(items.size() - 1 );
                edItem.setText("");

                // Toast to give user feedback that the item was added
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_LONG).show();

                // Save items
                saveItems();


            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.clear();
                itemsAdapter.notifyDataSetChanged();
                saveItems();
            }
        });

    }

    // Handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // Retrieve updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract original position of edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update the model at the right position with the new item text
            items.set(position, itemText);
            // notify adapter that something has changed
            itemsAdapter.notifyItemChanged(position);
            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_LONG).show();
        } else {
            Log.w("MainActivity", "Unknown call to onAcativityResult");
        }

    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of our data.txt file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch(IOException e){
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}