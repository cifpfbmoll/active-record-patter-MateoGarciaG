package org.acme.rest.json;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

@QuarkusTestResource(PostgresqlDBContainer.Initializer.class)
public class PostgresqlDBContainer {

    public static class Initializer implements QuarkusTestResourceLifecycleManager {
        // Defines the test resource interface

        // Sets PostGreSQLDB container object
        private PostgreSQLContainer postgresDBContainer;

        @Override
        public Map<String, String> start() {
            // Instantiate PostGrSQLDB container with required Docker image
            this.postgresDBContainer= new PostgreSQLContainer<>("postgres:latest").withDatabaseName("fruitsdbtest")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432, 3005);

            // withExposedPorts(): Nos permite exponer los puertos del contenedor DB. En realidad colocar el withExposedPorts -> 5432 es innecesario porque 5432 es el puerto por defecto de PostGreSQL. En cambio 3005 no, entonces si no queremos le estamos indicando que sea 5432 y 3005 los puertos expuestos a los que se pueden mapear.

            // Starts the container and waits until the container is accepting connections
            this.postgresDBContainer.start();
            return getConfigurationParameters();
        }

        private Map<String, String> getConfigurationParameters() {
            // Overrides Global Quarkus’s configuration Properties of connection to a Database to point and set database connection variables to connect the app with the container test.
            // to the container one

            final Map<String, String> conf = new HashMap<>();
            // Para que el contenedor que crea testcontainers NO SOBRESCRIBA los valores del application.properties del perfil %test. SOLO SOBRESCRIBIMOS la JDBC.URL con la de TESTCONTAINERS que ya se encargará del mapeo de los puertos entre localhost y el puerto del contenedor de la DB. Al parecer el por defecto es: localhost -> 49287 y la del contenedor DB es: 5432 y 3005
            // Por lo cual el mapeo de ambos puertos es: 49287:5432 o 49287:3005

            // Mientras que con el Contenedor que genera TESTCONTAINERS para dockerizar la API, los puertos son: 49287:8080
            conf.put("%test.quarkus.datasource.jdbc.url", this.postgresDBContainer.getJdbcUrl());
            // conf.put("%test.quarkus.datasource.username", this.postgresDBContainer.getUsername());
            // conf.put("%test.quarkus.datasource.password", this.postgresDBContainer.getPassword());
            
            // conf.put("quarkus.datasource.driver", this.mariaDBContainer.getDriverClassName());
            return conf;
        }

        @Override
        public void stop() {
            // Stops the container
            if (this.postgresDBContainer != null) {
                this.postgresDBContainer.close();
            }
        }
    }
}
