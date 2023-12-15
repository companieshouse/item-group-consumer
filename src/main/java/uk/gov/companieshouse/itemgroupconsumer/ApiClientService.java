package uk.gov.companieshouse.itemgroupconsumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;

@Component
public class ApiClientService {

    public ApiClient ApiClientService() {
        return ApiSdkManager.getSDK();
    }

    public InternalApiClient getInternalApiClient() {
        return ApiSdkManager.getPrivateSDK();
    }
}