#!/bin/sh

# copy vscode files
mkdir -p .vscode && cp docs/vscode-template/* .vscode

# set auth type
export PATH="$PATH:~/.dotnet/tools"
export AUTH_TYPE=CLI

# install WebV global tool
dotnet tool install -g webvalidate

# update .bashrc
echo "" >> ~/.bashrc
echo 'export PATH="$PATH:~/.dotnet/tools"' >> ~/.bashrc
echo "export AUTH_TYPE=CLI" >> ~/.bashrc

