package org.tptacs.presentation.requestModels;

public class TomographyRequest {
    private byte[] tomography;
    private String title;
    private String userId;

    // Getters and Setters
    public byte[] getTomography() {
        return tomography;
    }

    public void setTomography(byte[] tomography) {
        this.tomography = tomography;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
