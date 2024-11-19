package uk.gov.companieshouse.itemgroupconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.companieshouse.api.model.itemgroup.DeliveryDetailsApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemCostsApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemGroupApi;
import uk.gov.companieshouse.api.model.itemgroup.ItemLinksApi;
import uk.gov.companieshouse.api.model.itemgroup.OrderedByApi;
import uk.gov.companieshouse.api.model.itemgroup.LinksApi;
import uk.gov.companieshouse.itemgroupordered.DeliveryDetails;
import uk.gov.companieshouse.itemgroupordered.Item;
import uk.gov.companieshouse.itemgroupordered.ItemCosts;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;
import uk.gov.companieshouse.itemgroupordered.ItemLinks;
import uk.gov.companieshouse.itemgroupordered.OrderLinks;
import uk.gov.companieshouse.itemgroupordered.OrderedBy;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.companieshouse.itemgroupconsumer.ItemGroupConsumerApplication.APPLICATION_NAME_SPACE;
import static uk.gov.companieshouse.itemgroupconsumer.EnumValueNameConverter.convertEnumValueNameToJson;

/**
 * Maps Avro schema object (ItemGroupOrdered) into an ItemGroupApi to be used in a request to create a new
 * item group via the item-group-workflow-api
 */
public class ItemGroupMessageMapper {

    private static final Logger logger = LoggerFactory.getLogger(APPLICATION_NAME_SPACE);

    public static ItemGroupApi createPayload(ItemGroupOrdered itemGroupOrdered){
        ItemGroupApi itemGroupApi = new ItemGroupApi();

        // Map the simple top-level fields
        itemGroupApi.setOrderNumber(itemGroupOrdered.getOrderId());
        itemGroupApi.setOrderedAt(LocalDateTime.parse(itemGroupOrdered.getOrderedAt()));
        itemGroupApi.setReference(itemGroupOrdered.getReference());
        itemGroupApi.setPaymentReference(itemGroupOrdered.getPaymentReference());
        itemGroupApi.setTotalOrderCost(itemGroupOrdered.getTotalOrderCost());

        itemGroupApi.setLinks(mapLinksApi(itemGroupOrdered.getLinks()));
        itemGroupApi.setOrderedBy(mapOrderedByApi(itemGroupOrdered.getOrderedBy()));
        itemGroupApi.setDeliveryDetails(mapDeliveryDetailsApi(itemGroupOrdered.getDeliveryDetails()));
        itemGroupApi.setItems(mapItemApi(itemGroupOrdered.getItems()));

        return itemGroupApi;
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
        if (links == null)
            return null;

        ItemLinksApi itemLinksApi = new ItemLinksApi();
        itemLinksApi.setOriginalItem(links.getOriginalItem());
        return itemLinksApi;
    }

    private static Map<String, Object> mapItemOptions(Map<String, String> itemOptions) {
        final Map<String, Object> itemOption = new HashMap<>();

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            // The filing_history_documents key contains nested objects.
            Object[] documentsArray = objectMapper.readValue(itemOptions.get("filing_history_documents"), Object[].class);
            itemOption.put("filing_history_documents", documentsArray);
        } catch (Exception ex) {
            itemOption.put("filing_history_documents", itemOptions.get("filing_history_documents"));
            logger.error("Error reading 'filing_history_documents' as an object. Passing along as string.");
        }

        // Convert all other key-value pairs
        for (Map.Entry<String, String> entry : itemOptions.entrySet()) {
            String key = entry.getKey();
            if (!key.equals("filing_history_documents")) {
                itemOption.put(key, entry.getValue());
            }
        }

        return itemOption;
    }

    private static List<ItemCostsApi> mapItemCostsApi(List<ItemCosts> itemCosts) {
        if (itemCosts == null)
            return null;

        //Presume that there is one item is the item costs list.
        ItemCosts itemCost = itemCosts.getFirst();
        ItemCostsApi itemCostsApi = new ItemCostsApi();
        itemCostsApi.setCalculatedCost(itemCost.getCalculatedCost());
        itemCostsApi.setDiscountApplied(itemCost.getDiscountApplied());
        itemCostsApi.setItemCost(itemCost.getItemCost());
        itemCostsApi.setProductType(convertEnumValueNameToJson(itemCost.getProductType()));

        return Collections.singletonList(itemCostsApi);
    }

    private static OrderedByApi mapOrderedByApi(OrderedBy orderedBy) {
        if (orderedBy == null)
            return null;

        OrderedByApi orderedByApi = new OrderedByApi();
        orderedByApi.setEmail(orderedBy.getEmail());
        orderedByApi.setId(orderedBy.getId());

        return orderedByApi;
    }

    public static LinksApi mapLinksApi(OrderLinks links){
        if (links == null)
            return null;

        LinksApi linksApi = new LinksApi();
        linksApi.setOrder(links.getOrder());
        return linksApi;
    }

    public static DeliveryDetailsApi mapDeliveryDetailsApi(DeliveryDetails deliveryDetails){
        if (deliveryDetails == null)
            return null;

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
