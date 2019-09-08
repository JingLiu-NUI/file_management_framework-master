package com.studentportal.interceptor;

import java.io.Console;
import java.io.File;

public class FileValidationIntercepter implements I_Intercepter {

    @Override
    public void interceptExecute(ContextFile ctxFile) {
        File file = ctxFile.getFile();
        if (file == null) {
            ctxFile.setFile(null);
        } else {
            String fileName = file.getName();
            if (fileName.contains(".exe") || fileName.contains(".php")
                    || fileName.contains(".js")
                    || fileName.contains(".asp")
                    || fileName.contains(".html")
                    || fileName.contains(".py")
                    || fileName.contains(".cs")
                    ) {
                // is not a valid filename -> for security problem.
                ctxFile.setFile(null);// if it's null, it will have a relevant output.

            } else {
                // continue to execute next codes in FileManagementUi;
            }
        }
    }
}
