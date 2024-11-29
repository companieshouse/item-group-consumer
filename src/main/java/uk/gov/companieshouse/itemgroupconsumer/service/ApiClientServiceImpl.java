package uk.gov.companieshouse.itemgroupconsumer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;

@Component
public class ApiClientServiceImpl implements ApiClientService {

    @Value("${chs.internal-api.base-path}") String internalApiUrl;

    @Override
    public InternalApiClient getInternalApiClient() {
        InternalApiClient internalApiClient = ApiSdkManager.getPrivateSDK();
        internalApiClient.setInternalBasePath(internalApiUrl);
        internalApiClient.setBasePath(internalApiUrl);
        return internalApiClient;
    }

}