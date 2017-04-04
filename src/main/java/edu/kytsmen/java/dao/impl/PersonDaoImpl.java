package edu.kytsmen.java.dao.impl;

import com.mongodb.*;
import edu.kytsmen.java.converter.PersonConverter;
import edu.kytsmen.java.dao.PersonDao;
import edu.kytsmen.java.model.Person;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkytsmen on 4/4/17.
 */
public class PersonDaoImpl implements PersonDao {

    private DBCollection collection;

    public PersonDaoImpl(MongoClient mongoClient) {
        this.collection = mongoClient.getDB("Java").getCollection("Persons");
    }

    @Override
    public Person createPerson(Person p) {
        DBObject doc = PersonConverter.toDBObject(p);
        this.collection.insert(doc);
        Object id = (ObjectId) doc.get("_id");
        p.setId(id.toString());
        return p;
    }

    @Override
    public void updatePerson(Person p) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(p.getId()))
                .get();
        this.collection.update(query, PersonConverter.toDBObject(p));
    }

    @Override
    public List<Person> readAllPerson() {
        List<Person> data = new ArrayList<>();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            Person p = PersonConverter.toPerson(doc);
            data.add(p);
        }
        return data;
    }

    @Override
    public void deletePerson(Person p) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(p.getId())).get();
        this.collection.remove(query);

    }

    @Override
    public Person readPerson(Person p) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(p.getId())).get();
        DBObject data = this.collection.findOne(query);
        return PersonConverter.toPerson(data);
    }
}
