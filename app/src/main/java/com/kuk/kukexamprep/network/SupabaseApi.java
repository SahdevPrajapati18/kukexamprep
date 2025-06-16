package com.kuk.kukexamprep.network;

import com.kuk.kukexamprep.model.PdfItem;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface SupabaseApi {
    @Headers({
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx2enNvdm5rcnRyd3FrcXpqZXVwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY0NzY1OTEsImV4cCI6MjA2MjA1MjU5MX0.eFb5nr-KKx81Hagu203PeQh0GpADo7g7N1Bp-G0sqkY",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx2enNvdm5rcnRyd3FrcXpqZXVwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY0NzY1OTEsImV4cCI6MjA2MjA1MjU5MX0.eFb5nr-KKx81Hagu203PeQh0GpADo7g7N1Bp-G0sqkY"
    })
    @GET("rest/v1/notes?select=*")  // Replace `pdfs` with your actual table name
    Call<List<PdfItem>> getPdfs();
}
