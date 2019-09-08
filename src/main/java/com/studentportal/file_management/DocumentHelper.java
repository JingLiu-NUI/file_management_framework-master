package com.studentportal.file_management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class DocumentHelper {

    public static String convertDocToJson(Document doc) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(doc);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static List<Document> extractDocumentListFromJson(String json) {
        List<Document> docs = null;
        if (json != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                docs = mapper.readValue(json, new TypeReference<List<Document>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return docs;
    }

    public static Document extractDocumentFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Document doc = null;

        try {
            doc = mapper.readValue(json, Document.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static String extractExtension(File file) {
        return FilenameUtils.getExtension(file.getName());
    }

    public static String extractFileSizeFromBytes(byte[] bytes) {
        return FileUtils.byteCountToDisplaySize(bytes.length);
    }

    public static byte[] extractBytesFromFile(File file) {
        byte[] byteArr = null;
        try {
            byteArr = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(byteArr);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArr;
    }

    public static String extractFilename(File file) {
        String filename = FilenameUtils.getBaseName(file.getAbsolutePath());
        return filename;
    }
}
