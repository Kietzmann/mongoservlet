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
@WebServlet("/deletePerson")
public class DeletePersonServlet extends HttpServlet {

    private static final long serialVersionUID = 6798036766148281767L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null || "".equals(id)) {
            throw new ServletException("id missing for delete operation");
        }
        MongoClient mongo = (MongoClient) request.getServletContext()
                .getAttribute("MONGO_CLIENT");
        PersonDao personDAO = new PersonDaoImpl(mongo);
        Person p = new Person();
        p.setId(id);
        personDAO.deletePerson(p);
        System.out.println("Person deleted successfully with id=" + id);
        request.setAttribute("success", "Person deleted successfully");
        List<Person> persons = personDAO.readAllPerson();
        request.setAttribute("persons", persons);

        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/persons.jsp");
        rd.forward(request, response);
    }
}
