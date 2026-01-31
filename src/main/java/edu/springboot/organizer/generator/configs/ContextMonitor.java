package edu.springboot.organizer.generator.configs;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ContextMonitor {

    @Value("${spring.application.name:web-sheet}")
    private String appName;

    @PostConstruct
    public void displaySystemInfo() {
        try {
            safeRun();
        } catch (Exception ex) {
            log.error("Context info failed! {} | {}", ex.getCause(), ex.getMessage());
        }

    }

    private void safeRun() {
        Properties properties = System.getProperties();

        log.info("Display OS properties:");
        LinkedHashMap<String, String> collect = properties.entrySet().stream()
                .collect(Collectors.toMap(k -> (String) k.getKey(),
                        e -> (String) e.getValue()))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        collect.forEach((k, v) -> {
                    if (log.isDebugEnabled()) {
                        log.debug(" + [{}]=[{}]", k, v);
                    }
                }
        );

        if (log.isDebugEnabled()) {
            log.debug("ENV variables presentation:");
            Map<String, String> env = System.getenv();
            env.forEach((key, value) -> log.debug(" + [{}]=[{}]", key, value));
        }
        log.info("{} started!", (appName != null ? appName : "Not"));
    }
}
