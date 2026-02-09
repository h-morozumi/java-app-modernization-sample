#!/bin/bash
set -e

# Install SDKMAN!
curl -s 'https://get.sdkman.io' | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Enable auto_env and auto_answer to avoid interactive prompts
sed -i 's/sdkman_auto_env=false/sdkman_auto_env=true/' "$HOME/.sdkman/etc/config"
sed -i 's/sdkman_auto_answer=false/sdkman_auto_answer=true/' "$HOME/.sdkman/etc/config"

# Install Java versions
sdk install java 8.0.472-amzn
sdk install java 11.0.26-amzn
sdk install java 21.0.9-amzn

# Install Maven versions
sdk install maven 3.6.3
sdk install maven 3.8.8
sdk install maven 3.9.12
