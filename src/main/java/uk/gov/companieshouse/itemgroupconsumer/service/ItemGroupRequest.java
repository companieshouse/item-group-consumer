package uk.gov.companieshouse.itemgroupconsumer.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.itemgroupconsumer.service.ApiClientService;
import uk.gov.companieshouse.logging.Logger;

/**
 * Service that propagates the contents of the received item-group-ordered kafka message
 * and then send that via an HTTP request to item-group-workflow-api
 */
@Service
public class ItemGroupRequest {
    private final Logger logger;
    private final ApiClientService apiClientService;
    public static final String itemGroupURI = "/item-groups";

    public ItemGroupRequest(ApiClientService apiClientService, Logger logger){
        this.logger = logger;
        this.apiClientService = apiClientService;
    }

    public void sendItemGroup(ItemGroupApi itemGroupWorkflowApi) throws ApiErrorResponseException {
        apiClientService
                .getInternalApiClient()
                .privateItemGroupResourceHandler()
                .postItemGroup(itemGroupURI, itemGroupWorkflowApi)
                .execute();
    }
}
