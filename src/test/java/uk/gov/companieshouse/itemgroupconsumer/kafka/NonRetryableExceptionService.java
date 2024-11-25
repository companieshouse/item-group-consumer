package uk.gov.companieshouse.itemgroupconsumer.kafka;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.itemgroupconsumer.exception.NonRetryableException;
import uk.gov.companieshouse.itemgroupconsumer.service.Service;
import uk.gov.companieshouse.itemgroupconsumer.service.ServiceParameters;

/**
 * Test implementation of {@link Service} that always throws a {@link NonRetryableException}.
 */
@Component
public class NonRetryableExceptionService implements Service {

    @Override
    public void processMessage(ServiceParameters parameters) {
        throw new NonRetryableException("Unable to handle message");
    }
}