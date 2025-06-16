package com.kuk.kukexamprep.model;

public class PdfItem {
    private String title;
    private String fileName;
    private String filePath;
    private String year;
    private String subject;
    private long fileSize;

    public PdfItem(String title, String fileName, String filePath, String year, String subject) {
        this.title = title;
        this.fileName = fileName;
        this.filePath = filePath;
        this.year = year;
        this.subject = subject;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
}