package edu.kytsmen.java.listener;

import com.mongodb.MongoClient;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.net.UnknownHostException;

/**
 * Created by dkytsmen on 4/4/17.
 */
@WebListener
public class MongoDBContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        MongoClient mongoClient = (MongoClient) servletContextEvent.getServletContext()
                .getAttribute("MONGO_CLIENT");
        mongoClient.close();
        System.out.println("MongoClient closed successfully");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {

            ServletContext servletContext = servletContextEvent.getServletContext();

            MongoClient mongoClient = new MongoClient(servletContext.getInitParameter("MONGODB_HOST"),
                    Integer.parseInt(servletContext.getInitParameter("MONGODB_PORT")));
            System.out.println("MongoClient initialized successfully");
            servletContextEvent.getServletContext().setAttribute("MONGO_CLIENT", mongoClient);
        } catch (UnknownHostException e) {
            throw new RuntimeException("MongoClient init failed");
        }
    }
}
