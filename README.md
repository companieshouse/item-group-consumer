# item-group-consumer
Consumes Kafka messages from the `item-group-ordered` topic.

## Environment variables

The following environment variables are all **mandatory**.

| Variable                        | Type   | Description                                                                                                                  | Example                      | 
|---------------------------------|--------|------------------------------------------------------------------------------------------------------------------------------|------------------------------|
| `BACKOFF_DELAY`                 | number | The delay in milliseconds between message republish attempts.                                                                | `100`                        |
| `BOOTSTRAP_SERVER_URL`          | url    | The URLs of the Kafka brokers that the consumers will connect to.                                                            | `kafka:9092`                 |
| `CONCURRENT_LISTENER_INSTANCES` | number | The number of consumers that should participate in the consumer group. Must be equal to the number of main topic partitions. | `1`                          |
| `GROUP_ID`                      | string | The group ID of the main consume.                                                                                            | `item-group-consumer`        |
| `INVALID_MESSAGE_TOPIC`         | string | The topic to which consumers will republish messages if any unchecked exception other than `RetryableException` is thrown.     | `item-group-ordered-invalid` |
| `MAX_ATTEMPTS`                  | number | The maximum number of times messages will be processed before they are sent to the dead letter topic.                        | `4`                          |
| `SERVER_PORT`                   | number | Port this application runs on when deployed.                                                                                 | `18628`                      |
| `TOPIC`                         | string | The topic from which the main consumer will consume messages.                                                                | `item-group-ordered`         |

## Endpoints

| Path                                 | Method | Description                                                         |
|--------------------------------------|--------|---------------------------------------------------------------------|
| *`/item-group-consumer/healthcheck`* | GET    | Returns HTTP OK (`200`) to indicate a healthy application instance. |
