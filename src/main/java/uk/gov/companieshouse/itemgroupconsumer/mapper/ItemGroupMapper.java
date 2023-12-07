package uk.gov.companieshouse.itemgroupconsumer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemApi;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemGroupWorkflowApi;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;
import uk.gov.companieshouse.itemgroupordered.Item;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface ItemGroupMapper {

    @Mapping(target = "orderNumber", source = "orderId")
    ItemGroupWorkflowApi itemGroupOrderedToitemGroupWorkflowApi(ItemGroupOrdered itemGroupOrdered);


//    ItemApi map(Item value);
}
