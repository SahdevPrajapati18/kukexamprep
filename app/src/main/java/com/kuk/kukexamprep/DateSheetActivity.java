package com.kuk.kukexamprep;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DateSheetActivity extends AppCompatActivity {

    Spinner courseSpinner, semesterSpinner, examTypeSpinner;
    Button fetchDateSheetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_sheet);

        courseSpinner = findViewById(R.id.courseSpinner);
        semesterSpinner = findViewById(R.id.semesterSpinner);
        examTypeSpinner = findViewById(R.id.examTypeSpinner);
        fetchDateSheetBtn = findViewById(R.id.fetchDateSheetBtn);

        setupSpinners();

        fetchDateSheetBtn.setOnClickListener(v -> {
            String course = courseSpinner.getSelectedItem().toString();
            String semester = semesterSpinner.getSelectedItem().toString();
            String examType = examTypeSpinner.getSelectedItem().toString();

            Toast.makeText(this, "Fetching Date Sheet for: " + course + ", " + semester + ", " + examType, Toast.LENGTH_SHORT).show();

            // Firebase logic or Intent to another screen for viewing
        });
    }

    private void setupSpinners() {
        String[] courses = {"MCA", "MSC (CS)", "MA"};
        String[] semesters = {"1st Sem", "2nd Sem", "3rd Sem", "4th Sem", "5th Sem", "6th Sem", "7th Sem", "8th Sem"};
        String[] examTypes = {"Regular", "Reappear", "Special Chance"};

        courseSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courses));
        semesterSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesters));
        examTypeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, examTypes));
    }
}
