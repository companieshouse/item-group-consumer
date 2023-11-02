package uk.gov.companieshouse.itemgroupconsumer;

import static uk.gov.companieshouse.itemgroupconsumer.ItemGroupConsumerApplication.NAMESPACE;

import java.util.Map;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.logging.util.DataMap;

/**
 * The service that is responsible for sending item group workflow creation requests to the
 * Item Group Workflow API.
 */
@Component
class ItemGroupWorkflowService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    @Override
    public void processMessage(ServiceParameters parameters) {
        final var message = parameters.getData();
        final var orderId = message.getOrderId();
        final var itemId = message.getItems().get(0).getId();

        LOGGER.info("Processing message " + message + " for order ID " + orderId +
                ", item ID " + itemId + ".", getLogMap(orderId, itemId));

        // TODO DCAC-46 Implement this.
    }

    private Map<String, Object> getLogMap(final String orderId, final String itemId) {
        return new DataMap.Builder()
            .orderId(orderId)
            .itemId(itemId)
            .build()
            .getLogMap();
    }
}