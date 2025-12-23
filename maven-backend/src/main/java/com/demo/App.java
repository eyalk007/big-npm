package com.demo;

import org.springframework.core.SpringVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Demo Maven Backend starting...");
        logger.info("Spring Version: " + SpringVersion.getVersion());
        System.out.println("Hello from Maven Backend!");
    }
}

