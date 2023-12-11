package uk.gov.companieshouse.itemgroupconsumer;

import java.util.Map;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemGroupWorkflowApi;
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

    ItemGroupWorkflowService(Logger logger, ItemGroupWorkflowRequest itemGroupWorkflowRequest) {
        this.logger = logger;
        this.itemGroupWorkflowRequest = itemGroupWorkflowRequest;
    }

    @Override
    public void processMessage(ServiceParameters parameters) {
        final var message = parameters.getData();
        final var orderId = message.getOrderId();
        final var itemId = message.getItems().get(0).getId();

        logger.info("Processing message " + message + " for order ID " + orderId +
                ", item ID " + itemId + ".", getLogMap(orderId, itemId));

        try{
            ItemGroupWorkflowApi itemGroupWorkflowApi = ItemGroupMessageMapper.createPayload(message);

            logger.info("Converted message: " + itemGroupWorkflowApi.getItems().get(0).getItemOptions().toString());

            //consume the message
            itemGroupWorkflowRequest.sendItemGroup(itemGroupWorkflowApi);
        }catch (Exception ex){
            logger.error("error:" + ex.getMessage(), ex);
        }
    }

    private Map<String, Object> getLogMap(final String orderId, final String itemId) {
        return new DataMap.Builder()
            .orderId(orderId)
            .itemId(itemId)
            .build()
            .getLogMap();
    }
}