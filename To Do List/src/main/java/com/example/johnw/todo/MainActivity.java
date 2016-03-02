package com.example.johnw.todo;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    ArrayList<Task> items ;
    TasksAdapter itemsAdapter ;
    ListView lvItem ;
    int pos2;
    DBHelper tododb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items =  new ArrayList<Task>();
        itemsAdapter = new TasksAdapter(this, items);
        lvItem = (ListView) findViewById(R.id.IvItems);
        tododb = new DBHelper(this);
        items = new ArrayList<>();

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
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String date = "";
                    if (myCalendar != null) {
                        date = sdf.format(myCalendar.getTime());
                    }
                    Task newTask = new Task(itemText,date);
                    itemsAdapter.add(newTask);
                    etNewItem.setText("");
                    tododb.updateAfterAdapter(itemsAdapter, items);
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

                Task newtask = items.get(pos2);
                Task writetask = new Task(message,newtask.datetime);
                items.set(pos2,writetask);
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
                        tododb.updateAfterAdapter(itemsAdapter, items);
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
    public void setDate(View v){
        new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void onAddItem(View v) {



        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String date = "";
        //itemsAdapter.add(itemText);
         if (myCalendar != null) {
           date = sdf.format(myCalendar.getTime());
        }
        Task newTask = new Task(itemText,date);
        itemsAdapter.add(newTask);
        etNewItem.setText("");
        tododb.updateAfterAdapter(itemsAdapter, items);
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            onPause();
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            onResume();
        }

    };
}











