package com.studentportal.http.reminders;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class SaveReminderRequest implements HttpRequest<Void, String> {
    public SaveReminderRequest() {}

    @Override
    public Void makeRequest(RequestHandler callback, String json) {
        if(callback == null) {
            System.out.print("callback is null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost("http://localhost:9990/reminder/setReminder");
                ResponseHandler<Void> responseHandler = new ResponseHandler<Void>() {
                    @Override
                    public Void handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                        int status = httpResponse.getStatusLine().getStatusCode();
                        if(status >= 200 && status < 300) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(new ClientProtocolException("Unexpected status code: " + status));
                        }
                        return null;
                    }
                };

                StringEntity entity = new StringEntity(json);
                httpPost.setEntity(entity);
                httpPost.addHeader("content-type", "application/json");

                httpClient.execute(httpPost, responseHandler);
            } catch(IOException e) {
                callback.onFailure(e);
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
