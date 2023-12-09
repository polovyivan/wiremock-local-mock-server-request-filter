package com.polovyi.ivan.tutorials.filter;


import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilterV2;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GlobalRequestFilter implements StubRequestFilterV2 {

    private static final String MOCK_COMMAND_SEPARATOR = ":";

    @Override
    public String getName() {
        return "request-interceptor";
    }

//    @Override
//    public RequestFilterAction filter(Request request) {
//
//
//       // log.info("Request is intercepted. mock command is {}", commandHeader);
//        if (true) {
//
//            //log.info("Returning an error with the status {}", errorStatusCode);
//            return RequestFilterAction.stopWith(
//                    ResponseDefinitionBuilder.jsonResponse(new GenericResponse("Error"), 500));
//        }
//        return RequestFilterAction.continueWith(request);
//    }

    @Override
    public RequestFilterAction filter(Request request, ServeEvent serveEvent) {

        System.out.println("request.getBody() = " + request.getBody());
        return null;
    }

    public enum InterceptorCommand {
        GLOBAL
    }

    @Getter
    @AllArgsConstructor
    static class GenericResponse {

        private String errorMessage;
    }

}
