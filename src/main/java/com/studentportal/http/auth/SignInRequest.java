package com.studentportal.http.auth;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import com.studentportal.user.User;
import com.studentportal.user.UserHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SignInRequest implements HttpRequest<User, String> {

    public SignInRequest() {}

    @Override
    public User makeRequest(RequestHandler callback, String json) {
        User user = null;
        if (callback == null || StringUtils.isBlank(json)) {
            System.out.println("callback or credentials are null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost("http://localhost:9990/auth/signIn");
                ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                    @Override
                    public String handleResponse(HttpResponse httpResponse) throws IOException {
                        int status = httpResponse.getStatusLine().getStatusCode();
                        String reason = httpResponse.getStatusLine().getReasonPhrase();
                        if (status >= 200 && status < 300) {
                            HttpEntity entity = httpResponse.getEntity();
                            if (entity != null) {
                                callback.onSuccess();
                                return EntityUtils.toString(entity);
                            }
                        } else {
                            String msg = EntityUtils.toString(httpResponse.getEntity());
                            callback.onFailure(new ClientProtocolException(reason + ": " + msg));
                        }
                        return null;
                    }
                };

                StringEntity entity = new StringEntity(json);
                httpPost.addHeader("content-type", "application/json");
                httpPost.setEntity(entity);
                String jsonResponse = httpClient.execute(httpPost, responseHandler);
                user = UserHelper.extractUserFromJson(jsonResponse);
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
        return user;
    }
}
