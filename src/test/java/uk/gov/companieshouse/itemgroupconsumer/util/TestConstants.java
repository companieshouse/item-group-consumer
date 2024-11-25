package uk.gov.companieshouse.itemgroupconsumer.util;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Map;

import uk.gov.companieshouse.itemgroupordered.DeliveryDetails;
import uk.gov.companieshouse.itemgroupordered.Item;
import uk.gov.companieshouse.itemgroupordered.ItemCosts;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;
import uk.gov.companieshouse.itemgroupordered.ItemLinks;
import uk.gov.companieshouse.itemgroupordered.OrderLinks;
import uk.gov.companieshouse.itemgroupordered.OrderedBy;

public class TestConstants {

  private TestConstants() {
  }

  public static final ItemGroupOrdered ITEM_GROUP_ORDERED = ItemGroupOrdered.newBuilder()
      .setOrderId("ORD-710716-964943")
      .setOrderedAt("2023-10-05T09:25:38.742")
      .setOrderedBy(new OrderedBy("someone@companieshouse.gov.uk", "eELukBtEOUfpWKQMSOLdRzDBxxx"))
      .setPaymentReference("johWoBDER4zfd9H")
      .setReference("ORD-710716-964943")
      .setTotalOrderCost("30")
      .setItems(createItems())
      .setDeliveryDetails(createDeliveryDetails())
      .setLinks(new OrderLinks("/orders/ORD-710716-964943"))
      .build();

  public static ItemGroupOrdered createItemGroupOrdered(){
    return ItemGroupOrdered.newBuilder()
            .setOrderId("ORD-710716-964943")
            .setOrderedAt("2023-10-05T09:25:38.742")
            .setOrderedBy(new OrderedBy("someone@companieshouse.gov.uk", "eELukBtEOUfpWKQMSOLdRzDBxxx"))
            .setPaymentReference("johWoBDER4zfd9H")
            .setReference("ORD-710716-964943")
            .setTotalOrderCost("30")
            .setItems(createItems())
            .setDeliveryDetails(createDeliveryDetails())
            .setLinks(new OrderLinks("/orders/ORD-710716-964943"))
            .build();
  }

  private static DeliveryDetails createDeliveryDetails(){
    return DeliveryDetails.newBuilder()
            .setAddressLine1("123 street")
            .setAddressLine2("")
            .setCountry("UK")
            .setPostalCode("PL1 2EF")
            .setForename("Tom")
            .setSurname("Sunburn")
            .setLocality("Local")
            .setPoBox("")
            .build();
  }
  private static List<Item> createItems() {
    return singletonList(new Item(
        "CERTIFIED DOCUMENTS TEST COMPANY LIMITED",
        "10371283",
        null,
        "certified copy for company 10371283",
        "certified-copy",
        createDescriptionValues(),
        "10547bd47210a6bd21b1adf7492a60081e108dc4",
        "CCD-597716-964942",
        createItemCosts(),
        createCertifiedCopyFirstFilingHistoryDocOptions(),
        "/orderable/certified-copies/CCD-597716-964942",
        "item#certified-copy",
        new ItemLinks("/orderable/certificates/CRT-175616-964942"),
        "0",
        false,
        1,
        "15"
    ));
  }

  private static Map<String, String> createDescriptionValues() {
    return Map.of("company_number", "10371283",
                  "certified-copy", "certified copy for company 10371283");
  }

  private static List<ItemCosts> createItemCosts() {
    return singletonList(new ItemCosts("15", "0", "15", "certified-copy"));
  }

  private static Map<String, String> createCertifiedCopyFirstFilingHistoryDocOptions() {
    return Map.of("filing_history_documents", "[{\"filing_history_date\":\"2023-05-18\",\"filing_history_description\":\"appoint-person-director-company-with-name-date\",\"filing_history_description_values\":{\"appointment_date\":\"2023-05-01\",\"officer_name\":\"MrTomSunburn\"},\"filing_history_id\":\"OTYxNjg0ODc0MmFkaXF6a2N4\",\"filing_history_type\":\"AP01\",\"filing_history_cost\":\"50\"}]",
            "delivery_method", "collection",
            "delivery_timescale", "standard");
  }
}
