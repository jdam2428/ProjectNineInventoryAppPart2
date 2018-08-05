package com.example.android.projecteightinventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.projecteightinventoryapp.data.BookContract.BookEntry;
import com.example.android.projecteightinventoryapp.data.BookDbHelper;

// Code referenced from course app "Pets".
// Images sourced from https://pixabay.com/en/photos/

/**
 * Displays a list of books that are entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    /**
     * Database instances
     **/
    private SQLiteDatabase db_read;
    private SQLiteDatabase db_write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        /**
         * The database helper that provides access to the database
         */
        BookDbHelper mDbHelper = new BookDbHelper(this);
        db_read = mDbHelper.getReadableDatabase();
        db_write = mDbHelper.getWritableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the books database.
     */
    private void displayDatabaseInfo() {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRODUCT_PRICE,
                BookEntry.COLUMN_PRODUCT_QUANTITY,
                BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER};

        Cursor cursor = db_read.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = findViewById(R.id.text_view_book);

        try {
            displayView.setText("The number of books in the book table is: "
                    + cursor.getCount() + "\n\n");
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_PRODUCT_NAME + " - " +
                    BookEntry.COLUMN_PRODUCT_PRICE + " - " +
                    BookEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + "\n");

            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                double currentPrice = cursor.getDouble(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                int currentSupplierPhoneNumber = cursor.getInt(supplierPhoneNumberColumnIndex);
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhoneNumber));
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded book data into the database.
     */
    private void insertBook() {
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "The Theory of Everything");
        values.put(BookEntry.COLUMN_PRODUCT_PRICE, 10.00);
        values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, 10);
        values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "The Hawking Press");
        values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "0405562727");

        db_write.insert(BookEntry.TABLE_NAME, null, values);
    }

    /**
     * Method to delete all books data from the database.
     */
    private void deleteAllEntries() {
        db_write.delete(BookEntry.TABLE_NAME, null,
                null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_dummy_data:
                insertBook();
                displayDatabaseInfo();
                return true;
            case R.id.delete_all_entries:
                deleteAllEntries();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}