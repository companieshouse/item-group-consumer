package uk.gov.companieshouse.itemgroupconsumer.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.companieshouse.api.model.itemgroupworkflow.ItemApi;
import uk.gov.companieshouse.itemgroupordered.Item;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "itemOptions", expression = "java(convertItemOptions(item.getItemOptions()))")
    @Mapping(target = "itemOptions.filing_history_documents", expression = "java(convertItemOptions(item.getItemOptions()))")
    ItemApi mapItemToItemApi(Item item);

//    default Map<String, Object> convertItemOptions(Map<String, String> itemOptions){
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> convertedMap;
//        try{
//            String jsonString = objectMapper.writeValueAsString(itemOptions);
//            convertedMap = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
//        }catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//        return convertedMap;
////        Map<String, Object> convertedOptions = new HashMap<>();
////        itemOptions.forEach((key, value) -> convertedOptions.put(key, (Object) value));
////        return convertedOptions;
//    }
}
