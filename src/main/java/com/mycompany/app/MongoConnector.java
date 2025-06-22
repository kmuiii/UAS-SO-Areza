package app;

import com.mongodb.client.*;
import org.bson.Document;

public class MongoConnector {
    private final MongoClient client;
    private final MongoDatabase db;
    private final MongoCollection<Document> collection;

    public MongoConnector(String uri, String dbName, String collName) {
        client = MongoClients.create(uri);
        db = client.getDatabase(dbName);
        collection = db.getCollection(collName);
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public void insert(Document doc) {
        collection.insertOne(doc);
    }

    public void update(String title, Document newData) {
        collection.updateOne(new Document("Title", title), new Document("$set", newData));
    }

    public void delete(String title) {
        collection.deleteOne(new Document("Title", title));
    }

    public FindIterable<Document> findAll() {
        return collection.find();
    }

    public void close() {
        client.close();
    }
}

