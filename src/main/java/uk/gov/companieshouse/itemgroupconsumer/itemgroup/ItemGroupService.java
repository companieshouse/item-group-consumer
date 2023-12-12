package uk.gov.companieshouse.itemgroupconsumer.itemgroup;

import java.util.Map;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.itemgroupconsumer.Service;
import uk.gov.companieshouse.itemgroupconsumer.ServiceParameters;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.util.DataMap;

/**
 * The service that is responsible for sending item group workflow creation requests to the
 * Item Group Workflow API.
 */
@Component
class ItemGroupService implements Service {

    private final Logger logger;
    private final ItemGroupRequest itemGroupRequest;

    ItemGroupService(Logger logger, ItemGroupRequest itemGroupRequest) {
        this.logger = logger;
        this.itemGroupRequest = itemGroupRequest;
    }

    @Override
    public void processMessage(ServiceParameters parameters) {
        final var message = parameters.getData();
        final var orderId = message.getOrderId();
        final var itemId = message.getItems().get(0).getId();

        logger.info("Processing message " + message + " for order ID " + orderId +
                ", item ID " + itemId + ".", getLogMap(orderId, itemId));

        try{
            ItemGroupApi itemGroupApi = ItemGroupMessageMapper.createPayload(message);
            itemGroupRequest.sendItemGroup(itemGroupApi);
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