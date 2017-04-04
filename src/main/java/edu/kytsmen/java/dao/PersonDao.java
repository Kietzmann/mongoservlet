package edu.kytsmen.java.dao;

import edu.kytsmen.java.model.Person;

import java.util.List;

/**
 * Created by dkytsmen on 4/4/17.
 */
public interface PersonDao {
    Person createPerson(Person p);

    void updatePerson(Person p);

    List<Person> readAllPerson();

    void deletePerson(Person p);

    Person readPerson(Person p);
}
