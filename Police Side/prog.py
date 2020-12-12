from azure.cosmos import CosmosClient
import os

url = 'https://aditya-db.documents.azure.com:443/'
key = '<AUTHTOKEN>'
client = CosmosClient(url, credential=key)
database_name = 'TsuyoiDatabase'
database = client.get_database_client(database_name)
container_name = 'HelpUserList'
container = database.get_container_client(container_name)

# Enumerate the returned items
import json
import os
s=''
for item in container.query_items(
        query='SELECT * FROM mycontainer',
        enable_cross_partition_query=True):
    s=s+(json.dumps(item, indent=True)+",\n")
s=s[0:len(s)-2]
s='['+s+']'
if(s=='[]'):
    os.system("start \"\" \"data.html\"")
#print(s)
else:
    file = open("data.json","w+")
    file.write(s)
    file.close()
    os.system("start \"\" \"data.json\"")
