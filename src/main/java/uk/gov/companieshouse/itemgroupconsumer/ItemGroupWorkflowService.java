package uk.gov.companieshouse.itemgroupconsumer;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemApi;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemGroupWorkflowApi;
import uk.gov.companieshouse.itemgroupconsumer.mapper.ItemGroupMapper;
import uk.gov.companieshouse.itemgroupconsumer.mapper.ItemMapper;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.util.DataMap;

/**
 * The service that is responsible for sending item group workflow creation requests to the
 * Item Group Workflow API.
 */
@Component
class ItemGroupWorkflowService implements Service {

    private final Logger logger;
    private final ItemGroupWorkflowRequest itemGroupWorkflowRequest;
    private final ItemGroupMapper mapper;

    ItemGroupWorkflowService(Logger logger, ItemGroupWorkflowRequest itemGroupWorkflowRequest, ItemGroupMapper mapper) {
        this.logger = logger;
        this.itemGroupWorkflowRequest = itemGroupWorkflowRequest;
        this.mapper = mapper;
    }

    @Override
    public void processMessage(ServiceParameters parameters) {
        final var message = parameters.getData();
        final var orderId = message.getOrderId();
        final var itemId = message.getItems().get(0).getId();

        logger.info("Processing message " + message + " for order ID " + orderId +
                ", item ID " + itemId + ".", getLogMap(orderId, itemId));

        // TODO DCAC-46 Implement this.
//        ItemGroupWorkflowApi itemGroupWorkflowApi = mapMessageToRequest(message);

        try{
            ItemGroupWorkflowApi itemGroupWorkflowApi = mapper.itemGroupOrderedToitemGroupWorkflowApi(message);
            Map<String, Object> itemOptions = itemGroupWorkflowApi.getItems().get(0).getItemOptions();
            ObjectMapper objectMapper = new ObjectMapper();
            Object filingHistoryDocuments = itemOptions.get("filing_history_documents");
            String jsonString = objectMapper.writeValueAsString(itemOptions);
            logger.info("JSON STRING: "+ jsonString);
            Map<String, Object> convertedMap =  objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
            itemGroupWorkflowApi.getItems().get(0).setItemOptions(convertedMap);

            logger.info("Converted message: " + itemGroupWorkflowApi.getItems().get(0).getItemOptions().toString());

            //consume the message
            itemGroupWorkflowRequest.sendItemGroup(itemGroupWorkflowApi);
        }catch (Exception ex){
            logger.error("error processing $$: " + ex);
        }

        //send request to item-group-workflow-api (/item-groups)
    }

    private ItemGroupWorkflowApi mapMessageToRequest(ItemGroupOrdered itemGroupOrdered){
        ItemGroupWorkflowApi itemGroupWorkflowApi = new ItemGroupWorkflowApi();
//        itemGroupWorkflowApi.setItems(itemGroupOrdered.getItems());
//        itemGroupWorkflowApi.setReference(itemGroupOrdered.getReference());
//        itemGroupWorkflowApi.setOrderNumber(itemGroupOrdered.getOrderId());
        return itemGroupWorkflowApi;
    }

    private Map<String, Object> getLogMap(final String orderId, final String itemId) {
        return new DataMap.Builder()
            .orderId(orderId)
            .itemId(itemId)
            .build()
            .getLogMap();
    }
}