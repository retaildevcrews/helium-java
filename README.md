# Build a Web API reference application via a Java Web API application using Managed Identity, Key Vault, and Cosmos DB that is designed to be deployed to Azure App Service or AKS

![License](https://img.shields.io/badge/license-MIT-green.svg)

This is a Java Spring Boot Web API reference application designed to "fork and code" with the following features:

- Securely build, deploy and run an App Service (Web App for Containers) application
- Use Managed Identity to securely access resources
- Securely store secrets in Key Vault
- Securely build and deploy the Docker container from Container Registry
- Connect to and query CosmosDB
- Automatically send telemetry and logs to Azure Monitor
- Instructions for setting up Key Vault, ACR, Azure Monitor and Cosmos DB are in the Helium [readme](https://github.com/retaildevcrews/helium)

## Prerequisites

- Azure subscription with permissions to create:
  - Resource Groups, Service Principals, Keyvault, CosmosDB, App Service, Azure Container Registry, Azure Monitor
- Bash shell (tested on Mac, Ubuntu, Windows with WSL2)
  - Will not work in Cloud Shell unless you have a remote dockerd
  - Will not work in WSL1
- Azure CLI ([download](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest))
- Docker CLI ([download](https://docs.docker.com/install/))
- Java 8 above ([download](https://www.java.com/en/download/manual.jsp/)) 
- JQ ([download](https://stedolan.github.io/jq/download/))
- Maven ([download](https://maven.apache.org/download.cgi))
- Completion of the Key Vault, ACR, Azure Monitor and Cosmos DB setup in the Helium [readme](https://github.com/retaildevcrews/helium)
- Visual Studio Code (optional) ([download](https://code.visualstudio.com/download))

## Setup

The application requires Key Vault and Cosmos DB to be setup per the Helium [readme](https://github.com/retaildevcrews/helium)

- Fork this repo and clone to your local machine
  - cd to the base directory of the repo
  - All instructions assume starting from the root of the repo

### Build the container using Docker
- The unit tests run as part of the Docker build process. You can also run the unit tests manually.
- For instructions on building the container with ACR, please see the Helium [readme](https://github.com/retaildevcrews/helium)

```bash
# Make sure you are in the root of the repo
# Build the image - this can take 3-5 minutes

docker build -t helium-dev .
```

- Run the application locally and clean previous packages and unit tests

- The application requires Key Vault and Cosmos DB to be setup per the Helium [readme](https://github.com/retaildevcrews/helium)
  - You can run the application locally by using Azure CLI cached credentials
    - You must run az login before this will work
```bash
az login

# show your Azure accounts
az account list -o table

# select the Azure account
az account set -s {subscription name or Id}

# Make sure you are in the root of the repo

export AUTH_TYPE=CLI
export KEYVAULT_NAME="YOUR_KEYVAULT_NAME"

# This command takes about 3-5 minutes:

mvn clean package 

mvn spring-boot:run

# When the previous command shows 'Netty started on port(s): 8080' 
# test the application (takes about 10 seconds to start) in a new window

curl http://localhost:8080/healthz

# Stop the application by typing Ctrl-C in the Azure CLI terminal window
# Or :::::

mvn spring-boot:stop

```

- Run the application as a local container instead

```bash

# Make sure you are in the root of the repo
# Run the command below to stop the previous instance and free up port 8080:

mvn spring-boot:stop 

# You can also hit Ctrl-C from the az cli window

# Build the image and run the container using Docker 

# make sure you are in the root of the repo

# this will use Azure CLI cached credentials

# build the image

docker build -t helium-dev .

# Then run the container without Appinsights.  To run with Appinsights see next command.  

docker run -p4120:4120 --env AUTH_TYPE=CLI --env KEYVAULT_NAME=$KEYVAULT_NAME -v ~/.azure:/home/helium/.azure helium-dev:latest

# To run with the App-insights instrumentation key first btain APPLICATIONINSIGHTS_CONNECTION_STRING using the following link:

https://docs.microsoft.com/en-us/azure/azure-monitor/app/java-get-started?tabs=maven

# Set the env variable for the instrumentation key 

export APP_INSIGHTS_CONNECTION_STRING="$(az monitor app-insights component show --app inst2-appinsights --resource-group wsrtf-app-rg --output yaml|grep instrumentationKey|awk '{print $2}')"

# The run the container with Appinsights

docker run -p4120:4120 --env AUTH_TYPE=CLI --env KEYVAULT_NAME=$KEYVAULT_NAME --env APPLICATIONINSIGHTS_CONNECTION_STRING=$APP_INSIGHTS_CONNECTION_STRING -v ~/.azure:/home/helium/.azure helium-dev:latest

# Check the logs to ensure the container is properly running
# Re-run until the application started message appears
docker ps -a 
docker logs container_id

# test the application
# the application takes about 10 seconds to start and you may have to run the below command more than once
curl http://localhost:4120/healthz

# Clean up  - stop and remove the container
docker stop container_id
docker rm container_id

```

## Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit [Microsoft Contributor License Agreement](https://cla.opensource.microsoft.com).

When you submit a pull request, a CLA bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

