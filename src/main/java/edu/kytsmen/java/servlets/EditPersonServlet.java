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

@WebServlet("/editPerson")
public class EditPersonServlet extends HttpServlet {
    private static final long serialVersionUID = -6554920927964049383L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.equals("")) {
            throw new ServletException("id missing for edit operation");
        }
        System.out.println("Person edit requested with id = " + id);
        MongoClient mongoClient = (MongoClient) req.getServletContext().getAttribute("MONGO_CLIENT");
        PersonDao personDao = new PersonDaoImpl(mongoClient);
        Person p = new Person();
        p.setId(id);
        p = personDao.readPerson(p);
        List<Person> persons = personDao.readAllPerson();
        req.setAttribute("persons", persons);
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/persons.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id"); // keep it non-editable in UI
        if (id == null || "".equals(id)) {
            throw new ServletException("id missing for edit operation");
        }

        String name = req.getParameter("name");
        String country = req.getParameter("country");

        if ((name == null || name.equals(""))
                || (country == null || country.equals(""))) {
            req.setAttribute("error", "Name and Country Can't be empty");
            MongoClient mongo = (MongoClient) req.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            PersonDao personDAO = new PersonDaoImpl(mongo);
            Person p = new Person();
            p.setId(id);
            p.setName(name);
            p.setCountry(country);
            req.setAttribute("person", p);
            List<Person> persons = personDAO.readAllPerson();
            req.setAttribute("persons", persons);

            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/persons.jsp");
            rd.forward(req, resp);
        } else {
            MongoClient mongo = (MongoClient) req.getServletContext()
                    .getAttribute("MONGO_CLIENT");
            PersonDao personDAO = new PersonDaoImpl(mongo);
            Person p = new Person();
            p.setId(id);
            p.setName(name);
            p.setCountry(country);
            personDAO.updatePerson(p);
            System.out.println("Person edited successfully with id=" + id);
            req.setAttribute("success", "Person edited successfully");
            List<Person> persons = personDAO.readAllPerson();
            req.setAttribute("persons", persons);

            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/persons.jsp");
            rd.forward(req, resp);
        }
    }
}
