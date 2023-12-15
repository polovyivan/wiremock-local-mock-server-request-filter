package com.polovyi.ivan.tutorials.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestWrapper;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestWrapper.Builder;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilterV2;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.polovyi.ivan.tutorials.transformer.CreateCustomerTransformer.CreateCustomerRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RequestFilterV2 implements StubRequestFilterV2 {

    private static final String INSTRUCTION_HEADER_NAME = "instruction_2";
    private static final String INSTRUCTION_HEADER_1 = "add-header";
    private static final String INSTRUCTION_HEADER_2 = "add-query-param";
    private static final String INSTRUCTION_HEADER_3 = "transform-body";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getName() {
        return "request-filter-2";
    }

    @Override
    public RequestFilterAction filter(Request request, ServeEvent serveEvent) {
        log.info("Request is in filter-2.");
        String instructionHeader = request.getHeader(INSTRUCTION_HEADER_NAME);

        if (request.containsHeader(INSTRUCTION_HEADER_NAME)) {
            Builder newRequest = RequestWrapper.create();
            if (INSTRUCTION_HEADER_1.equals(instructionHeader)) {
                newRequest.addHeader("new-header", "new-header-value");
            } else if (INSTRUCTION_HEADER_2.equals(instructionHeader)) {
                newRequest.transformAbsoluteUrl(url -> url.concat("?additionalQueryParameter=additionalQueryParameterValue"));
            } else if (INSTRUCTION_HEADER_3.equals(instructionHeader)) {
                newRequest.transformBody(this::getCreateCustomerRequest);
            }
            return RequestFilterAction.continueWith(newRequest.wrap(request));
        }
        return RequestFilterAction.continueWith(request);
    }

    @SneakyThrows
    private Body getCreateCustomerRequest(Body body) {
        CreateCustomerRequest createCustomerRequest = mapper.readValue(body.asString(), CreateCustomerRequest.class);
        createCustomerRequest.setCustomerName("NewName");
        return new Body(mapper.writeValueAsString(createCustomerRequest));
    }
}
