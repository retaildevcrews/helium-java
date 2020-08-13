FROM maven:3.6.3-jdk-11-slim

ENV DEBIAN_FRONTEND=noninteractive

#Install Common Tools
RUN apt-get update && apt-get -y install --no-install-recommends apt-transport-https apt-utils bash-completion curl dialog dnsutils git httpie lsb-release procps unzip wget

#Install Azure CLI
RUN curl -sL https://aka.ms/InstallAzureCLIDeb | bash

# Link Java Installation to defined /docker-java-home
RUN ln -s "${JAVA_HOME}" /docker-java-home

#Install .NET Core SDK
RUN wget https://packages.microsoft.com/config/debian/10/packages-microsoft-prod.deb -O /tmp/packages-microsoft-prod.deb && \
 dpkg -i /tmp/packages-microsoft-prod.deb && apt-get update && apt-get -y install --no-install-recommends dotnet-sdk-3.1

#Install WebValidate
RUN export DOTNET_ROOT=/root/.dotnet && dotnet tool install -g webvalidate
ENV PATH="/root/.dotnet/tools:${PATH}"

ENV DEBIAN_FRONTEND=

