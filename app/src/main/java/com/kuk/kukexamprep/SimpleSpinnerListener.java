package com.kuk.kukexamprep;

import android.view.View;
import android.widget.AdapterView;

public class SimpleSpinnerListener implements AdapterView.OnItemSelectedListener {
    private final OnItemSelected callback;

    public interface OnItemSelected {
        void onSelect(int position);
    }

    public SimpleSpinnerListener(OnItemSelected callback) {
        this.callback = callback;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        callback.onSelect(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
