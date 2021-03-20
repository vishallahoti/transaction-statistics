# transaction-statistics
API's to calculate realtime statistics

Curl commands to run the same:

POST endpoint:

curl --location --request POST 'http://localhost:8080/transactions' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount":20.5,
    "timestamp":123456789101123
}'


GET endpoint:
curl --location --request GET 'http://localhost:8080/statistics'



