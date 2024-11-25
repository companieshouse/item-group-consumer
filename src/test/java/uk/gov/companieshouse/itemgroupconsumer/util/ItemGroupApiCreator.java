package uk.gov.companieshouse.itemgroupconsumer.util;

import uk.gov.companieshouse.api.model.itemgroup.DeliveryDetailsApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemCostsApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemLinksApi;
import uk.gov.companieshouse.api.model.itemgroup.LinksApi;
import uk.gov.companieshouse.api.model.itemgroup.OrderedByApi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class that creates the expected ItemGroupApi object to be checked in the ItemGroupMessageMapper tests
 */
public class ItemGroupApiCreator {

    public static ItemGroupApi createExpectedItemGroupApi() {
        ItemGroupApi expectedItemGroupApi = new ItemGroupApi();
        expectedItemGroupApi.setOrderNumber("ORD-710716-964943");
        expectedItemGroupApi.setOrderedAt(LocalDateTime.parse("2023-10-05T09:25:38.742"));
        expectedItemGroupApi.setReference("ORD-710716-964943");
        expectedItemGroupApi.setPaymentReference("johWoBDER4zfd9H");
        expectedItemGroupApi.setTotalOrderCost("30");
        expectedItemGroupApi.setOrderedBy(createOrderedByApi());
        expectedItemGroupApi.setDeliveryDetails(createDeliveryDetailsApi());
        expectedItemGroupApi.setItems(createItemApis());
        expectedItemGroupApi.setLinks(createLinksApi());

        return expectedItemGroupApi;
    }

    private static OrderedByApi createOrderedByApi() {
        OrderedByApi orderedByApi = new OrderedByApi();
        orderedByApi.setEmail("someone@companieshouse.gov.uk");
        orderedByApi.setId("eELukBtEOUfpWKQMSOLdRzDBxxx");
        return orderedByApi;
    }

    private static List<ItemApi> createItemApis(){
        // Create a List of ItemApi objects
        List<ItemApi> itemApis = new ArrayList<>();
        ItemApi itemApi = new ItemApi();
        itemApi.setCompanyName("CERTIFIED DOCUMENTS TEST COMPANY LIMITED");
        itemApi.setCompanyNumber("10371283");
        itemApi.setDescription("certified copy for company 10371283");
        itemApi.setDescriptionIdentifier("certified-copy");
        itemApi.setDescriptionValues(Map.of("company_number", "10371283",
                "certified-copy", "certified copy for company 10371283"));
        itemApi.setEtag("10547bd47210a6bd21b1adf7492a60081e108dc4");
        itemApi.setId("CCD-597716-964942");
        itemApi.setItemOptions(createExpectedItemOptionsApi());
        itemApi.setKind("item#certified-copy");
        itemApi.setPostageCost("0");
        itemApi.setPostalDelivery(false);
        itemApi.setQuantity(1);
        itemApi.setTotalItemCost("15");
        itemApi.setLinks(createItemLinksApi());
        itemApi.setItemCosts(Collections.singletonList(createItemCostsApi()));

        itemApis.add(itemApi);
        return itemApis;
    }

    private static DeliveryDetailsApi createDeliveryDetailsApi() {
        DeliveryDetailsApi deliveryDetailsApi = new DeliveryDetailsApi();
        deliveryDetailsApi.setAddressLine1("123 street");
        deliveryDetailsApi.setAddressLine2("");
        deliveryDetailsApi.setCountry("UK");
        deliveryDetailsApi.setForename("Tom");
        deliveryDetailsApi.setLocality("Local");
        deliveryDetailsApi.setPoBox("");
        deliveryDetailsApi.setPostalCode("PL1 2EF");
        deliveryDetailsApi.setSurname("Sunburn");
        return deliveryDetailsApi;
    }

    private static Map<String, Object> createExpectedItemOptionsApi(){
        Map<String, Object> itemOptions = new HashMap<>();

        // Create filing_history_description_values map
        Map<String, Object> filingHistoryDescriptionValues = new HashMap<>();
        filingHistoryDescriptionValues.put("appointment_date", "2023-05-01");
        filingHistoryDescriptionValues.put("officer_name", "MrTomSunburn");

        // Create filing_history_documents map
        Map<String, Object> filingHistoryDocument = new HashMap<>();
        filingHistoryDocument.put("filing_history_date", "2023-05-18");
        filingHistoryDocument.put("filing_history_description", "appoint-person-director-company-with-name-date");
        filingHistoryDocument.put("filing_history_description_values", filingHistoryDescriptionValues);
        filingHistoryDocument.put("filing_history_id", "OTYxNjg0ODc0MmFkaXF6a2N4");
        filingHistoryDocument.put("filing_history_type", "AP01");
        filingHistoryDocument.put("filing_history_cost", "50");

        // Add values to the itemOptions map
        itemOptions.put("filing_history_documents", new Object[]{filingHistoryDocument});
        itemOptions.put("delivery_method", "collection");
        itemOptions.put("delivery_timescale", "standard");
        return itemOptions;
    }

    private static ItemLinksApi createItemLinksApi() {
        ItemLinksApi itemLinksApi = new ItemLinksApi();
        itemLinksApi.setOriginalItem("/orderable/certificates/CRT-175616-964942");
        return itemLinksApi;
    }

    private static LinksApi createLinksApi() {
        LinksApi linksApi = new LinksApi();
        linksApi.setOrder("/orders/ORD-710716-964943");
        return linksApi;
    }

    private static ItemCostsApi createItemCostsApi() {
        ItemCostsApi itemCostsApi = new ItemCostsApi();
        itemCostsApi.setCalculatedCost("15");
        itemCostsApi.setDiscountApplied("0");
        itemCostsApi.setItemCost("15");
        itemCostsApi.setProductType("certified-copy");
        return itemCostsApi;
    }
}
