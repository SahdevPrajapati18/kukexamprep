package com.kuk.kukexamprep;

import android.view.View;
import android.widget.AdapterView;

/**
 * A simple listener to reduce Spinner boilerplate.
 */
public abstract class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {

    public abstract void onItemSelected(String selectedItem);

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        onItemSelected(selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No action needed
    }
}
