package org.acme.rest.json.service;


import org.acme.rest.json.entities.Fruit;

import javax.enterprise.context.ApplicationScoped;


import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ServiceFruit Class which will contains methods to manipulate the Fruit Entity which have PanacheEntity methods and acts like a EntityManager by itself.
 */

@ApplicationScoped
public class ServiceFruit {

    public ServiceFruit() {
    }


    public Set<Fruit> list() {
        // It's a stream because after we can transform this stream into a SET collection.
        // The collection is Set because we need to assure that the Fruits are unique objects.
        Stream<Fruit> fruitsSet = Fruit.streamAll();
        return fruitsSet.collect(Collectors.toSet());
    }

    public void add(Fruit fruit) {
        // The entity acts like a EntityManager by itself.
        // So by using the Entity itself with can add a new Entity into de Persistence Context
        fruit.persist();
    }

    public void remove(String name) {
        Fruit fruit = Fruit.find("name", name).firstResult();
        fruit.delete();
    }

    public Optional<Fruit> getFruit(String name) {
        return name.isBlank() ? Optional.ofNullable(null) : Fruit.find("name", name).firstResultOptional();

    }


}
