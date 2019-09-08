package com.studentportal.http.documents;

import com.studentportal.file_management.Document;
import com.studentportal.file_management.DocumentHelper;
import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class GetAllDocumentsRequest implements HttpRequest<List<Document>, Void> {

    public GetAllDocumentsRequest() {}

    @Override
    public List<Document> makeRequest(RequestHandler callback, Void v) {
        String json = null;
        if (callback == null) {
            System.out.println("callback is null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet("http://localhost:9990/file-mgmt/document/download/all");
                ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(final HttpResponse response) throws IOException {
                        int status = response.getStatusLine().getStatusCode();
                        String s = null;
                        if (status >= 200 && status < 300) {
                            HttpEntity entity = response.getEntity();
                            if (entity != null) {
                                s = EntityUtils.toString(entity);
                                callback.onSuccess();
                            }
                        } else {
                            callback.onFailure(new ClientProtocolException(
                                    "Unexpected response status: " + status));
                        }
                        return s;
                    }
                };
                json = httpClient.execute(httpGet, responseHandler);
            } catch (ClientProtocolException e) {
                callback.onFailure(e);
            } catch (IOException e) {
                callback.onFailure(e);
            } finally {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return DocumentHelper.extractDocumentListFromJson(json);
    }
}
