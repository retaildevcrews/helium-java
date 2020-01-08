# Helium JAVA

A reference app for using the Azure Web App for Containers service.


# Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.microsoft.com.

When you submit a pull request, a CLA-bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., label, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.


## Prerequisites

- Azure subscription with permissions to create:
  - Resource Groups, Service Principals, Keyvault, CosmosDB, App Service, Azure Container Registry, Azure Monitor
- Bash shell (tested on Mac, Ubuntu, Windows with WSL2)
  - Will not work in Cloud Shell unless you have a remote dockerd
- Azure CLI 2.0.72+ ([download](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest))
- Docker CLI ([download](https://docs.docker.com/install/))
- Java 8 above


## Setup

- Fork this repo and clone to your local machine
  - cd to the base directory of the repo

Login to Azure and select subscription

```bash

az login

# show your Azure accounts
az account list -o table

# select the Azure account
az account set -s {subscription name or Id}

```

Choose a unique DNS name

```bash

# this will be the prefix for all resources
# do not include punctuation - only use a-z and 0-9
# must be at least 5 characters long
# must start with a-z (only lowercase)
export He_Name="youruniquename"

### if true, change He_Name
az cosmosdb check-name-exists -n ${He_Name}

### if nslookup doesn't fail to resolve, change He_Name
nslookup ${He_Name}.azurewebsites.net
nslookup ${He_Name}.vault.azure.net
nslookup ${He_Name}.azurecr.io

```

Create Resource Groups

- When experimenting with this sample, you should create new resource groups to avoid accidentally deleting resources

  - If you use an existing resource group, please make sure to apply resource locks to avoid accidentally deleting resources

- You will create 3 resource groups
  - One for CosmosDB
  - One for ACR
  - One for App Service, Key Vault and Azure Monitor

```bash

# set location
export He_Location=centralus

# resource group names
export He_ACR_RG=${He_Name}-rg-acr
export He_App_RG=${He_Name}-rg-app
export He_Cosmos_RG=${He_Name}-rg-cosmos

# create the resource groups
az group create -n $He_App_RG -l $He_Location
az group create -n $He_ACR_RG -l $He_Location
az group create -n $He_Cosmos_RG -l $He_Location

```

Save your environment variables for ease of reuse and picking up where you left off.


Create and load sample data into CosmosDB

- This takes several minutes to run
- This sample is designed to use a simple dataset from IMDb of 100 movies and their associated actors and genres
  - See full explanation of data model design decisions [here:](https://github.com/4-co/imdb)

```bash

# set the other Cosmos environment variables
export He_Cosmos_URL=https://${He_Name}.documents.azure.com:443/
export He_Cosmos_DB=imdb
export He_Cosmos_Col=movies

# since spring-Boot doesnt support single collection with different document types, we will be creating separate collections for each document types
# There is a CSE feedback filed  issue on spring patch for fixing this issue

export He_Cosmos_MoviesCol=movies
export He_Cosmos_ActorsCol=actors
export He_Cosmos_GenresCol=genres


# create the CosmosDB server
az cosmosdb create -g $He_Cosmos_RG -n $He_Name

# create the database
az cosmosdb database create -d $He_Cosmos_DB -g $He_Cosmos_RG -n $He_Name

# create the collection
# 400 is the minimum RUs
# /partitionKey is the partition key
# partition key is the id mod 10
az cosmosdb collection create --throughput 400 --partition-key-path /partitionKey -g $He_Cosmos_RG -n $He_Name -d $He_Cosmos_DB -c $He_Cosmos_MoviesCol
az cosmosdb collection create --throughput 400 --partition-key-path /partitionKey -g $He_Cosmos_RG -n $He_Name -d $He_Cosmos_DB -c $He_Cosmos_ActorsCol
az cosmosdb collection create --throughput 400 --partition-key-path /partitionKey -g $He_Cosmos_RG -n $He_Name -d $He_Cosmos_DB -c $He_Cosmos_GenresCol


# get Cosmos readonly key (used by App Service)
export He_Cosmos_RO_Key=$(az cosmosdb keys list -n $He_Name -g $He_Cosmos_RG --query primaryReadonlyMasterKey -o tsv)

# get readwrite key (used by the imdb import)
export He_Cosmos_RW_Key=$(az cosmosdb keys list -n $He_Name -g $He_Cosmos_RG --query primaryMasterKey -o tsv)

# import data into movies, actors, genres collection
docker run -it --rm fourco/imdb-import $He_Name $He_Cosmos_RW_Key imdb actors genres movies

```

Create Azure Key Vault

- All secrets are stored in Azure Key Vault for security
  - This sample uses Managed Identity to access Key Vault

```bash

## create the Key Vault and add secrets
az keyvault create -g $He_App_RG -n $He_Name

# add CosmosDB keys - Java Spring-Boot Version has to follow following nomenclature

az keyvault secret set -o table --vault-name $He_Name --name "azure-cosmosdb-uri" --value $He_Cosmos_URL
az keyvault secret set -o table --vault-name $He_Name --name "azure-cosmosdb-key" --value $He_Cosmos_RO_Key
az keyvault secret set -o table --vault-name $He_Name --name "azure-cosmosdb-database" --value $He_Cosmos_DB
az keyvault secret set -o table --vault-name $He_Name --name "azure-cosmosdb-coll" --value $He_Cosmos_Col


```

(Optional) In order to run the application locally, each developer will need access to the Key Vault. Since you created the Key Vault during setup, you will automatically have permission, so this step is only required for additional developers.

Use the following command to grant permissions to each developer that will need access.

```bash

# get the object id for each developer (optional)
export dev_Object_Id=$(az ad user show --id {developer email address} --query objectId -o tsv)

# grant Key Vault access to each developer (optional)
az keyvault set-policy -n $He_Name --secret-permissions get list --key-permissions get list --object-id $dev_Object_Id


Run the application locally

# IMP: security hole in keyvault MSI
KeyVault MSI does not work locally , there is a CSE feedback issue filed on the same

To get KeyVault working locally we would need to go thru clear text route, for that update the clientid and clientkey by uncommenting these lines in application.properties and populating clientid and clientkey 
#azure.keyvault.uri=https://{kvurl}.vault.azure.net/
#azure.keyvault.client-id={client_id}
#azure.keyvault.client-key={client_key}

```bash

# run locally with unit tests
mvn clean package

# run locally without unit tests
mvn clean package -DskipTests

mvn clean spring-boot:run

# test the application
# the application takes about 10 seconds to start
curl http://localhost:8080/healthz

```


Setup Container Registry

- Create the Container Registry with admin access _disabled_

```bash

# create the ACR
az acr create --sku Standard --admin-enabled false -g $He_ACR_RG -n $He_Name

# Login to ACR
az acr login -n $He_Name

# If you get an error that the login server isn't available, it's a DNS issue that will resolve in a minute or two, just retry

# Build the container with az acr build
### Make sure you are in the src directory
az acr build -r $He_Name -t $He_Name.azurecr.io/helium-java .

```

Create Azure Monitor

- The Application Insights extension is in preview and needs to be added to the CLI

```bash

# Add App Insights extension
az extension add -n application-insights

# Create App Insights
export He_AppInsights_Key=$(az monitor app-insights component create -g $He_App_RG -l $He_Location -a $He_Name --query instrumentationKey -o tsv)

# add App Insights Key to Key Vault
az keyvault secret set -o table --vault-name $He_Name --name "AppInsightsKey" --value $He_AppInsights_Key


```

Create a Service Principal for Container Registry

- App Service will use this Service Principal to access Container Registry

```bash

# create a Service Principal
export He_SP_PWD=$(az ad sp create-for-rbac -n http://${He_Name}-acr-sp --query password -o tsv)
export He_SP_ID=$(az ad sp show --id http://${He_Name}-acr-sp --query appId -o tsv)

# get the Container Registry Id
export He_ACR_Id=$(az acr show -n $He_Name -g $He_ACR_RG --query "id" -o tsv)

# assign acrpull access to Service Principal
az role assignment create --assignee $He_SP_ID --scope $He_ACR_Id --role acrpull

# add credentials to Key Vault
az keyvault secret set -o table --vault-name $He_Name --name "AcrUserId" --value $He_SP_ID
az keyvault secret set -o table --vault-name $He_Name --name "AcrPassword" --value $He_SP_PWD


```

Create and configure App Service (Web App for Containers)

- App Service will fail to start until configured properly

```bash

# create App Service plan
az appservice plan create --sku B1 --is-linux -g $He_App_RG -n ${He_Name}-plan

# create Web App for Containers
az webapp create --deployment-container-image-name hello-world -g $He_App_RG -n $He_Name -p ${He_Name}-plan

# assign Managed Identity
export He_MSI_ID=$(az webapp identity assign -g $He_App_RG -n $He_Name --query principalId -o tsv)

# grant Key Vault access to Managed Identity
az keyvault set-policy -n $He_Name --secret-permissions get list --key-permissions get list --object-id $He_MSI_ID

### Configure Web App

# turn on CI
export He_CICD_URL=$(az webapp deployment container config -n $He_Name -g $He_App_RG --enable-cd true --query CI_CD_URL -o tsv)

# add the webhook
az acr webhook create -r $He_Name -n ${He_Name} --actions push --uri $He_CICD_URL --scope helium-java:latest

# set the Key Vault name app setting (environment variable)
az webapp config appsettings set --settings KeyVaultName=$He_Name -g $He_App_RG -n $He_Name

# turn on container logging
# this will send stdout and stderr to the logs
az webapp log config --docker-container-logging filesystem -g $He_App_RG -n $He_Name

# get the Service Principal Id and Key from Key Vault
export He_AcrUserId=$(az keyvault secret show --vault-name $He_Name --name "AcrUserId" --query id -o tsv)
export He_AcrPassword=$(az keyvault secret show --vault-name $He_Name --name "AcrPassword" --query id -o tsv)

# Optional: Run ./saveenv.sh to save latest variables

# configure the Web App to use Container Registry
# get Service Principal Id and Key from Key Vault
az webapp config container set -n $He_Name -g $He_App_RG \
-i ${He_Name}.azurecr.io/helium-java \
-r https://${He_Name}.azurecr.io \
-u "@Microsoft.KeyVault(SecretUri=${He_AcrUserId})" \
-p "@Microsoft.KeyVault(SecretUri=${He_AcrPassword})"

# restart the Web App
az webapp restart -g $He_App_RG -n $He_Name

# curl the health check endpoint
# this will eventually work, but may take a minute or two
# you may get a 403 error, if so, just run again
curl https://${He_Name}.azurewebsites.net/healthz
```

## Key concepts

This sample is Java  WebAPI application designed to "fork and code" with the following features:

- Securely build, deploy and run an App Service (Web App for Containers) application
- Use Managed Identity to securely access resources
- Securely store secrets in Key Vault
- Securely build and deploy the Docker container from Container Registry or Azure DevOps
- Connect to and query CosmosDB
- Automatically send telemetry and logs to Azure Monitor
