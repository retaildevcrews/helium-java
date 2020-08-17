FROM maven:3.6.3-jdk-11-slim

ENV DEBIAN_FRONTEND=noninteractive

#Install .NET Core SDK Package Dependencies
RUN curl https://packages.microsoft.com/config/debian/10/packages-microsoft-prod.deb --output /tmp/packages-microsoft-prod.deb && \
    dpkg -i /tmp/packages-microsoft-prod.deb && \

#Install Common Tools
    apt-get update && \
    apt-get -y install --no-install-recommends \
            apt-transport-https \
            apt-utils \
            bash-completion \
            curl \
            dotnet-sdk-3.1 \
            dialog \
            dnsutils \
            git \
            httpie \
            lsb-release \
            procps \
            unzip && \

#Install Azure CLI
    curl -sL https://aka.ms/InstallAzureCLIDeb | bash && \

# Link Java Installation to defined /docker-java-home
    ln -s "${JAVA_HOME}" /docker-java-home && \

#Install WebValidate
    dotnet tool install -g webvalidate

ENV PATH="~/.dotnet/tools:${PATH}"
ENV DEBIAN_FRONTEND=