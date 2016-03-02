package com.example.johnw.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class Edit extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        EditText etNewItem = (EditText) findViewById(R.id.editText);
        etNewItem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    EditText editT = (EditText) findViewById(R.id.editText);
                    String itemText = editT.getText().toString();
                    if(itemText.equals("")){
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE","0");
                    }
                    else {
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", itemText);
                        setResult(2, intent);
                        finish();
                        return true;
                    }
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE","0");
                   return true;

                }

                return false;
            }
        });
    }
    public void onSaveButtonPressed(View v){
        EditText editT = (EditText) findViewById(R.id.editText);
        String itemText = editT.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("MESSAGE",itemText);
        setResult(2,intent);
        finish();


    }

}
