package uk.gov.companieshouse.itemgroupconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.companieshouse.api.model.itemgroupworkflow.DeliveryDetailsApi;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemApi;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemCostsApi;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemGroupWorkflowApi;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemLinksApi;
import uk.gov.companieshouse.api.model.itemgroupworkflow.OrderedByApi;
import uk.gov.companieshouse.api.model.itemgroupworkflow.LinksApi;
import uk.gov.companieshouse.itemgroupordered.DeliveryDetails;
import uk.gov.companieshouse.itemgroupordered.Item;
import uk.gov.companieshouse.itemgroupordered.ItemCosts;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;
import uk.gov.companieshouse.itemgroupordered.ItemLinks;
import uk.gov.companieshouse.itemgroupordered.OrderLinks;
import uk.gov.companieshouse.itemgroupordered.OrderedBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts Avro schema (Item
 */
public class ItemGroupMessageMapper {

    public static ItemGroupWorkflowApi createPayload(ItemGroupOrdered itemGroupOrdered){
        ItemGroupWorkflowApi itemGroupWorkflowApi = new ItemGroupWorkflowApi();

        // Map the simple top-level fields
        itemGroupWorkflowApi.setOrderNumber(itemGroupOrdered.getOrderId());
        itemGroupWorkflowApi.setOrderedAt(LocalDateTime.parse(itemGroupOrdered.getOrderedAt()));
        itemGroupWorkflowApi.setReference(itemGroupOrdered.getReference());
        itemGroupWorkflowApi.setPaymentReference(itemGroupOrdered.getPaymentReference());
        itemGroupWorkflowApi.setTotalOrderCost(itemGroupOrdered.getTotalOrderCost());

        itemGroupWorkflowApi.setLinks(mapLinksApi(itemGroupOrdered.getLinks()));
        itemGroupWorkflowApi.setOrderedBy(mapOrderedByApi(itemGroupOrdered.getOrderedBy()));
        itemGroupWorkflowApi.setDeliveryDetails(mapDeliveryDetailsApi(itemGroupOrdered.getDeliveryDetails()));
        itemGroupWorkflowApi.setItems(mapItemApi(itemGroupOrdered.getItems()));


        return itemGroupWorkflowApi;
    }

    private static List<ItemApi> mapItemApi(List<Item> items) {
        List<ItemApi> itemApis = new ArrayList<>();

        for (Item item : items) {
            ItemApi itemApi = new ItemApi();
            itemApi.setCompanyName(item.getCompanyName());
            itemApi.setCompanyNumber(item.getCompanyNumber());
            itemApi.setDescription(item.getDescription());
            itemApi.setDescriptionIdentifier(item.getDescriptionIdentifier());
            itemApi.setDescriptionValues(item.getDescriptionValues());
            itemApi.setEtag(item.getEtag());
            itemApi.setId(item.getId());
            itemApi.setKind(item.getKind());
            itemApi.setPostageCost(item.getPostageCost());
            itemApi.setPostalDelivery(item.getPostalDelivery());
            itemApi.setQuantity(item.getQuantity());
            itemApi.setTotalItemCost(item.getTotalItemCost());

            itemApi.setItemCosts(mapItemCostsApi(item.getItemCosts()));
            itemApi.setItemOptions(mapItemOptions(item.getItemOptions()));
            itemApi.setLinks(mapItemLinks(item.getLinks()));

            itemApis.add(itemApi);
        }

        return itemApis;
    }

    private static ItemLinksApi mapItemLinks(ItemLinks links) {
        ItemLinksApi itemLinksApi = new ItemLinksApi();
        itemLinksApi.setOriginalItem(links.getOriginalItem());
        return itemLinksApi;
    }

    private static Map<String, Object> mapItemOptions(Map<String, String> itemOptions) {
        final Map<String, Object> itemOption = new HashMap<>();
        Object[] documentsArray;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            // The filing_history_documents key contains nested objects.
            documentsArray = objectMapper.readValue(itemOptions.get("filing_history_documents"), Object[].class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        itemOption.put("filing_history_documents", documentsArray);
        itemOption.put("delivery_method", itemOptions.get("delivery_method"));
        itemOption.put("delivery_timescale", itemOption.get("delivery_timescale"));
        return itemOption;
    }

    private static List<ItemCostsApi> mapItemCostsApi(List<ItemCosts> itemCosts) {
        //Presume that there is one item is the item costs list.
        ItemCosts itemCost = itemCosts.get(0);
        ItemCostsApi itemCostsApi = new ItemCostsApi();
        itemCostsApi.setCalculatedCost(itemCost.getCalculatedCost());
        itemCostsApi.setDiscountApplied(itemCost.getDiscountApplied());
        itemCostsApi.setItemCost(itemCost.getItemCost());
        itemCostsApi.setProductType(itemCost.getProductType());

        return Collections.singletonList(itemCostsApi);
    }

    private static OrderedByApi mapOrderedByApi(OrderedBy orderedBy) {
        OrderedByApi orderedByApi = new OrderedByApi();
        orderedByApi.setEmail(orderedBy.getEmail());
        orderedByApi.setId(orderedBy.getId());

        return orderedByApi;
    }

    public static LinksApi mapLinksApi(OrderLinks links){
        LinksApi linksApi = new LinksApi();
        linksApi.setOrder(links.getOrder());
        return linksApi;
    }

    public static DeliveryDetailsApi mapDeliveryDetailsApi(DeliveryDetails deliveryDetails){
        DeliveryDetailsApi deliveryDetailsApi = new DeliveryDetailsApi();
        deliveryDetailsApi.setAddressLine1(deliveryDetails.getAddressLine1());
        deliveryDetailsApi.setAddressLine2(deliveryDetails.getAddressLine2());
        deliveryDetailsApi.setCountry(deliveryDetails.getCountry());
        deliveryDetailsApi.setForename(deliveryDetails.getForename());
        deliveryDetailsApi.setLocality(deliveryDetails.getLocality());
        deliveryDetailsApi.setPoBox(deliveryDetails.getPoBox());
        deliveryDetailsApi.setPostalCode(deliveryDetails.getPostalCode());
        deliveryDetailsApi.setRegion(deliveryDetailsApi.getRegion());
        deliveryDetailsApi.setSurname(deliveryDetails.getSurname());

        return deliveryDetailsApi;
    }
}
