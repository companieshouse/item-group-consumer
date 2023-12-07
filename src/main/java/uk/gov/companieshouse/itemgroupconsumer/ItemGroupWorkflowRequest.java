package uk.gov.companieshouse.itemgroupconsumer;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemGroupWorkflowApi;
import uk.gov.companieshouse.logging.Logger;

/**
 * Service that propagates the contents of the recieved item-group-ordered kafka message
 * and then send that via a HTTP request to item-group-workflow-api
 */
@Service
public class ItemGroupWorkflowRequest {
    private final Logger logger;
    private final ApiClientService apiClientService;

    public ItemGroupWorkflowRequest(ApiClientService apiClientService, Logger logger){
        this.logger = logger;
        this.apiClientService = apiClientService;
    }


    public void sendItemGroup(ItemGroupWorkflowApi itemGroupWorkflowApi) {
        try{
            ApiResponse<Void> response = apiClientService
                    .getInternalApiClient()
                    .privateItemGroupWorkflowResourceHandler()
                    .postItemGroupWorkflow("/item-groups", itemGroupWorkflowApi)
                    .execute();
        } catch (ApiErrorResponseException ex){
            logger.error("ApiErrorResponseException: " + ex.getMessage());
        }
    }
}
