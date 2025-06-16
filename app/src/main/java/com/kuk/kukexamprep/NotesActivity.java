package com.kuk.kukexamprep;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kuk.kukexamprep.model.PdfItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "NotesActivity";
    private static final String SUPABASE_URL = "https://wrpcjqfnsdcdhztvmlfa.supabase.co";
    private static final String SUPABASE_API_KEY = BuildConfig.SUPABASE_API_KEY;

    Spinner courseSpinner, semesterSpinner, subjectSpinner;
    Button fetchNotesBtn;
    FrameLayout fragmentContainer;

    private PdfListFragment pdfListFragment;
    private final OkHttpClient client = new OkHttpClient();
    private List<String> courses = new ArrayList<>();
    private List<String> semesters = new ArrayList<>();
    private List<String> subjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        courseSpinner = findViewById(R.id.courseSpinner);
        semesterSpinner = findViewById(R.id.semesterSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        fetchNotesBtn = findViewById(R.id.fetchNotesBtn);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        // Initialize spinners with placeholder
        initializeSpinners();

        // Fetch courses from Supabase
        fetchCourses();

        // Set up spinner listeners
        setupSpinnerListeners();

        fetchNotesBtn.setOnClickListener(v -> {
            String course = courseSpinner.getSelectedItem().toString().trim();
            String semester = semesterSpinner.getSelectedItem().toString().trim();
            String subject = subjectSpinner.getSelectedItem().toString().trim();

            if (course.equals("Select Course") || semester.equals("Select Semester") || subject.equals("Select Subject")) {
                Toast.makeText(this, "Please select all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Instead of checking single file, fetch all available PDFs
            fetchAvailableNotes(course, semester, subject);
        });
    }

    private void initializeSpinners() {
        // Set initial placeholder values
        String[] placeholder = {"Loading..."};
        courseSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, placeholder));
        semesterSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, placeholder));
        subjectSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, placeholder));

        // Disable spinners until data is loaded
        semesterSpinner.setEnabled(false);
        subjectSpinner.setEnabled(false);
    }

    private void setupSpinnerListeners() {
        // Course Spinner Listener
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourse = parent.getItemAtPosition(position).toString();
                if (!selectedCourse.equals("Select Course") && !selectedCourse.equals("Loading...")) {
                    fetchSemesters(selectedCourse);
                    subjectSpinner.setEnabled(false);
                    hidePdfFragment(); // Hide fragment when course changes

                    // Reset subject spinner
                    String[] placeholder = {"Select Subject"};
                    subjectSpinner.setAdapter(new ArrayAdapter<>(NotesActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, placeholder));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Semester Spinner Listener
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSem = parent.getItemAtPosition(position).toString();
                String selectedCourse = courseSpinner.getSelectedItem().toString();

                if (!selectedSem.equals("Select Semester") && !selectedSem.equals("Loading...") &&
                        !selectedCourse.equals("Select Course") && !selectedCourse.equals("Loading...")) {
                    fetchSubjects(selectedCourse, selectedSem);
                    hidePdfFragment(); // Hide fragment when semester changes
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Subject Spinner Listener - hide fragment when subject changes
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hidePdfFragment(); // Hide fragment when subject changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void fetchAvailableNotes(String course, String semester, String subject) {
        // Show loading toast
        Toast.makeText(this, "Searching for Notes...", Toast.LENGTH_SHORT).show();

        List<PdfItem> pdfList = new ArrayList<>();

        // Define possible variations to check for notes
        String[] possibleSuffixes = {"", "_complete", "_part1", "_part2", "_unit1", "_unit2", "_unit3", "_unit4", "_summary", "_detailed","_unit1_lesson1",
                "_unit2_lesson1","_unit3_lesson1","_unit4_lesson1","_unit1_lesson2","_unit2_lesson2","_unit3_lesson2","_unit4_lesson2","_unit1_lesson3",
                "_unit2_lesson3","_unit3_lesson3","_unit4_lesson3","_unit1_lesson4","_unit2_lesson4","_unit3_lesson4","_unit4_lesson4"};
        String[] possiblePrefixes = {"", "notes_", "study_", "material_"};

        // Use AtomicInteger for thread-safe counter
        AtomicInteger completedRequests = new AtomicInteger(0);
        final int totalRequests = possibleSuffixes.length * possiblePrefixes.length + 1; // +1 for main file

        for (String prefix : possiblePrefixes) {
            for (String suffix : possibleSuffixes) {
                String fileName = prefix + subject + suffix + ".pdf";
                String fileUrl = SUPABASE_URL + "/storage/v1/object/public/notes/"
                        + Uri.encode(course) + "/"
                        + Uri.encode(semester) + "/"
                        + Uri.encode(subject) + "/"
                        + Uri.encode(fileName);

                checkPdfExists(fileUrl, subject, prefix, suffix, pdfList,
                        completedRequests, totalRequests);
            }
        }

        // Also check for the main subject PDF (original logic)
        String mainFileName = subject + ".pdf";
        String mainFileUrl = SUPABASE_URL + "/storage/v1/object/public/notes/"
                + Uri.encode(course) + "/"
                + Uri.encode(semester) + "/"
                + Uri.encode(subject) + "/"
                + Uri.encode(mainFileName);

        checkPdfExists(mainFileUrl, subject, "", "Main", pdfList,
                completedRequests, totalRequests);
    }

    private void checkPdfExists(String fileUrl, String subject, String prefix, String suffix,
                                List<PdfItem> pdfList, AtomicInteger completedRequests, int totalRequests) {
        Request request = new Request.Builder()
                .url(fileUrl)
                .head()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "PDF not found: " + fileUrl);
                synchronized (pdfList) {
                    int completed = completedRequests.incrementAndGet();
                    if (completed >= totalRequests) {
                        runOnUiThread(() -> showPdfResults(pdfList));
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    synchronized (pdfList) {
                        if (response.isSuccessful()) {
                            // File exists, add to list
                            String title = subject + " Notes";

                            if (!prefix.isEmpty()) {
                                title = prefix.replace("_", " ").trim() + " " + subject;
                            }

                            if (!suffix.isEmpty() && !suffix.equals("Main")) {
                                title += " (" + suffix.replace("_", " ").trim() + ")";
                            } else if (suffix.equals("Main")) {
                                title = subject + " - Main Notes";
                            }

                            PdfItem pdfItem = new PdfItem(
                                    title,
                                    extractFileName(fileUrl),
                                    fileUrl,
                                    "Notes", // Using "Notes" as category instead of year
                                    subject
                            );
                            pdfList.add(pdfItem);
                            Log.d(TAG, "PDF found: " + fileUrl);
                        }

                        int completed = completedRequests.incrementAndGet();
                        if (completed >= totalRequests) {
                            runOnUiThread(() -> showPdfResults(pdfList));
                        }
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    private void showPdfResults(List<PdfItem> pdfList) {
        if (pdfList.isEmpty()) {
            Toast.makeText(this, "No notes found for selected options.", Toast.LENGTH_SHORT).show();
            hidePdfFragment();
            return;
        }

        // Sort PDFs by title for better organization
        pdfList.sort((p1, p2) -> {
            // Prioritize "Main" notes first
            if (p1.getTitle().contains("Main") && !p2.getTitle().contains("Main")) {
                return -1;
            } else if (!p1.getTitle().contains("Main") && p2.getTitle().contains("Main")) {
                return 1;
            }
            return p1.getTitle().compareTo(p2.getTitle());
        });

        showPdfFragment(pdfList);
    }

    private void showPdfFragment(List<PdfItem> pdfList) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (pdfListFragment == null) {
            pdfListFragment = PdfListFragment.newInstance(pdfList);
            transaction.add(R.id.fragmentContainer, pdfListFragment);
        } else {
            pdfListFragment.setPdfList(pdfList);
        }

        // Make fragment container visible
        fragmentContainer.setVisibility(View.VISIBLE);
        transaction.commit();

        Toast.makeText(this, "Found " + pdfList.size() + " note(s)", Toast.LENGTH_SHORT).show();
    }

    private void hidePdfFragment() {
        if (fragmentContainer != null) {
            fragmentContainer.setVisibility(View.GONE);
        }
    }

    public void openPdfViewer(PdfItem pdfItem) {
        try {
            Log.d(TAG, "Opening PDF: " + pdfItem.getTitle() + " at URL: " + pdfItem.getFilePath());

            Intent intent = new Intent(this, ViewPdfActivity.class);
            intent.putExtra("pdf_url", pdfItem.getFilePath());
            intent.putExtra("pdf_title", pdfItem.getTitle());

            // Add flags to ensure proper intent handling
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        } catch (Exception e) {
            Log.e(TAG, "Error opening PDF viewer: " + e.getMessage(), e);
            Toast.makeText(this, "Error opening PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();

            // Fallback: Try to open with external app
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfItem.getFilePath()));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
            } catch (Exception browserException) {
                Log.e(TAG, "Error opening PDF in browser: " + browserException.getMessage(), browserException);
                Toast.makeText(this, "Unable to open PDF. Please check if the file exists.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private void fetchCourses() {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/courses?select=course_name")
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch courses: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(NotesActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
                    setupDefaultCourses();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);

                        courses.clear();
                        courses.add("Select Course");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject courseObj = jsonArray.getJSONObject(i);
                            courses.add(courseObj.getString("course_name"));
                        }

                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    NotesActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    courses
                            );
                            courseSpinner.setAdapter(adapter);
                        });

                    } else {
                        Log.e(TAG, "API error: " + response.code());
                        runOnUiThread(() -> {
                            Toast.makeText(NotesActivity.this, "API error: " + response.code(), Toast.LENGTH_SHORT).show();
                            setupDefaultCourses();
                        });
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    runOnUiThread(() -> {
                        Toast.makeText(NotesActivity.this, "Error parsing course data", Toast.LENGTH_SHORT).show();
                        setupDefaultCourses();
                    });
                } finally {
                    response.close();
                }
            }
        });
    }

    private void fetchSemesters(String course) {
        String url = SUPABASE_URL + "/rest/v1/semesters?course_name=eq." + Uri.encode(course) + "&select=semester_name";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_API_KEY)
                .build();

        String[] loadingPlaceholder = {"Loading..."};
        runOnUiThread(() -> {
            semesterSpinner.setAdapter(new ArrayAdapter<>(NotesActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, loadingPlaceholder));
            semesterSpinner.setEnabled(true);
        });

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch semesters: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(NotesActivity.this, "Failed to load semesters", Toast.LENGTH_SHORT).show();
                    setupDefaultSemesters(course);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);

                        semesters.clear();
                        semesters.add("Select Semester");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject semesterObj = jsonArray.getJSONObject(i);
                            semesters.add(semesterObj.getString("semester_name"));
                        }

                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    NotesActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    semesters
                            );
                            semesterSpinner.setAdapter(adapter);
                        });

                    } else {
                        Log.e(TAG, "API error: " + response.code());
                        runOnUiThread(() -> {
                            Toast.makeText(NotesActivity.this, "API error: " + response.code(), Toast.LENGTH_SHORT).show();
                            setupDefaultSemesters(course);
                        });
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    runOnUiThread(() -> {
                        Toast.makeText(NotesActivity.this, "Error parsing semester data", Toast.LENGTH_SHORT).show();
                        setupDefaultSemesters(course);
                    });
                } finally {
                    response.close();
                }
            }
        });
    }

    private void fetchSubjects(String course, String semester) {
        String url = SUPABASE_URL + "/rest/v1/subjects" +
                "?course_name=eq." + Uri.encode(course) +
                "&semester_name=eq." + Uri.encode(semester) +
                "&select=subject_name";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SUPABASE_API_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_API_KEY)
                .build();

        String[] loadingPlaceholder = {"Loading..."};
        runOnUiThread(() -> {
            subjectSpinner.setAdapter(new ArrayAdapter<>(
                    NotesActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, loadingPlaceholder));
            subjectSpinner.setEnabled(true);
        });

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch subjects: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(NotesActivity.this, "Failed to load subjects", Toast.LENGTH_SHORT).show();
                    setupDefaultSubjects(course, semester);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);

                        subjects.clear();
                        subjects.add("Select Subject");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject subjectObj = jsonArray.getJSONObject(i);
                            subjects.add(subjectObj.getString("subject_name"));
                        }

                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    NotesActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    subjects
                            );
                            subjectSpinner.setAdapter(adapter);
                        });

                    } else {
                        Log.e(TAG, "API error: " + response.code());
                        runOnUiThread(() -> {
                            Toast.makeText(NotesActivity.this, "API error: " + response.code(), Toast.LENGTH_SHORT).show();
                            setupDefaultSubjects(course, semester);
                        });
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    runOnUiThread(() -> {
                        Toast.makeText(NotesActivity.this, "Error parsing subject data", Toast.LENGTH_SHORT).show();
                        setupDefaultSubjects(course, semester);
                    });
                } finally {
                    response.close();
                }
            }
        });
    }

    // Fallback methods remain the same
    private void setupDefaultCourses() {
        String[] defaultCourses = {"Select Course", "MCA", "MSc (CS)", "MSc Chemistry"};
        courses.clear();
        for (String course : defaultCourses) {
            courses.add(course);
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    NotesActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    courses
            );
            courseSpinner.setAdapter(adapter);
        });
    }

    private void setupDefaultSemesters(String course) {
        String[] defaultSems;

        switch (course) {
            case "MCA":
            case "MSc (CS)":
            case "MSc Chemistry":
                defaultSems = new String[]{"Select Semester", "1st Sem", "2nd Sem", "3rd Sem", "4th Sem"};
                break;
            default:
                defaultSems = new String[]{"Select Semester"};
                break;
        }

        semesters.clear();
        for (String sem : defaultSems) {
            semesters.add(sem);
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    NotesActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    semesters
            );
            semesterSpinner.setAdapter(adapter);
            semesterSpinner.setEnabled(true);
        });
    }

    private void setupDefaultSubjects(String course, String semester) {
        String[] defaultSubjects;
        String key = course + "_" + semester;

        switch (key) {
            case "MCA_1st Sem":
                defaultSubjects = new String[]{"Select Subject", "DSA", "Maths"};
                break;
            case "MCA_2nd Sem":
            case "MCA_3rd Sem":
            case "MCA_4th Sem":
                defaultSubjects = new String[]{"Select Subject", "Operating System", "CN"};
                break;
            case "MSc (CS)_1st Sem":
            case "MSc (CS)_2nd Sem":
            case "MSc (CS)_3rd Sem":
            case "MSc (CS)_4th Sem":
                defaultSubjects = new String[]{"Select Subject", "Machine Learning", "Maths"};
                break;
            case "MSc Chemistry_1st Sem":
            case "MSc Chemistry_2nd Sem":
            case "MSc Chemistry_3rd Sem":
            case "MSc Chemistry_4th Sem":
                defaultSubjects = new String[]{"Select Subject", "History", "Geography"};
                break;
            default:
                defaultSubjects = new String[]{"Select Subject"};
                break;
        }

        subjects.clear();
        for (String subject : defaultSubjects) {
            subjects.add(subject);
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    NotesActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    subjects
            );
            subjectSpinner.setAdapter(adapter);
            subjectSpinner.setEnabled(true);
        });
    }
}