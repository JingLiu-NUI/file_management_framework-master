package com.studentportal.file_management;

public class DocumentDetails {
    private int docId;
    private String location;
    private String newFilename;

    public DocumentDetails(int docId, String location, String newFilename) {
        this.docId = docId;
        this.location = location;
        this.newFilename = newFilename;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNewFilename() {
        return newFilename;
    }

    public void setNewFilename(String newFilename) {
        this.newFilename = newFilename;
    }
}
