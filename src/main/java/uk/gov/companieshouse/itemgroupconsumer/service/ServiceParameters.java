package uk.gov.companieshouse.itemgroupconsumer.service;

import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;

import java.util.Objects;

/**
 * Contains all parameters required by {@link Service service implementations}.
 */
public class ServiceParameters {

    private final ItemGroupOrdered data;

    public ServiceParameters(ItemGroupOrdered data) {
        this.data = data;
    }

    /**
     * Get data attached to the ServiceParameters object.
     *
     * @return An {@link ItemGroupOrdered} representing data that has been attached to the ServiceParameters object.
     */
    public ItemGroupOrdered getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceParameters)) {
            return false;
        }
        ServiceParameters that = (ServiceParameters) o;
        return Objects.equals(getData(), that.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData());
    }
}
