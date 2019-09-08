package com.studentportal.core;

import com.studentportal.serverscheduling.JobScheduler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class Server {
    private static Logger LOG = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        LOG.info("starting server");

        ResourceConfig cnf = new ResourceConfig();
        final URI baseUri = UriBuilder.fromUri("http://localhost/")
                .port(9990)
                .build();

        cnf.packages("com.studentportal.api");
        final HttpServer srv = GrizzlyHttpServerFactory.createHttpServer(baseUri, cnf);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            @SuppressWarnings("deprecation")
            public void run() {
                srv.stop();
            }
        }, "shutdownHook"));

        try {
            srv.start();

            JobScheduler.scheduleTasks();
            Thread.currentThread().join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
