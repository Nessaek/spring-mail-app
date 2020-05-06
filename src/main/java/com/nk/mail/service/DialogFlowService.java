package com.nk.mail.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;
import com.google.common.collect.Maps;
import com.google.protobuf.Value;
import com.nk.mail.config.DialogFlowConfig;
import com.nk.mail.model.Delivery;
import com.nk.mail.model.Email;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class DialogFlowService {

    private DialogFlowConfig dialogFlowConfig;



    public Map<String, QueryResult> detectIntentTexts(
        List<String> texts) {
           return texts.stream().map(this::createResponse)
               .filter(Optional::isPresent)
            .map(Optional::get)
        .collect(Collectors.toMap(Map.Entry::getKey, SimpleEntry::getValue));
               }


    Optional<SimpleEntry<String, QueryResult>> createResponse(String text) {
        Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(dialogFlowConfig.getLanguageCode());

        SessionName session = SessionName.of(dialogFlowConfig.getProjectId(), dialogFlowConfig.getSessionId());

        System.out.println("Session Path: " + session.toString());

        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        Map<String, QueryResult> queryResults = Maps.newHashMap();

        try (SessionsClient sessionsClient = SessionsClient.create()) {
            // Performs the detect intent request
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

            // Display the query result
            QueryResult queryResult = response.getQueryResult();

            System.out.println("====================");
            System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
            System.out.format("Detected Intent: %s (confidence: %f)\n",
                queryResult.getIntent().getDisplayName(),
                queryResult.getIntentDetectionConfidence());
            System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());

            Optional<Delivery> delivery = extractDelivery(queryResult);;
            if(delivery.isPresent()) {

                createOrderEmail(delivery.get());
            } else {

            }
            queryResults.put(text, queryResult);

            return Optional.of(new SimpleEntry<>(text, queryResult));

        } catch (IOException e) {
            log.error("uh oh! : {}", e.getMessage());
            return Optional.empty();
        }

    }

    Optional<Delivery> extractDelivery(QueryResult queryResult) throws IOException {

        List<Optional<Delivery>> deliveryList = new ArrayList<>();
        for ( Entry<String, Value> entry : queryResult.getParameters().getFieldsMap().entrySet()) {

            if (entry.getKey().equals("order")) {

              Optional<Delivery> optionalDelivery = getDeliveries().stream()
                  .filter(delivery -> entry.getValue().getStringValue().equals(delivery.getId())).findFirst();

              deliveryList.add(optionalDelivery);

            }
        }

        return deliveryList.stream().filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    List<Delivery> getDeliveries() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("resources/data/deliveries.json");

        return objectMapper.readValue(file, new TypeReference<List<Delivery>>(){});
    }

    Email createOrderEmail(Delivery delivery) {

        String message = "Dear "
            + delivery.getCustomerName() + ". Your order number "
            + delivery.getId() + "is in "
            + delivery.getDeliveryStatus()
            + "Regards";

        return new Email(delivery.getCustomerEmailAddress(),
            "Order number " + delivery.getId(),
            message);
    }

    Email createUnknownResponse(Delivery delivery) {

        String message = "Dear "
            + delivery.getCustomerName() + ". Your order number "
            + delivery.getId() + "is in "
            + delivery.getDeliveryStatus()
            + "Regards";

        return new Email(delivery.getCustomerEmailAddress(), "Order number " + delivery.getId(), message);
    }

}
