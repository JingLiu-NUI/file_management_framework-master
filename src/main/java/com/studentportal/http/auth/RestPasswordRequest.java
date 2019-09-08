package com.studentportal.http.auth;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class RestPasswordRequest implements HttpRequest<Void, String> {
    @Override
    public Void makeRequest(RequestHandler callback, String email) {
        if (callback == null || StringUtils.isBlank(email)) {
            System.out.println("callback or email is null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet("http://localhost:9990/auth/forgotPassword/" + email);
                ResponseHandler<Void> responseHandler = new ResponseHandler<Void>() {
                    @Override
                    public Void handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
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
                httpClient.execute(httpGet, responseHandler);
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
