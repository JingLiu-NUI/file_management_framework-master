package com.studentportal.interceptor;

import java.io.File;
import java.util.Date;

public class FileLoggingIntercepter implements I_Intercepter {
    @Override
    public void interceptExecute(ContextFile contextFile) {
        File file = contextFile.getFile();
        if (file == null) {
            contextFile.setFile(null);
        } else {
            Date date = new Date(System.currentTimeMillis());
            System.err.println("Date : " + date.getDay() + "/" + date.getMonth() + "/" + date.getYear() + "  " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
            System.err.println("\tFile : " + file.getName() + " is a request for trying to be uploaded .");
            System.err.println("\tFile size : " + file.length());
        }
    }
}
