package pl.ztbd.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("pl.ztbd.project.oracle.repository")
@EnableCassandraRepositories("pl.ztbd.project.cassandra.repository")
public class ZtbpProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZtbpProjectApplication.class, args);
    }

}
