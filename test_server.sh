#!/bin/bash

# Function to install nvm and Node.js 📦🌟
install_nvm() {
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash
    # shellcheck disable=SC1090
    source ~/.nvm/nvm.sh
    nvm install 16
    nvm use 16
}

# Function to install global dependencies 📦🌍
install_dependencies() {
    npm install -g mountebank
    npm install -g npm@8.4.1
    npm audit fix
}

# Main function to execute setup steps 🚀
setup_project() {
    install_nvm       # Call the install_nvm function to install nvm and Node.js
    install_dependencies  # Call the install_dependencies function to install global dependencies
}

# Execute the setup 🎉
setup_project    # Call the setup_project function to execute the setup steps

# Execute mb --configfile imposters.ejs ⚡️
mb --configfile imposters.ejs   # Run the "mb --configfile imposters.ejs" command to execute Mountebank with the specified configuration file

# This script is written in Bash and aims to simplify the setup process for a project. Here's a breakdown of each section:

## 1. `install_nvm` function:
#   - This function is responsible for installing nvm (Node Version Manager) and Node.js. 📦🌟
#   - It uses the `curl` command to download the nvm installation script from the official GitHub repository.
#   - The script is then executed using `bash` to install nvm.
#   - After installation, the `~/.nvm/nvm.sh` file is sourced to make nvm available in the current shell session.
#   - It installs Node.js version 16 using `nvm install 16` and sets it as the active version with `nvm use 16`.
#
## 2. `install_dependencies` function:
#   - This function installs the global dependencies required for the project using `npm`. 📦🌍
#   - The `npm install -g` command is used to install the following packages globally:
#     - `mountebank`: A tool for creating mock servers and service virtualization.
#     - `npm@8.4.1`: The specified version of the npm package manager.
#   - After the installation, `npm audit fix` is run to fix any reported vulnerabilities in the installed packages.
#
## 3. `setup_project` function:
#   - This function serves as the main entry point for the setup steps. 🚀
#   - It calls the `install_nvm` function to install nvm and Node.js.
#   - Then it calls the `install_dependencies` function to install the global dependencies.
#
## 4. `setup_project` execution:
#   - The `setup_project` function is executed, triggering the setup steps to be performed. 🎉
#
## 5. `mb --configfile imposters.ejs` execution:
#   - After the setup is completed, the script runs the command `mb --configfile imposters.ejs`. ⚡️
#   - This command executes Mountebank with the specified configuration file (`imposters.ejs`).
#
## This script provides a convenient way to set up the project environment by installing nvm, Node.js, global dependencies, and executing Mountebank with the desired configuration. Enjoy the setup process! 🎈🎊