package com.kuk.kukexamprep;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuk.kukexamprep.model.PdfItem;

import java.util.List;

public class PdfListFragment extends Fragment {

    private static final String TAG = "PdfListFragment";

    private RecyclerView recyclerView;
    private PdfAdapter pdfAdapter;
    private List<PdfItem> pdfList;

    public static PdfListFragment newInstance(List<PdfItem> pdfList) {
        PdfListFragment fragment = new PdfListFragment();
        Bundle args = new Bundle();
        // You can pass data through Bundle if needed
        fragment.setArguments(args);
        fragment.setPdfList(pdfList);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPdfs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (pdfList != null && !pdfList.isEmpty()) {
            pdfAdapter = new PdfAdapter(pdfList, this::onPdfClick);
            recyclerView.setAdapter(pdfAdapter);
        }

        return view;
    }

    public void setPdfList(List<PdfItem> pdfList) {
        this.pdfList = pdfList;
        if (pdfAdapter != null) {
            pdfAdapter.updatePdfList(pdfList);
        }
    }

    private void onPdfClick(PdfItem pdfItem) {
        // Handle PDF click - open PDF viewer
        // Check for different activity types that might use this fragment
        try {
            if (getActivity() instanceof PyqActivity) {
                Log.d(TAG, "Opening PDF from PyqActivity: " + pdfItem.getTitle());
                ((PyqActivity) getActivity()).openPdfViewer(pdfItem);
            } else if (getActivity() instanceof NotesActivity) {
                Log.d(TAG, "Opening PDF from NotesActivity: " + pdfItem.getTitle());
                ((NotesActivity) getActivity()).openPdfViewer(pdfItem);
            } else if (getActivity() instanceof SyllabusActivity) {
                Log.d(TAG, "Opening PDF from SyllabusActivity: " + pdfItem.getTitle());
                ((SyllabusActivity) getActivity()).openPdfViewer(pdfItem);
            } else {
                // Generic fallback - you can add more activity types as needed
                // Log the activity type for debugging
                Log.w(TAG, "Unknown activity type: " +
                        (getActivity() != null ? getActivity().getClass().getSimpleName() : "null"));

                // If no specific activity is found, still try to handle the click
                // by checking if the activity has an openPdfViewer method through reflection
                handleGenericPdfClick(pdfItem);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling PDF click: " + e.getMessage(), e);
            // Fallback error handling
            handleGenericPdfClick(pdfItem);
        }
    }

    /**
     * Generic fallback method to handle PDF clicks when the activity type is not recognized
     */
    private void handleGenericPdfClick(PdfItem pdfItem) {
        try {
            // Use reflection to check if the activity has an openPdfViewer method
            if (getActivity() != null) {
                java.lang.reflect.Method method = getActivity().getClass()
                        .getMethod("openPdfViewer", PdfItem.class);
                method.invoke(getActivity(), pdfItem);
                Log.d(TAG, "Successfully called openPdfViewer via reflection");
            }
        } catch (Exception reflectionException) {
            Log.e(TAG, "Could not call openPdfViewer method: " + reflectionException.getMessage());

            // Final fallback - show a message to the user
            if (getActivity() != null) {
                android.widget.Toast.makeText(getActivity(),
                        "Unable to open PDF: " + pdfItem.getTitle(),
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        }
    }
}