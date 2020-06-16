#!/bin/sh

date >> ~/status
echo "post-install.sh ..." >> ~/status

# source the bashrc-append from the repo
# you can add project specific settings to .bashrc-append and 
# they will be added for every user that clones the repo with Codespaces
# including keys or secrets could be a SECURITY RISK

# copy vscode files
mkdir -p .vscode && cp docs/vscode-template/* .vscode

echo "" >> ~/.bashrc
echo ". ${PWD}/.devcontainer/.bashrc-append" >> ~/.bashrc

date >> ~/status
echo "Updating Packages ..." >> ~/status

DEBIAN_FRONTEND=noninteractive
sudo apt-get update

date >> ~/status
echo "Installing basics ..." >> ~/status

sudo apt-get install -y --no-install-recommends apt-utils dialog dnsutils

date >> ~/status
echo "Installing mvn ..." >> ~/status

sudo apt-get install -y maven

DEBIAN_FRONTEND=dialog

# add tools to path
export PATH="$PATH:~/.dotnet/tools"

# set dotnet root
export DOTNET_ROOT=~/.dotnet

# install WebV
dotnet tool install -g webvalidate

date >> ~/status
echo "Done" >> ~/status
