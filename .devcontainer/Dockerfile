ARG VARIANT="11"
FROM mcr.microsoft.com/vscode/devcontainers/java:0-${VARIANT}

ENV DEBIAN_FRONTEND=noninteractive

ARG USERNAME="vscode"

RUN export DEBIAN_FRONTEND=noninteractive && \
    curl https://packages.microsoft.com/config/debian/10/packages-microsoft-prod.deb --output /tmp/packages-microsoft-prod.deb && \
    dpkg -i /tmp/packages-microsoft-prod.deb && \
    apt-get update && \
    apt-get -y install --no-install-recommends apt-utils dialog && \
    apt-get -y install --no-install-recommends apt-transport-https ca-certificates software-properties-common libssl-dev libffi-dev \
        dotnet-sdk-3.1 build-essential gnupg-agent dnsutils httpie bash-completion curl wget git unzip jq lsb-release procps gnupg2 && \
    apt-get -y upgrade

# [Option] Install Maven
ARG INSTALL_MAVEN="true"
ARG MAVEN_VERSION="3.6.3"
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then su vscode -c "source /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; fi

# [Option] Install Node.js
ARG  INSTALL_NODE="false"
ARG  NODE_VERSION="none"
ENV  NVM_DIR=/usr/local/share/nvm
ENV  NVM_SYMLINK_CURRENT=true \
     PATH=${NVM_DIR}/current/bin:${PATH}
COPY .devcontainer/library-scripts/node-debian.sh /tmp/library-scripts/
RUN  if [ "$INSTALL_NODE" = "true" ]; then bash /tmp/library-scripts/node-debian.sh "${NVM_DIR}" "${NODE_VERSION}" "${USERNAME}"; fi

# [Option] Install Azure CLI
ARG  INSTALL_AZURE_CLI="true"
COPY .devcontainer/library-scripts/azcli-debian.sh /tmp/library-scripts/
RUN  if [ "$INSTALL_AZURE_CLI" = "true" ]; then bash /tmp/library-scripts/azcli-debian.sh; fi

# [Option] Install Docker CLI
# To use the Docker CLI inside a container, use the bind mount option to add the docker socket to the container.
# Example: "docker run -v '/var/run/docker.sock:/var/run/docker.sock' ..."
ARG  INSTALL_DOCKER="true"
COPY .devcontainer/library-scripts/docker-debian.sh /tmp/library-scripts/
RUN  if [ "${INSTALL_DOCKER}" = "true" ]; then bash /tmp/library-scripts/docker-debian.sh "${USERNAME}"; fi

ENV PATH="~/.dotnet/tools:${PATH}"
USER $USERNAME
