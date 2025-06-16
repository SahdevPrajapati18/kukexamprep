package com.kuk.kukexamprep;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    CardView cardPyq, cardNotes, cardDateSheets, cardSyllabus, cardNotifications1;
    Button logoutButton;
    TextView userGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userGreeting = findViewById(R.id.userGreeting);
        logoutButton = findViewById(R.id.logoutButton);

        // Load current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.reload().addOnCompleteListener(task -> {
                String name = user.getDisplayName();
                String email = user.getEmail();

                if (name != null && !name.isEmpty()) {
                    userGreeting.setText("Hey " + name + " 👋");
                } else {
                    userGreeting.setText("Hey " + email + " 👋");
                }
            });
        } else {
            userGreeting.setText("Hey there 👋");
        }

        // Initialize cards
        cardPyq = findViewById(R.id.card_pyq);
        cardNotes = findViewById(R.id.card_notes);
        cardDateSheets = findViewById(R.id.card_date_sheets);
        cardSyllabus = findViewById(R.id.card_syllabus);
        cardNotifications1 = findViewById(R.id.card_notifications1);


        // Set click listeners
        cardPyq.setOnClickListener(v -> openActivity(PyqActivity.class));
        cardNotes.setOnClickListener(v -> openActivity(NotesActivity.class));
        cardDateSheets.setOnClickListener(v -> openActivity(DateSheetActivity.class));
        cardSyllabus.setOnClickListener(v -> openActivity(SyllabusActivity.class));
        cardNotifications1.setOnClickListener(v -> openActivity(NotificationsActivity.class));


        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(DashboardActivity.this, activityClass);
        startActivity(intent);
    }
}
