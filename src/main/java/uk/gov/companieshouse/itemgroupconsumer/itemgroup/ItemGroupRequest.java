package uk.gov.companieshouse.itemgroupconsumer.itemgroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.itemgroupconsumer.ApiClientService;
import uk.gov.companieshouse.logging.Logger;

/**
 * Service that propagates the contents of the recieved item-group-ordered kafka message
 * and then send that via a HTTP request to item-group-workflow-api
 */
@Service
public class ItemGroupRequest {
    private final Logger logger;
    private final ApiClientService apiClientService;

    public ItemGroupRequest(ApiClientService apiClientService, Logger logger){
        this.logger = logger;
        this.apiClientService = apiClientService;
    }


    public void sendItemGroup(ItemGroupApi itemGroupWorkflowApi) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            logger.info("sending over: " + itemGroupWorkflowApi);
            try {
                logger.info(mapper.writeValueAsString(itemGroupWorkflowApi));
            }catch (Exception ex) {
                logger.error("uh oh! : " + ex.getMessage());
            }
            ApiResponse<Void> response = apiClientService
                    .getInternalApiClient()
                    .privateItemGroupResourceHandler()
                    .postItemGroup("/item-groups", itemGroupWorkflowApi)
                    .execute();
        } catch (ApiErrorResponseException ex){
            logger.error("ApiErrorResponseException: " + ex.getMessage() + ex.getContent());
        }
    }
}
