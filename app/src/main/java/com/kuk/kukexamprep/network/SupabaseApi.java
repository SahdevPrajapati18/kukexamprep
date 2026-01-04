package com.kuk.kukexamprep.network;

import com.kuk.kukexamprep.model.PdfItem;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface SupabaseApi {
    @Headers({
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh2YmRmdGFodXJ6YnlhaHZ4cmRoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njc1MzA5NjUsImV4cCI6MjA4MzEwNjk2NX0.rD00jCMd9E_-Vw8RsJQ805jkL_b14v38U56ZEyU6OdY",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh2YmRmdGFodXJ6YnlhaHZ4cmRoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njc1MzA5NjUsImV4cCI6MjA4MzEwNjk2NX0.rD00jCMd9E_-Vw8RsJQ805jkL_b14v38U56ZEyU6OdY"
    })
    @GET("rest/v1/notes?select=*")  // Replace `pdfs` with your actual table name
    Call<List<PdfItem>> getPdfs();
}
