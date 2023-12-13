package uk.gov.companieshouse.itemgroupconsumer;

import java.util.Map;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.util.DataMap;

import static java.util.Collections.singletonList;
import static uk.gov.companieshouse.itemgroupconsumer.logging.LoggingUtils.getRootCause;

/**
 * The service that is responsible for sending item group workflow creation requests to the
 * Item Group Workflow API.
 */
@Component
class ItemGroupWorkflowService implements Service {

    private final Logger logger;
    private final ItemGroupRequest itemGroupRequest;

    ItemGroupWorkflowService(Logger logger, ItemGroupRequest itemGroupRequest) {
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

        try {
            ItemGroupApi itemGroupApi = ItemGroupMessageMapper.createPayload(message);
            itemGroupRequest.sendItemGroup(itemGroupApi);
        }catch (ApiErrorResponseException apiException) {
            logger.error("Error response from INTERNAL API: " + apiException, getLogMap(orderId, itemId, apiException));
            throw new RetryableException("Attempting retry due to failed response", apiException);
        } catch (Exception exception) {
            final var rootCause = getRootCause(exception);
            logger.error("NonRetryable Error: " + rootCause, getLogMap(orderId, itemId, rootCause));
            throw new NonRetryableException("ItemGroupWorkflowService.processMessage: ", rootCause);
        }
        logger.info("Item group creation request successfully sent to item-group-workflow-api.", getLogMap(orderId, itemId));
    }

    private Map<String, Object> getLogMap(final String orderId, final String itemId, final Throwable rootCause) {
        return new DataMap.Builder()
                .orderId(orderId)
                .itemId(itemId)
                .errors(singletonList(rootCause.getMessage()))
                .build()
                .getLogMap();
    }

    private Map<String, Object> getLogMap(final String orderId, final String itemId) {
        return new DataMap.Builder()
                .orderId(orderId)
                .itemId(itemId)
                .build()
                .getLogMap();
    }
}