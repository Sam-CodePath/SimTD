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

import java.util.List;
import java.util.ArrayList;

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

        items = new ArrayList<>();
        items.add("Buy milk");
        items.add("Go to the gym");
        items.add("Have fun!");

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);

                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);

                // Toast to give user feedback that the item was added
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_LONG).show();
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


            }
        });

    }
}