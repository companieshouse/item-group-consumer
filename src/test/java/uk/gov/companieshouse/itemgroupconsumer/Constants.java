package uk.gov.companieshouse.itemgroupconsumer;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Map;
import uk.gov.companieshouse.itemgroupordered.Item;
import uk.gov.companieshouse.itemgroupordered.ItemCosts;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;
import uk.gov.companieshouse.itemgroupordered.ItemLinks;
import uk.gov.companieshouse.itemgroupordered.OrderLinks;
import uk.gov.companieshouse.itemgroupordered.OrderedBy;

public class Constants {

  private Constants() {
  }

  public static final ItemGroupOrdered ITEM_GROUP_ORDERED = ItemGroupOrdered.newBuilder()
      .setOrderId("ORD-710716-964943")
      .setOrderedAt("2023-10-05T09:25:38.742")
      .setOrderedBy(new OrderedBy("someone@companieshouse.gov.uk", "eELukBtEOUfpWKQMSOLdRzDBxxx"))
      .setPaymentReference("johWoBDER4zfd9H")
      .setReference("ORD-710716-964943")
      .setTotalOrderCost("30")
      .setItems(createItems())
      .setLinks(new OrderLinks("/orders/ORD-710716-964943"))
      .build();

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
    return singletonList(new ItemCosts("15", "0", "15", "CERTIFIED_COPY"));
  }

  private static Map<String, String> createCertifiedCopyFirstFilingHistoryDocOptions() {
    return Map.of("filingHistoryDescriptionValues", "{\"date\":\"2019-11-10\",\"capital\":[{\"figure\":\"34,253,377\",\"currency\":\"GBP\"}]}",
                  "filingHistoryId", "OTAwMzQ1NjM2M2FkaXF6a6N4",
                  "filingHistoryType", "SH01",
                  "filingHistoryDescription", "capital-allotment-shares");
  }

}
