package uk.gov.companieshouse.itemgroupconsumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.util.DataMap;

import java.util.Map;

/**
 * Service that propagates the contents of the recieved item-group-ordered kafka message
 * and then send that via a HTTP request to item-group-workflow-api
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
