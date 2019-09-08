package com.studentportal.interceptor;


import java.io.File;

public class ContextFile {
    File file;

    public ContextFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
