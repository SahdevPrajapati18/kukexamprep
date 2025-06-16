package com.kuk.kukexamprep;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kuk.kukexamprep.model.PdfItem;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private List<PdfItem> pdfList;
    private OnPdfClickListener onPdfClickListener;

    public interface OnPdfClickListener {
        void onPdfClick(PdfItem pdfItem);
    }

    public PdfAdapter(List<PdfItem> pdfList, OnPdfClickListener listener) {
        this.pdfList = pdfList;
        this.onPdfClickListener = listener;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pdf, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        PdfItem pdfItem = pdfList.get(position);
        holder.bind(pdfItem);
    }

    @Override
    public int getItemCount() {
        return pdfList != null ? pdfList.size() : 0;
    }

    public void updatePdfList(List<PdfItem> newPdfList) {
        this.pdfList = newPdfList;
        notifyDataSetChanged();
    }

    class PdfViewHolder extends RecyclerView.ViewHolder {
        private ImageView pdfIcon;
        private TextView pdfTitle;
        private TextView pdfYear;
        private TextView pdfSubject;
        private ImageView downloadIcon;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfIcon = itemView.findViewById(R.id.pdfIcon);
            pdfTitle = itemView.findViewById(R.id.pdfTitle);
            pdfYear = itemView.findViewById(R.id.pdfYear);
            pdfSubject = itemView.findViewById(R.id.pdfSubject);
            downloadIcon = itemView.findViewById(R.id.downloadIcon);

            itemView.setOnClickListener(v -> {
                if (onPdfClickListener != null) {
                    onPdfClickListener.onPdfClick(pdfList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(PdfItem pdfItem) {
            pdfTitle.setText(pdfItem.getTitle());
            pdfYear.setText(pdfItem.getYear());
            pdfSubject.setText(pdfItem.getSubject());

            // Set PDF icon
            pdfIcon.setImageResource(R.drawable.ic_pdf);

            // Handle download icon click
            downloadIcon.setOnClickListener(v -> {
                // Implement download functionality
                downloadPdf(pdfItem);
            });
        }

        private void downloadPdf(PdfItem pdfItem) {
            // Implement PDF download logic here
            // You can use DownloadManager or your existing download implementation
        }
    }
}