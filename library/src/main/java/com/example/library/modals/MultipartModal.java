package com.example.library.modals;

public class MultipartModal {

    private String fileKey;
    private String filePath;

    public MultipartModal() {

    }

    public MultipartModal(String fileKey, String filePath) {
        this.fileKey = fileKey;
        this.filePath = filePath;

    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
