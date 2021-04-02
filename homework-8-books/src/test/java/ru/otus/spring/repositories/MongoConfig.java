package ru.otus.spring.repositories;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import com.github.cloudyrock.spring.v5.MongockSpring5;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

@TestConfiguration
public class MongoConfig {
    private static final String CHANGE_LOGS_PATH = "ru.otus.spring.changelogs";

    @Bean
    public IMongodConfig mongodConfig(EmbeddedMongoProperties embeddedProperties) throws IOException {
        MongodConfigBuilder builder = new MongodConfigBuilder()
                .version(Version.V4_0_2)
                .cmdOptions(new MongoCmdOptionsBuilder().useNoJournal(false).build());
        int localPort = Network.getFreeServerPort();
        builder.net(new Net("127.0.0.1", localPort, Network.localhostIsIPv6()));
        EmbeddedMongoProperties.Storage storage = embeddedProperties.getStorage();
        return builder.build();
    }

    @Bean
    public MongockSpring5.MongockInitializingBeanRunner mongockInitializingBeanRunner(
            ApplicationContext springContext,
            MongoTemplate mongoTemplate) {
        return MongockSpring5.builder()
                .setDriver(SpringDataMongoV3Driver.withDefaultLock(mongoTemplate))
                .addChangeLogsScanPackage(CHANGE_LOGS_PATH)
                .setSpringContext(springContext)
                .buildInitializingBeanRunner();
    }
}
