package com.example.persistence;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private Button buttonWrite;
    private Button buttonRead;
    private Button buttonClear;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        buttonWrite = findViewById(R.id.button_write);
        buttonRead = findViewById(R.id.button_read);
        buttonClear = findViewById(R.id.button_clear);
        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);
        editText3 = findViewById(R.id.edit_text3);
        textView = findViewById(R.id.text_view);

        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToDatabase();
            }
        });

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFromDatabase();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDatabase();
            }
        });

    }

    private void writeToDatabase() {
        String value1 = editText1.getText().toString();
        String value2 = editText2.getText().toString();
        String value3 = editText3.getText().toString();

        // Avoid writing "Mountains" to the database
        if(!value1.equalsIgnoreCase("Mountains") && !value2.equalsIgnoreCase("Mountains") && !value3.equalsIgnoreCase("Mountains")) {
            ContentValues values = new ContentValues();
            values.put("column1", value1);
            values.put("column2", value2);
            values.put("column3", value3);

            database.insert("MyTable", null, values);
        }
    }

    private void readFromDatabase() {
        Cursor cursor = null;
        try {
            cursor = database.query("MyTable", new String[]{"column1", "column2", "column3"}, null, null, null, null, null);
            StringBuilder builder = new StringBuilder();

            while (cursor.moveToNext()) {
                builder.append(cursor.getString(0));
                builder.append(" ");
                builder.append(cursor.getString(1));
                builder.append(" ");
                builder.append(cursor.getString(2));
                builder.append("\n");
            }

            textView.setText(builder.toString());
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void clearDatabase() {
        database.delete("MyTable", null, null);
        textView.setText("");
    }


    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
