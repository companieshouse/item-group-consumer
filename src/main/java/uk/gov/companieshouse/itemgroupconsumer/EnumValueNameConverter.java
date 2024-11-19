package uk.gov.companieshouse.itemgroupconsumer;

public class EnumValueNameConverter {

    private EnumValueNameConverter() { }

    public static String convertEnumValueNameToJson(final Enum value) {
        return value.name().toLowerCase().replace("_", "-");
    }

    public static String convertEnumValueNameToJson(final String value) {
        return value.toLowerCase().replace("_", "-");
    }

}
