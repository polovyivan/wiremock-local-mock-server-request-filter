package com.polovyi.ivan.tutorials.filter;


import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilterV2;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GlobalRequestFilterV1 implements StubRequestFilterV2 {

    private static final String INSTRUCTION_HEADER_NAME = "instruction_1";
    private static final String INSTRUCTION_HEADER_1 = "throw-exception-400";
    private static final String INSTRUCTION_HEADER_2 = "throw-exception-500";

    @Override
    public String getName() {
        return "request-interceptor_1";
    }

    @Override
    public RequestFilterAction filter(Request request, ServeEvent serveEvent) {
        log.info("Request is intercepted_1.");

        String instructionHeader = request.getHeader(INSTRUCTION_HEADER_NAME);

        if (INSTRUCTION_HEADER_1.equals(instructionHeader)) {
            return RequestFilterAction.stopWith(ResponseDefinitionBuilder.jsonResponse("Error", 400));
        } else if (INSTRUCTION_HEADER_2.equals(instructionHeader)) {
            return RequestFilterAction.stopWith(ResponseDefinitionBuilder.jsonResponse("Error", 500));
        }

        return RequestFilterAction.continueWith(request);
    }
}
