package com.polovyi.ivan.tutorials.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.TextFile;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

public class CreateCustomerTransformer extends ResponseDefinitionTransformer {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource,
            Parameters parameters) {
        String bodyAsString = request.getBodyAsString();
        CreateCustomerRequest createCustomerRequest = mapper.readValue(bodyAsString, CreateCustomerRequest.class);

        Set<ErrorResponse> errorResponses = validateRequest(createCustomerRequest, fileSource);
        if (errorResponses.isEmpty()) {
            return ResponseDefinitionBuilder.responseDefinition()
                    .withHeader("location", request.getAbsoluteUrl() + "/" + UUID.randomUUID())
                    .build();
        } else {
            return ResponseDefinitionBuilder.jsonResponse(errorResponses);
        }
    }

    @Override
    public String getName() {
        return "create-customer-transformer";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    private Set<ErrorResponse> validateRequest(CreateCustomerRequest createCustomerRequest, FileSource fileSource) {

        Set<ErrorResponse> errors = new HashSet<>();
        if (StringUtils.isBlank(createCustomerRequest.getCustomerName())) {
            errors.add(new ErrorResponse("400.01", "Field customerName is empty"));
        }
        Set<String> paymentMethods = createCustomerRequest.getPaymentMethods();
        if (paymentMethods == null || paymentMethods.isEmpty()) {
            errors.add(new ErrorResponse("400.02", "Field paymentMethods is empty"));
        } else {
            TextFile textFile = fileSource.getTextFileNamed("payment-methods.csv");
            Set<String> validPaymentTypes = textFile.readContentsAsString().lines().collect(Collectors.toSet());
            if (!validPaymentTypes.containsAll(paymentMethods)) {
                errors.add(new ErrorResponse("400.03", "Field paymentMethods contains invalid values"));
            }
        }

        return errors;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCustomerRequest {

        private String customerName;
        private Set<String> paymentMethods;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ErrorResponse {

        private String errorCode;
        private String errorMessage;
    }
}
