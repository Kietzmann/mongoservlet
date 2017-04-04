package edu.kytsmen.java.servlets;

import com.mongodb.MongoClient;
import edu.kytsmen.java.dao.PersonDao;
import edu.kytsmen.java.dao.impl.PersonDaoImpl;
import edu.kytsmen.java.model.Person;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by dkytsmen on 4/4/17.
 */
@WebServlet("/addPerson")
public class AddPersonServlet extends HttpServlet {
    private static final long serialVersionUID = -7060758261496829905L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        if (name == null || name.equals("") || country == null || country.equals("")) {
            request.setAttribute("error", "Mandatory Parameters Missing");

            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/persons.jsp");
            requestDispatcher.forward(request, response);
        } else {
            Person person = new Person();
            person.setCountry(country);
            person.setName(name);

            MongoClient mongoClient = (MongoClient) request.getServletContext().getAttribute("MONGO_CLIENT");
            PersonDao personDao = new PersonDaoImpl(mongoClient);
            personDao.createPerson(person);
            System.out.println("Person Added Successfully with id = " + person.getId());
            request.setAttribute("success", "Person Added Successfully");
            List<Person> personList = personDao.readAllPerson();
            request.setAttribute("persons", personList);

            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/persons.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
