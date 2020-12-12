from azure.cosmos import exceptions, CosmosClient, PartitionKey

import uuid
import sys

nm=''
eml=''
lnk=''
argum=['','','','']
if __name__ == "__main__":
    #print(f"Arguments count: {len(sys.argv)}")
    for i, arg in enumerate(sys.argv):
        #print(f"Argument {i:>6}: {arg}")
        argum[i]=arg
    nm=argum[1]
    eml=argum[2]
    lnk=argum[3]

# Initialize the Cosmos client
endpoint = "https://aditya-db.documents.azure.com:443/"
key = '<COSMOS DB AUTHTOKEN>'

# <create_cosmos_client>
client = CosmosClient(endpoint, key)
# </create_cosmos_client>

# Create a database
# <create_database_if_not_exists>
database_name = 'TsuyoiDatabase'
database = client.create_database_if_not_exists(id=database_name)
# </create_database_if_not_exists>

# Create a container
# Using a good partition key improves the performance of database operations.
# <create_container_if_not_exists>
container_name = 'HelpUserList'
container = database.create_container_if_not_exists(
    id=container_name,
    partition_key=PartitionKey(path="/Name"),
    offer_throughput=400
)
# </create_container_if_not_exists>


# Add items to the container
user_items_to_create = [{
        'id': nm+'_'+str(uuid.uuid4()),
        'Name':nm,
        'Email':eml,
        'Map':lnk
        }]

 # <create_item>
for user_item in user_items_to_create:
    container.create_item(body=user_item)
# </create_item>


request_charge = container.client_connection.last_response_headers['x-ms-request-charge']

print('Query returned {0} items. Operation consumed {1} request units'.format(len(items), request_charge))
# </query_items>