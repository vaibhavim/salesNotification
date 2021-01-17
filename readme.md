# Sales Notification App

###This application is used to stored the sale messages and display report after specific interval. It generates a adjustment report after and pause after pause interval is reached.

### SalesNotificationController exposes 3 API

1. /rest/publish  - To publish messages to queue
2. /rest/pauseSalesNotificationService - Pause the service once pause interval is reached.
3. /rest/resumeSalesNotificationService -  Resume the service.


- This service accepts three types of messages

- Type 1 - contains details of 1 sale - Eg Apple at 20p each
{
    "messageType":"1",
    "productType":"APPLE",
    "quantity":"1",
    "value":"20",
    "operationType":""
}


- Type 2 -   contains details of multiple sale - Eg - 10 Apple at 20 p each
{
    "messageType":"2",
    "productType":"APPLE",
    "quantity":"10",
    "value":"20",
    "operationType":""
}


- Type 3 -   contains details of adjustment sale - Eg - add 10p to each sale of apple till now
{
    "messageType":"3",
    "productType":"APPLE",
    "quantity":"",
    "value":"10",
    "operationType":"add"
}


- Currently file salesMessages.txt present in /src/test/resources/ contains the test data  

- Execute the Spring Integration Test class - SalesNotificationApplicationTests.java to get the result