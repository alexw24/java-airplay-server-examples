package com.github.serezhka.jap2s.jmuxer;

import com.github.serezhka.jap2server.AirPlayServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@SpringBootApplication
public class JMuxerPlayerApp {

    private final AirPlayServer airPlayServer;

    @Autowired
    public JMuxerPlayerApp(@Value("${server.name}") String serverName,
                           @Value("${airplay.port}") int airPlayPort,
                           @Value("${airtunes.port}") int airTunesPort,
                           JMuxerWebSocketServer jMuxerWebSocketServer) {
        airPlayServer = new AirPlayServer(serverName, airPlayPort, airTunesPort, jMuxerWebSocketServer);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(JMuxerPlayerApp.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(args);
    }

    @PostConstruct
    private void postConstruct() throws Exception {
        airPlayServer.start();
        log.info("AirPlay server started!");
    }

    @PreDestroy
    private void preDestroy() {
        airPlayServer.stop();
        log.info("AirPlay server stopped!");
    }
}
