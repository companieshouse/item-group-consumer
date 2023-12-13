package uk.gov.companieshouse.itemgroupconsumer;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.itemgroup.PrivateItemGroupResourceHandler;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.itemgroupconsumer.ApiClientService;
import uk.gov.companieshouse.itemgroupconsumer.ItemGroupRequest;
import uk.gov.companieshouse.api.handler.itemgroup.request.PrivateItemGroup;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class ItemGroupRequestTest {
    @Mock
    private Logger logger;
    @Mock
    private ApiClientService apiClientService;
    @Mock
    private InternalApiClient internalApiClient;
    @Mock
    private PrivateItemGroup privateItemGroup;
    @Mock
    private ApiResponse<Void> satisfyItemResponse;
    @Mock
    private PrivateItemGroupResourceHandler privateItemGroupResourceHandler;
    @InjectMocks
    private ItemGroupRequest itemGroupRequest;

    @BeforeEach
    public void setup() {
        when(apiClientService.getInternalApiClient()).thenReturn(internalApiClient);
        when(internalApiClient.privateItemGroupResourceHandler()).thenReturn(privateItemGroupResourceHandler);
        when(privateItemGroupResourceHandler.postItemGroup(anyString(), any(ItemGroupApi.class))).thenReturn(privateItemGroup);
    }

    @Test
    public void testSendItemGroupSuccess() throws Exception {
        // Assert the correct ApiClient call is made once the item group creation request has been called
        ItemGroupApi itemGroupApi = new ItemGroupApi();

        itemGroupRequest.sendItemGroup(itemGroupApi);

        //TODO: remap the uri value in the postItemGroup to env var


        verify(apiClientService.getInternalApiClient().privateItemGroupResourceHandler().postItemGroup("/item-groups", itemGroupApi)).execute();
    }

    @Test
    public void testSendItemGroupApiErrorResponseException() throws Exception {
        //Simple test to ensure the error is propagated up to the calling service to be handled
        ItemGroupApi itemGroupApi = new ItemGroupApi();

        when(privateItemGroup.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () -> itemGroupRequest.sendItemGroup(itemGroupApi));
    }
}