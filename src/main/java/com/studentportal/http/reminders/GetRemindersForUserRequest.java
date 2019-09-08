package com.studentportal.http.reminders;

import com.studentportal.assignments.AssignmentHelper;
import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import com.studentportal.reminders.ReminderHelper;
import com.studentportal.reminders.ReminderTypes.Reminder;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class GetRemindersForUserRequest implements HttpRequest<List<Reminder>, Integer> {
    List<Reminder> rList = null;
    @Override
    public List<Reminder> makeRequest(RequestHandler callback, Integer userId) {
        if (callback == null || userId == null) {
            System.out.println("callback or userId is null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet("http://localhost:9990/reminder/byUser/" + userId);
                ResponseHandler<String> responseHandler = httpResponse -> {
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
                };
                String json = httpClient.execute(httpGet, responseHandler);
                rList = ReminderHelper.extractReminderListFromJson(json);
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
        return rList;
    }
}
