package com.polovyi.ivan.tutorials;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.polovyi.ivan.tutorials.transformer.CreateCustomerTransformer;
import lombok.extern.slf4j.Slf4j;
import org.wiremock.webhooks.Webhooks;

@Slf4j
public class WiremockLocalMockServer {

    public static void main(String[] args) {
        log.info("Starting local mock server on port 8000");
        new WireMockServer(options()
                .usingFilesUnderClasspath("src/main/resources/wiremock")
                .extensions(new ResponseTemplateTransformer(false),
                        new CreateCustomerTransformer(),
                        new Webhooks()
                        )
                .port(8000)
        ).start();
    }
}
