## Whats App

#### Development

__Prerequisites__

- Java 17
- spring boot
- docker compose

__Starting development environment__

- run `docker-compose up` in the project root. This will start the Postgres container with empty global database and an empty tenant database.

__Stopping or recreating database__

- to shut down the database containers run `docker-compose down` (all data will be lost)
- to restart the database and start from clean state run `docker-compose down && docker-compose up`

__database schema__

1. [ ] # ![](/home/samsona/Pictures/Screenshots/Screenshot from 2024-05-10 09-47-50.png "DB")

__what was developed__
```
- use profile related APIs
- reaction related APIs
- message related APIs
- chat room related APIs
- chat room member related APIs
- file/attachment related APIs
- all enums that are used are exposed in APIs

```

__possible problems / not implemented__
```
- some senarios specially all edge cases are not hanled
- not all error handlers are implmented
- some senarios are missed / or not considered

```
__Create initial tables and migrations (not implemented because of time__

- Flyway is responsible for creating and migrating the database when the application starts the flyway run and verifies if it is necessary to apply the migrations.

- Initial create migrations with proper version and structures can be found `resource/db/migration`

- To apply the migration to the global database the SQL scripts must be in the folder `resource/db/migration`

### Running Locally with local sqs queue (not Implemented because of time constraint)
1. Set active profile to `local`
This will create a local redis instance local sqs queues. You can verify the queues were created:
```shell
docker exec -it whatsapp_localstack sh
```
2. copy and paste
```shell
awslocal --endpoint-url=http://localhost:4566 sqs create-queue --queue-name whatsapp-localqueue.fifo --attributes FifoQueue=true
```

2.by entering the following you have to see the created queue:
```shell
awslocal --endpoint-url=http://localhost:4566 sqs list-queues
```
```shell
{
    "QueueUrls": [
        "http://localhost:4566/000000000000/whatsapp-localqueue.fifo"
    ]
}
```