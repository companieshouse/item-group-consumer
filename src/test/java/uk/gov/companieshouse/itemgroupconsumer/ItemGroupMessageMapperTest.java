package uk.gov.companieshouse.itemgroupconsumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.itemgroup.ItemApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.itemgroupconsumer.Constants.createItemGroupOrdered;

/**
 * Tests that test the mapping of the ItemGroupOrdered kafka message content into an ItemGroupApi object
 */
@ExtendWith(MockitoExtension.class)
class ItemGroupMessageMapperTest {

    @Test
    public void testCreatePayloadValidInputShouldMapCorrectly() {
        //given a known input (ItemGroupOrdered) assert the expected ItemGroupApi object is output after mapping
        ItemGroupOrdered itemGroupOrdered = createItemGroupOrdered();
        ItemGroupApi expectedItemGroupApi = ItemGroupApiCreator.createExpectedItemGroupApi();

        ItemGroupApi actualItemGroupApi = ItemGroupMessageMapper.createPayload(itemGroupOrdered);

        // Check the top level fields
        assertEquals(actualItemGroupApi.getOrderNumber(), expectedItemGroupApi.getOrderNumber());
        assertEquals(actualItemGroupApi.getOrderedAt(), expectedItemGroupApi.getOrderedAt());
        assertEquals(actualItemGroupApi.getReference(), expectedItemGroupApi.getReference());
        assertEquals(actualItemGroupApi.getPaymentReference(), expectedItemGroupApi.getPaymentReference());
        assertEquals(actualItemGroupApi.getTotalOrderCost(), expectedItemGroupApi.getTotalOrderCost());

        //check nested objects
        assertThat(actualItemGroupApi.getOrderedBy(), samePropertyValuesAs(expectedItemGroupApi.getOrderedBy()));
        assertThat(actualItemGroupApi.getDeliveryDetails(), samePropertyValuesAs(expectedItemGroupApi.getDeliveryDetails()));
        assertThat(actualItemGroupApi.getLinks(), samePropertyValuesAs(expectedItemGroupApi.getLinks()));

        //due to item having multiple nested objects, check the content manually
        ItemApi resultItem = actualItemGroupApi.getItems().get(0);
        ItemApi expectedItem = expectedItemGroupApi.getItems().get(0);

        assertEquals(resultItem.getId(), expectedItem.getId());
        assertEquals(resultItem.getCompanyNumber(), expectedItem.getCompanyNumber());
        assertEquals(resultItem.getCompanyName(), expectedItem.getCompanyName());
        assertEquals(resultItem.getKind(), expectedItem.getKind());
        assertEquals(resultItem.getEtag(), expectedItem.getEtag());
        assertEquals(resultItem.getCustomerReference(), expectedItem.getCustomerReference());
        assertEquals(resultItem.getPostalDelivery(), expectedItem.getPostalDelivery());
        assertEquals(resultItem.getPostageCost(), expectedItem.getPostageCost());
        assertEquals(resultItem.getTotalItemCost(), expectedItem.getTotalItemCost());
        assertEquals(resultItem.getQuantity(), expectedItem.getQuantity());
        assertEquals(resultItem.getDescription(), expectedItem.getDescription());
        assertEquals(resultItem.getDescriptionIdentifier(), expectedItem.getDescriptionIdentifier());

        assertThat(resultItem.getLinks(), samePropertyValuesAs(expectedItem.getLinks()));
        assertThat(resultItem.getItemCosts().getFirst(), samePropertyValuesAs(expectedItem.getItemCosts().getFirst()));
        assertThat(resultItem.getDescriptionValues(), samePropertyValuesAs(expectedItem.getDescriptionValues()));
        assertThat(resultItem.getItemOptions(), samePropertyValuesAs(expectedItem.getItemOptions()));
    }

    @Test
    public void testCreatePayloadNullLinksApi() {
        //given OrderLinks from ItemGroupOrdered are null, check the application handles the null correctly
        ItemGroupOrdered itemGroupOrdered = createItemGroupOrdered();
        itemGroupOrdered.setLinks(null);

        ItemGroupApi actualItemGroupApi = ItemGroupMessageMapper.createPayload(itemGroupOrdered);

        assertNull(actualItemGroupApi.getLinks());
    }

    @Test
    public void testCreatePayloadNullDeliveryDetailsApi() {
        //given DeliveryDetails from ItemGroupOrdered are null, check the application handles the null correctly
        ItemGroupOrdered itemGroupOrdered = createItemGroupOrdered();
        itemGroupOrdered.setDeliveryDetails(null);

        ItemGroupApi actualItemGroupApi = ItemGroupMessageMapper.createPayload(itemGroupOrdered);

        assertNull(actualItemGroupApi.getDeliveryDetails());
    }

    @Test
    public void testCreatePayloadNullOrderedByApi() {
        //given OrderedBy from ItemGroupOrdered are null, check the application handles the null correctly
        ItemGroupOrdered itemGroupOrdered = createItemGroupOrdered();
        itemGroupOrdered.setOrderedBy(null);

        ItemGroupApi actualItemGroupApi = ItemGroupMessageMapper.createPayload(itemGroupOrdered);

        assertNull(actualItemGroupApi.getOrderedBy());
    }

    @Test
    public void testCreatePayloadNullItemCosts() {
        //given ItemCosts from ItemGroupOrdered are null, check the application handles the null correctly
        ItemGroupOrdered itemGroupOrdered = createItemGroupOrdered();
        itemGroupOrdered.getItems().get(0).setItemCosts(null);

        ItemGroupApi actualItemGroupApi = ItemGroupMessageMapper.createPayload(itemGroupOrdered);

        assertNull(actualItemGroupApi.getItems().get(0).getItemCosts());
    }

    @Test
    public void testCreatePayloadNullItemLinks() {
        //given ItemLinks from ItemGroupOrdered are null, check the application handles the null correctly
        ItemGroupOrdered itemGroupOrdered = createItemGroupOrdered();
        itemGroupOrdered.getItems().get(0).setLinks(null);

        ItemGroupApi actualItemGroupApi = ItemGroupMessageMapper.createPayload(itemGroupOrdered);

        assertNull(actualItemGroupApi.getItems().get(0).getLinks());
    }

    @Test
    public void testMapItemOptionsCatchBlock() {
        // Test that ensures if filing_history_documents cannot be parsed, it is passed
        // as a string
        Map<String, String> itemOptions = new HashMap<>();
        ItemGroupOrdered itemGroupOrdered = createItemGroupOrdered();
        String filingHistoryDocumentsValue = "invalid_json";

        itemOptions.put("filing_history_documents", filingHistoryDocumentsValue);

        itemGroupOrdered.getItems().get(0).setItemOptions(itemOptions);

        ItemGroupApi actualItemGroupApi = ItemGroupMessageMapper.createPayload(itemGroupOrdered);

        // Verify that the original value is passed along in the catch block
        assertEquals(filingHistoryDocumentsValue, actualItemGroupApi.getItems().get(0).getItemOptions().get("filing_history_documents"));

    }
}