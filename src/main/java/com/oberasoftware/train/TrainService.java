package com.oberasoftware.train;

import com.oberasoftware.home.core.mqtt.MQTTConfiguration;
import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.train.api.ConnectionException;
import com.oberasoftware.train.api.ControllerManager;
import com.oberasoftware.train.api.TrainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@Configuration
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class })
@Import({
        MQTTConfiguration.class
})
@ComponentScan
public class TrainService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);

    public static void main(String[] args) {
        LOG.info("Starting Train Service Application container");

        SpringApplication springApplication = new SpringApplication(TrainService.class);
        springApplication.setShowBanner(false);
        ConfigurableApplicationContext context = springApplication.run(args);
        TrainController trainController = context.getBean(TrainController.class);
        context.getBean(ControllerManager.class).register(trainController);
        try {
            trainController.connect();
        } catch (ConnectionException e) {
            LOG.error("Could not connect to train controller", e);
        }
        MQTTTopicEventBus eventBus = context.getBean(MQTTTopicEventBus.class);
        eventBus.connect();
        eventBus.subscribe("/commands/ecos/#");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Killing the train controller gracefully on shutdown");
            try {
                trainController.disconnect();
            } catch (ConnectionException e) {
                LOG.error("Could not cleanly disconnect train controller", e);
            }
        }));
    }
}
