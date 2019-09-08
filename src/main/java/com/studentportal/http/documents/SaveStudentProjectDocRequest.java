package com.studentportal.http.documents;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.xml.ws.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SaveStudentProjectDocRequest implements HttpRequest<Void, String> {
    @Override
    public Void makeRequest(RequestHandler callback, String json) {
        if (callback == null || StringUtils.isBlank(json)) {
            System.out.println("callback is null or json is empty");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost("http://localhost:9990/file-mgmt/document/student/project");
                ResponseHandler<Void> responseHandler = httpResponse -> {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    String msg = httpResponse.getStatusLine().getReasonPhrase();
                    if (status >= 200 && status < 300) {
                        callback.onSuccess();
                    } else if (status == 409) {
                        callback.onFailure(new ClientProtocolException(
                                status + ": That name is already taken. Rename the document"));
                    } else {
                        callback.onFailure(new ClientProtocolException(
                                "Unexpected status code: " + status));
                    }
                    return null;
                };
                StringEntity entity = new StringEntity(json);
                httpPost.addHeader("content-type", "application/json");
                httpPost.setEntity(entity);
                httpClient.execute(httpPost, responseHandler);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
