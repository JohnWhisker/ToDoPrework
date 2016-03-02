package com.example.johnw.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItem;
    String taskname, taskdate;
    int pos2;
    DBHelper tododb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tododb = new DBHelper(this);
        lvItem = (ListView) findViewById(R.id.IvItems);
        items = new ArrayList<>();
        //readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItem.setAdapter(itemsAdapter);
        tododb.refreshTaskList(itemsAdapter);
        setupListViewListener();
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        etNewItem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
                    String itemText = etNewItem.getText().toString();
                    itemsAdapter.add(itemText);
                    etNewItem.setText("");
                    //writeItems();
                    tododb.updateAfterAdapter(itemsAdapter,items);
                    return true;
                }
                return false;

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            String message = data.getStringExtra("MESSAGE");
            //edit
            if (!message.equals("0")) {
                items.set(pos2, message);
                itemsAdapter.notifyDataSetChanged();
                tododb.updateAfterAdapter(itemsAdapter,items);
            }
        }

    }

    private void setupListViewListener() {
        final Context context = this;
        lvItem.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int position, long id) {

                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        tododb.updateAfterAdapter(itemsAdapter,items);
                        return true;
                    }
                }
        );
        lvItem.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pos2 = position;
                        Intent intent = new Intent(context, Edit.class);
                        startActivityForResult(intent, 2);
                    }
                }
        );
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        tododb.updateAfterAdapter(itemsAdapter,items);
    }


}











