package com.studentportal.http.courses;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;

public class UpdateCourseRequest implements HttpRequest<Void, UpdateCourseDetails> {
    @Override
    public Void makeRequest(RequestHandler callback, UpdateCourseDetails details) {
        if (callback == null || details == null) {
            System.out.println("callback and details cannot be null");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                String courseCode = details.getCourseCode();
                int studentId = details.getStudentId();
                String modType = details.getUpdateType().name();

                HttpPut httpPut = new HttpPut("http://localhost:9990/courses/" +
                        courseCode + "/" + modType + "/" + studentId);
                ResponseHandler<Void> responseHandler = new ResponseHandler<Void>() {
                    @Override
                    public Void handleResponse(HttpResponse httpResponse) throws IOException {
                        int status = httpResponse.getStatusLine().getStatusCode();
                        String message = httpResponse.getStatusLine().getReasonPhrase();
                        if (status >= 202 && status < 300) {
                            callback.onSuccess();
                        } else if (status == 409) {
                            callback.onFailure(new ClientProtocolException(
                                    "That student is already added/removed to the course"));
                        } else {
                            callback.onFailure(new ClientProtocolException(
                                    "Response: " + status + "\nReason: " + message));
                        }
                        return null;
                    }
                };
                httpClient.execute(httpPut, responseHandler);
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
