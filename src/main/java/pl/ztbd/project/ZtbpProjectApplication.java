package pl.ztbd.project;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.ztbd.project.api.FlashcardsAPI;
import pl.ztbd.project.api.UserAPI;
import pl.ztbd.project.cassandra.service.CassandraFlashcardService;
import pl.ztbd.project.cassandra.service.CassandraUserService;
import pl.ztbd.project.oracle.service.OracleFlashcardsService;
import pl.ztbd.project.oracle.service.OracleUserService;
import pl.ztbd.project.process.TestScenario;

import java.util.concurrent.*;
import java.util.stream.IntStream;

@SpringBootApplication
//@EnableJpaRepositories("pl.ztbd.project.oracle.repository")
@EnableCassandraRepositories("pl.ztbd.project.cassandra.repository")
@RequiredArgsConstructor
public class ZtbpProjectApplication {

    private final CassandraFlashcardService cassandraFlashcardService;
    private final CassandraUserService cassandraUserService;

    private final OracleFlashcardsService oracleFlashcardsService;
    private final OracleUserService oracleUserService;

    public static void main(String[] args) {
        SpringApplication.run(ZtbpProjectApplication.class, args);
    }

    @PostConstruct
    public void launchTestScenario() throws InterruptedException {
        launchCassandraTestScenario();
//        launchOracleTestScenario();
    }

    public void launchCassandraTestScenario() throws InterruptedException {
        launchScenario(cassandraFlashcardService, cassandraUserService);
    }

    public void launchOracleTestScenario() throws InterruptedException {
        launchScenario(oracleFlashcardsService, oracleUserService);
    }

    private void launchScenario(FlashcardsAPI<?> flashcardsAPI, UserAPI userAPI) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 100_000; i++)
            pool.execute(new TestScenario(flashcardsAPI, userAPI));
        pool.shutdown();

    }
}
