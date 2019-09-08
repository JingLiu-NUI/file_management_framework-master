package com.studentportal.http.assignments;

import com.studentportal.assignments.Assignment;
import com.studentportal.assignments.AssignmentHelper;
import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class GetAssignmentsByCourseIdRequest implements HttpRequest<List<Assignment>, Integer> {

    @Override
    public List<Assignment> makeRequest(RequestHandler callback, Integer courseId) {
        List<Assignment> aList = null;
        if (callback == null || courseId == null) {
            System.out.println("callback or courseId cannot be null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet("http://localhost:9990/assignment/by/" + courseId);
                ResponseHandler<String> responseHandler = httpResponse -> {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    String msg = httpResponse.getStatusLine().getReasonPhrase();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity);
                        }
                    } else {
                        callback.onFailure(new ClientProtocolException(msg));
                    }
                    return null;
                };
                String json = httpClient.execute(httpGet, responseHandler);
                aList = AssignmentHelper.extractAssignmentListFromJson(json);
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
        return aList;
    }
}
