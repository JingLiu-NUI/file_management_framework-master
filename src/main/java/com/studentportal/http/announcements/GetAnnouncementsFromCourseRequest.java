package com.studentportal.http.announcements;

import com.studentportal.announcement.Announcement;
import com.studentportal.courses.CourseHelper;
import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.xml.ws.Response;
import java.io.IOException;
import java.util.List;

public class GetAnnouncementsFromCourseRequest implements HttpRequest<List<Announcement>, Integer> {
    List<Announcement> announcementList = null;
    @Override
    public List<Announcement> makeRequest(RequestHandler callback, Integer courseId) {
        if (callback == null || courseId == null) {
            System.out.println("callback or courseId cannot be null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet("http://localhost:9990/announcements/get/all/from/" + courseId);
                ResponseHandler<String> responseHandler = httpResponse -> {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    String reason = httpResponse.getStatusLine().getReasonPhrase();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = httpResponse.getEntity();
                        String json;
                        if (entity != null) {
                            json = EntityUtils.toString(entity);
                            System.out.println(json);
                            callback.onSuccess();
                            return json;
                        }
                    } else {
                        String msg = EntityUtils.toString(httpResponse.getEntity());
                        callback.onFailure(new ClientProtocolException(reason + ": " + msg));
                    }
                    return null;
                };
                // make request
               String response = httpClient.execute(httpGet, responseHandler);
               announcementList = CourseHelper.extractAnnouncementListFromJson(response);
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
        return announcementList;
    }
}
