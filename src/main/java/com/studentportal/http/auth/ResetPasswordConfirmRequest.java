package com.studentportal.http.auth;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class ResetPasswordConfirmRequest implements HttpRequest<Void, String> {

    @Override
    public Void makeRequest(RequestHandler callback, String json) {
        if (callback == null || StringUtils.isBlank(json)) {
            System.out.println("callback or json is null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost("http://localhost:9990/auth/forgotPassword/confirm");
                ResponseHandler<Void> handler = new ResponseHandler<Void>() {
                    @Override
                    public Void handleResponse(HttpResponse httpResponse) throws IOException {
                        int status = httpResponse.getStatusLine().getStatusCode();
                        String message = httpResponse.getStatusLine().getReasonPhrase();
                        if (status >= 200 && status < 300) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(new ClientProtocolException(message));
                        }
                        return null;
                    }
                };
                StringEntity entity = new StringEntity(json);
                httpPost.addHeader("content-type", "application/json");
                httpPost.setEntity(entity);
                httpClient.execute(httpPost, handler);
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
