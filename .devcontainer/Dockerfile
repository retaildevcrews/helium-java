FROM maven:3.6.3-jdk-11

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update
RUN apt-get -y install --no-install-recommends apt-utils dialog dnsutils httpie bash-completion curl wget git unzip
RUN apt-get -y install git procps lsb-release
RUN curl -sL https://aka.ms/InstallAzureCLIDeb | bash

# Link Java Installation to defined /docker-java-home
RUN ln -s "${JAVA_HOME}" /docker-java-home

RUN groupadd -g 4120 helium && \
    useradd -u 4120 -g helium -s /bin/sh helium
USER helium

ENV DEBIAN_FRONTEND=