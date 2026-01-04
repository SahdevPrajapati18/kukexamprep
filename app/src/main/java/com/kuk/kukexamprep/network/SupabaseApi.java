package com.kuk.kukexamprep.network;

import com.kuk.kukexamprep.model.PdfItem;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface SupabaseApi {
    @Headers({
            "apikey: sb_publishable_c6KvkGC_HH8g5e5N7Bd8Ew_wpNoq9V7",
            "Authorization: Bearer sb_publishable_c6KvkGC_HH8g5e5N7Bd8Ew_wpNoq9V7"
    })
    @GET("rest/v1/notes?select=*")  // Replace `pdfs` with your actual table name
    Call<List<PdfItem>> getPdfs();
}
