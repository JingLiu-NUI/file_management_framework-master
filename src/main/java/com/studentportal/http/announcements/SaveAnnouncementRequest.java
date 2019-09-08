package com.studentportal.http.announcements;

import com.studentportal.courses.Course;
import com.studentportal.courses.CourseHelper;
import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import com.studentportal.user.UserHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SaveAnnouncementRequest implements HttpRequest<Void, String> {
    @Override
    public Void makeRequest(RequestHandler callback, String json) {
        if (callback == null || StringUtils.isBlank(json)) {
            System.out.println("callback or json is null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpPost httpPost = new HttpPost("http://localhost:9990/courses/add/announcement");
                ResponseHandler<Void> responseHandler = httpResponse -> {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    String reason = httpResponse.getStatusLine().getReasonPhrase();
                    if (status >= 200 && status < 300) {
                        callback.onSuccess();
                    } else {
                        String msg = EntityUtils.toString(httpResponse.getEntity());
                        callback.onFailure(new ClientProtocolException(reason + ": " + msg));
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
