package com.example.android.projecteightinventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.projecteightinventoryapp.data.BookContract.BookEntry;
import com.example.android.projecteightinventoryapp.data.BookDbHelper;

// Code referenced from course app "Pets".
// Images sourced from https://pixabay.com/en/photos/

/**
 * Allows user to create a new book or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field used to enter the name of the book
     */
    private EditText mNameEditText;

    /**
     * EditText field used to enter the price of the book
     */
    private EditText mPriceEditText;

    /**
     * EditText field to enter the number of available books
     */
    private EditText mQuantityEditText;

    /**
     * EditText field to enter the book's supplier name
     */
    private EditText mSupplierNameEditText;

    /**
     * EditText field to enter the book supplier's phone number
     */
    private EditText mSupplierPhoneNumberEditText;

    /**
     * Database instance
     **/
    private SQLiteDatabase db_write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mNameEditText = findViewById(R.id.edit_book_name);
        mPriceEditText = findViewById(R.id.edit_book_price);
        mQuantityEditText = findViewById(R.id.edit_book_quantity);
        mSupplierNameEditText = findViewById(R.id.edit_book_supplier_name);
        mSupplierPhoneNumberEditText = findViewById(R.id.edit_book_supplier_phone_number);

        BookDbHelper mDbHelper = new BookDbHelper(this);

        db_write = mDbHelper.getWritableDatabase();
    }

    /**
     * Get user input from editor and save new book into database.
     */
    private void insertBook() {
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumberString = mSupplierPhoneNumberEditText.getText().toString().trim();
        Long quantity = Long.parseLong(quantityString);
        Long supplierPhoneNumber = Long.parseLong(supplierPhoneNumberString);

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(BookEntry.COLUMN_PRODUCT_PRICE, priceString);
        values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        long newRowId = db_write.insert(BookEntry.TABLE_NAME, null, values);

        // Shows a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving this book entry",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "New book entry saved with row id: "
                    + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertBook();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}