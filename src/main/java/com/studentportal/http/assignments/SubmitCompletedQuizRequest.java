package com.studentportal.http.assignments;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SubmitCompletedQuizRequest implements HttpRequest<String, String> {
    @Override
    public String makeRequest(RequestHandler callback, String json) {
        String result = null;
        if (callback == null || StringUtils.isBlank(json)) {
            System.out.println("callback or json is null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost("http://localhost:9990/assignment/complete/quiz");
                ResponseHandler<String> responseHandler = httpResponse -> {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    String msg = httpResponse.getStatusLine().getReasonPhrase();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            callback.onSuccess();
                            return EntityUtils.toString(entity);
                        }
                    } else {
                        System.out.println(status);
                        callback.onFailure(new ClientProtocolException(msg));
                    }
                    return null;
                };
                StringEntity entity = new StringEntity(json);
                httpPost.addHeader("content-type", "application/json");
                httpPost.setEntity(entity);
                result = httpClient.execute(httpPost, responseHandler);
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
        return result;
    }
}
