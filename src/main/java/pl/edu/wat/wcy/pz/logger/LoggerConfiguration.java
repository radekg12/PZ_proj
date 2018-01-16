package pl.edu.wat.wcy.pz.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerConfiguration {
    private static final Logger LOGGER = Logger.getLogger(LoggerConfiguration.class.getSimpleName(), "LogsMessages");

    public LoggerConfiguration() {
        String propertiesFileName = "logging.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "logger.config", e);
        }
        LOGGER.info("logger.success");
    }
}



