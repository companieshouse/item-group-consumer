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
| `INVALID_MESSAGE_TOPIC`         | string | The topic to which consumers will republish messages if any unchecked exception other than `RetryableException` is thrown.   | `item-group-ordered-invalid` |
| `INTERNAL_API_URL`              | string | The internal api url used as the base for a http request to the item-group-workflow-api.                                     | `http:/api.chs.local:4001`   |
| `MAX_ATTEMPTS`                  | number | The maximum number of times messages will be processed before they are sent to the dead letter topic.                        | `4`                          |
| `SERVER_PORT`                   | number | Port this application runs on when deployed.                                                                                 | `18628`                      |
| `TOPIC`                         | string | The topic from which the main consumer will consume messages.                                                                | `item-group-ordered`         |

## Terraform ECS

### What does this code do?

The code present in this repository is used to define and deploy a dockerised container in AWS ECS.
This is done by calling a [module](https://github.com/companieshouse/terraform-modules/tree/main/aws/ecs) from terraform-modules. Application specific attributes are injected and the service is then deployed using Terraform via the CICD platform 'Concourse'.


Application specific attributes | Value                                | Description
:---------|:-----------------------------------------------------------------------------|:-----------
**ECS Cluster**        |order-service                                     | ECS cluster (stack) the service belongs to
**Load balancer**      |N/A - consumer service                                            | The load balancer that sits in front of the service
**Concourse pipeline**     |[Pipeline link](https://ci-platform.companieshouse.gov.uk/teams/team-development/pipelines/item-group-consumer) <br> [Pipeline code](https://github.com/companieshouse/ci-pipelines/blob/master/pipelines/ssplatform/team-development/item-group-consumer)                                  | Concourse pipeline link in shared services


### Contributing
- Please refer to the [ECS Development and Infrastructure Documentation](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/4390649858/Copy+of+ECS+Development+and+Infrastructure+Documentation+Updated) for detailed information on the infrastructure being deployed.

### Testing
- Ensure the terraform runner local plan executes without issues. For information on terraform runners please see the [Terraform Runner Quickstart guide](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/1694236886/Terraform+Runner+Quickstart).
- If you encounter any issues or have questions, reach out to the team on the **#platform** slack channel.

### Vault Configuration Updates
- Any secrets required for this service will be stored in Vault. For any updates to the Vault configuration, please consult with the **#platform** team and submit a workflow request.

### Useful Links
- [ECS service config dev repository](https://github.com/companieshouse/ecs-service-configs-dev)
- [ECS service config production repository](https://github.com/companieshouse/ecs-service-configs-production)