# Helium-Java

## Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit [Microsoft Contributor License Agreement](https://cla.opensource.microsoft.com).

When you submit a pull request, a CLA-bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., label, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

# Build a Docker containerized, secure Java Web API application using Managed Identity, Key Vault, and Cosmos DB that is designed to be deployed to Azure App Service or AKS

![License](https://img.shields.io/badge/license-MIT-green.svg)
![Docker Image Build](https://github.com/retaildevcrews/helium-typescript/workflows/Docker%20Image%20Build/badge.svg)

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
- Azure CLI 2.0.72+ ([download](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest))
- Docker CLI ([download](https://docs.docker.com/install/))
- Java 8 above ([download](https://nodejs.org/en/download/)) 
- JQ ([download](https://stedolan.github.io/jq/download/))
- Completion of the Key Vault, ACR, Azure Monitor and Cosmos DB setup in the Helium [readme](https://github.com/retaildevcrews/helium)
- Visual Studio Code (optional) ([download](https://code.visualstudio.com/download))

### Dependency Vulnerability

Any dependencies?  

## Setup

The application requires Key Vault and Cosmos DB to be setup per the Helium [readme](https://github.com/retaildevcrews/helium)

### Run locally on your machine

- Fork this repo and clone to your local machine  https://github.com/retaildevcrews/helium-java.git 
  - All instructions assume starting from the root of the repo

```bash
# run locally with unit tests
mvn test 

# run locally without unit tests (use first or second command below - they both run)
mvn clean package -DskipTests
mvn test -DskipTests

mvn clean spring-boot:start 

# test the application (takes about 10 seconds to start)
curl http://localhost:8080/healthz

# Stop the application by typing Ctrl-C in the Azure CLI terminal window
Or :::::
mvn spring-boot:stop

### Run application as a local container instead

```bash

# Make sure you are in the root of the repo
# Build the container first 
## Run this command to build an image in the container OR run the docker build command

docker build -t helium-dev .

# Then run the container
docker run  -p8080:8080 --env KEYVAULT_NAME=devshop-gelatodev-kv myimage_name:latest

# Check the logs to ensure the container is properly running
# Re-run until the application started message appears
docker logs container_id

# Ensure the endpoint is up and running with:
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

