package com;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBProvider implements AutoCloseable {

    private final MongoClient mongoClient;

    public MongoDBProvider(String connectionString) {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .codecRegistry(codecRegistry)
                .build();

        this.mongoClient = MongoClients.create(settings);
    }

    @Override
    public void close() {
        this.mongoClient.close();
    }

    public Datastore getDatastore(String database) {

        Datastore datastore = Morphia.createDatastore(mongoClient, database);

        datastore.getMapper().mapPackage("com.entity");
        datastore.ensureIndexes();

        return datastore;
    }
}
