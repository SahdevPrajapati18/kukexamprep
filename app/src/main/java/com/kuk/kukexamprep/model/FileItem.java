package com.kuk.kukexamprep.model;

public class FileItem {
    private String name;
    private String bucket_id;
    private String id;
    private String created_at;
    private String last_accessed_at;
    private String metadata;

    // Add more fields if needed, based on Supabase response

    // Getters and setters (or use Lombok if available)
    public String getName() {
        return name;
    }

    public String getBucket_id() {
        return bucket_id;
    }

    public String getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getLast_accessed_at() {
        return last_accessed_at;
    }

    public String getMetadata() {
        return metadata;
    }
}
