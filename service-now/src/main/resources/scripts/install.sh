#!/bin/bash

# Software installation script
# Parameters: $1 = software name, $2 = hostname

SOFTWARE_NAME=$1
HOSTNAME=$2

echo "==================================="
echo "Starting installation process..."
echo "Software: $SOFTWARE_NAME"
echo "Target Host: $HOSTNAME"
echo "Timestamp: $(date)"
echo "==================================="

# Simulate installation process
case $SOFTWARE_NAME in
    "vscode"|"Visual Studio Code")
        echo "Installing Visual Studio Code..."
        sleep 2
        echo "Downloading VS Code package..."
        sleep 1
        echo "Installing VS Code on $HOSTNAME..."
        sleep 2
        echo "VS Code installation completed successfully!"
        ;;
    "docker"|"Docker Desktop")
        echo "Installing Docker Desktop..."
        sleep 3
        echo "Setting up Docker environment on $HOSTNAME..."
        sleep 2
        echo "Docker Desktop installation completed successfully!"
        ;;
    "idea"|"IntelliJ IDEA")
        echo "Installing IntelliJ IDEA..."
        sleep 2
        echo "Setting up IntelliJ IDEA on $HOSTNAME..."
        sleep 1
        echo "IntelliJ IDEA installation completed successfully!"
        ;;
    *)
        echo "Installing $SOFTWARE_NAME..."
        sleep 2
        echo "Generic installation process for $SOFTWARE_NAME on $HOSTNAME..."
        sleep 1
        echo "$SOFTWARE_NAME installation completed successfully!"
        ;;
esac

echo "==================================="
echo "Installation process finished!"
echo "Software: $SOFTWARE_NAME"
echo "Host: $HOSTNAME"
echo "Status: SUCCESS"
echo "==================================="

exit 0