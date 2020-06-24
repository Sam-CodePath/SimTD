package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    List<String> items;

    Button btnAdd;
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

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);

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