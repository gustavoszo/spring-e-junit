package br.com.gustavo.demo.integrationTests.testcontainers;

import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;

// Essa classe é anotada com @ContextConfiguration, indicando que deve usar a classe interna Initializer para inicializar o contexto de aplicação do Spring.
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    
    // A classe Initializer implementa a interface ApplicationContextInitializer, que permite modificar o contexto de aplicação antes que ele seja usado pelos testes.
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>  {

        // Container MySQL usando a imagem do MySQL
        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.28");

        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }

        private Map<String, String> connectionConfiguration() {
            return Map.of(
                "spring.datasource.url", mysql.getJdbcUrl(),
                "spring.datasource.username", mysql.getUsername(),
                "spring.datasource.password", mysql.getPassword()
            );
        }

        @SuppressWarnings("unchecked")
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            @SuppressWarnings("rawtypes")
            MapPropertySource testContainers = new MapPropertySource("testcontainers", (Map) connectionConfiguration());
            environment.getPropertySources().addFirst(testContainers);
        }

    }

}
