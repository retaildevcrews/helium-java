### Build a Web API reference application via a Java Web API application using Managed Identity, Key Vault, and Cosmos DB that is designed to be deployed to Azure App Service or AKS

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

## CI-CD


This repo uses [GitHub Actions](/.github/workflows/dockerCI.yml) for Continuous Integration.

- CI supports pushing to Azure Container Registry or DockerHub
- The action is setup to execute on a PR or commit to ```master```
  - The action does not run on commits to branches other than ```master```
- The action always publishes an image with the ```:beta``` tag
- If you tag the repo with a version i.e. ```v1.0.8``` the action will also
  - Tag the image with ```:1.0.8```
  - Tag the image with ```:stable```
  - Note that the ```v``` is case sensitive (lower case)

CD is supported via webhooks in Azure App Services connected to the ACR or DockerHub repository.

### Pushing to Azure Container Registry

In order to push to ACR, you must create a Service Principal that has push permissions to the ACR and set the following ```secrets``` in your GitHub repo:

- Azure Login Information
  - TENANT
  - SERVICE_PRINCIPAL
  - SERVICE_PRINCIPAL_SECRET

- ACR Information
  - ACR_REG
  - ACR_REPO
  - ACR_IMAGE

### Pushing to DockerHub

In order to push to DockerHub, you must set the following ```secrets``` in your GitHub repo:

- DOCKER_REPO
- DOCKER_USER
- DOCKER_PAT
  - Personal Access Token

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
export KEYVAULT_NAME=$He_Name

# This command takes about 3-5 minutes:

mvn clean package

mvn spring-boot:run

# When the previous command shows 'Netty started on port(s): 4120'
# test the application (takes about 10 seconds to start) in a new window

curl http://localhost:4120/healthz

# Stop the application by typing Ctrl-C in the Azure CLI terminal window
# Or :::::

mvn spring-boot:stop

```

- Run the application as a local container instead

```bash

# Make sure you are in the root of the repo
# Run the command below to stop the previous instance and free up port 4120:

mvn spring-boot:stop

# You can also hit Ctrl-C from the shell window

# Build the image and run the container using Docker

# make sure you are in the root of the repo

# this will use Azure CLI cached credentials

# build the image

# IMPORTANT: In the following steps, you will be using the Dockerfile-Dev to build a developer
# image, and thus, you MUST make sure that you change the USER ID (UID) and GROUP ID (GID) of the
# 'helium' user set via the'useradd -u <uid>' in the Dockerfile-Dev to the one that is assigned
# to you on your host. The Dockerfile-Dev sets default values of UID=1000 and GID=1000 for the
# helium user. On a single user system, in general you would have the same IDs but kindly
# verify using the 'id' command that this is the case or change appropriately.

docker build . -t helium-dev -f Dockerfile-Dev


# We use a multi-stage docker build as installing the prerequisites and Azure CLI takes a while.
# If you want to build a "permanent cache" of the first stage (so that "docker system prune"
# doesn't delete it), you can run this command first:

docker build . --target helium-dev-base -t helium-dev-base -f Dockerfile-Dev

# Note that as part of building the dev container, we copy the source code into the
# /home/helium/helium-java as we run the code as the helium user and so the developer has
# access to the source within the container to experiment with.

# Customizing your environment with dotFiles
# If you want to use git from within the container, you should copy your ~/.gitconfig to dotFiles folder
# before building the container. You can also copy your ~/.bashrc file to dotFiles to keep your aliases and exports.
# Ensure you don't accidentally copy any credentials or secrets!
# .gitignore will not commit any files in dotFiles that begin with "."
# update .gitignore for any other files to exclude.

# Then run the container with the command specified below. Note that the -v argument below specifies
# that the Host OS $HOME/.azure should be mounted in the /home/helium/.azure in the container. This
# is done so that any stored azure credentials created and cached by (az login) from the  host OS
# will be used to access the keyvault specified using the cmd line argument KEYVAULT_NAME.
# Additionally, mounting the volume (with the -v option) prevents your Azure credentials from
# accidentally getting pushed to a repo.

docker run -p 4120:4120 --name helium-dev \
--env KEYVAULT_NAME=$He_Name  \
-v ~/.azure:/home/helium/.azure -it helium-dev:latest

# Note that the dev dockerfile contains a full environment for you to be able to build and run the
# app. However, it does not contain a prebuilt copy of the application for  you to use immediately.
# You will need to use the bash terminal spawned from the docker command above  to build and run
# the app as follows:

mvn clean package
mvn spring-boot:run

# In a different terminal than the one spawned above, check the logs to ensure the container
# is properly running and wait until the application started message appears.

docker logs helium-dev

# test the application
# the application takes about 10 seconds to start and you may have to run the below command more than once.
curl http://localhost:4120/healthz

# Clean up  - stop and remove the container.
docker stop helium-dev
docker rm helium-dev

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

