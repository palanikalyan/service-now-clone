# WSL2 Deployment Guide for Windows

## Prerequisites
- Windows 10/11 with WSL2 enabled
- Docker Desktop for Windows (with Docker integration for WSL2)
- ~5GB disk space

## Step 1: Set up WSL2 & Install Ansible

Open PowerShell (Admin) and run:
```powershell
# Enable WSL2 and install Ubuntu
wsl --install -d Ubuntu

# Restart your computer if prompted
```

Then launch WSL:
```powershell
wsl
```

Inside WSL terminal (Ubuntu):
```bash
# Update package manager
sudo apt update
sudo apt upgrade -y

# Install Ansible and dependencies
sudo apt install -y \
  ansible \
  python3-pip \
  docker.io

# Install Python Docker module
pip install docker

# Add your user to docker group (to avoid sudo)
sudo usermod -aG docker $USER

# Activate group membership (no logout needed)
newgrp docker

# Verify installations
ansible --version
docker --version
```

---

## Step 2: Navigate to Your Repository

Inside WSL terminal:
```bash
cd /mnt/c/Users/Asus/Downloads/rm/ansible

# Verify files are there
ls -la
```

---

## Step 3: Verify Inventory for Localhost

The `inventory.ini` file should use `localhost` with `ansible_connection=local`:

```bash
cat inventory.ini
# Should show:
# [local]
# localhost ansible_connection=local ansible_become_method=sudo
```

---

## Step 4: Run the Playbook

```bash
# Test connectivity first
ansible all -i inventory.ini -m ping

# Run the full deployment (may ask for sudo password)
ansible-playbook -i inventory.ini deploy.yml

# Or run with specific tags:
ansible-playbook -i inventory.ini deploy.yml --tags backend
ansible-playbook -i inventory.ini deploy.yml --tags frontend
```

---

## Step 5: Verify Deployment

```bash
# Check running containers
docker ps

# View logs
docker logs service-now-app
docker logs request-management-ui

# Test services
curl http://localhost/          # Frontend
curl http://localhost:8080/api  # Backend

# Check H2 database
curl http://localhost:8080/h2-console  # H2 console (if enabled)
```

---

## Troubleshooting

### Permission Denied / Docker Not Found
```bash
# Ensure Docker daemon is running
sudo service docker start

# If WSL integration not working:
# - Restart Docker Desktop from Windows
# - Close and reopen WSL: wsl --terminate Ubuntu && wsl
```

### Ansible: "No module named 'docker'"
```bash
# Install Docker module for Ansible
pip install docker

# Or via Ansible Galaxy:
ansible-galaxy collection install community.docker
```

### Cannot Connect to Docker Daemon
```bash
# Start Docker service in WSL
sudo service docker start

# Or enable autostart (add to ~/.bashrc):
echo 'sudo service docker start' >> ~/.bashrc
```

### Port Already in Use (80, 8080)
```bash
# Find and kill process using port 8080
sudo lsof -i :8080
sudo kill -9 <PID>

# Or change ports in inventory.ini:
# backend_port_mapping: "8081:8080"  # Use 8081 instead
```

### Sudo Password Prompt During Playbook
```bash
# Run with --ask-become-pass:
ansible-playbook -i inventory.ini deploy.yml --ask-become-pass
```

---

## Quick Commands Reference

```bash
# Deploy everything
ansible-playbook -i inventory.ini deploy.yml

# Deploy only backend
ansible-playbook -i inventory.ini deploy.yml --tags backend

# Deploy only frontend
ansible-playbook -i inventory.ini deploy.yml --tags frontend

# Dry run (check mode)
ansible-playbook -i inventory.ini deploy.yml --check

# Verbose output
ansible-playbook -i inventory.ini deploy.yml -vvv

# Restart services
docker restart service-now-app
docker restart request-management-ui

# View running containers
docker ps

# View all containers (including stopped)
docker ps -a

# Stop all containers
docker stop $(docker ps -q)

# Remove all stopped containers
docker container prune -f
```

---

## Accessing Services from Windows

Once running in WSL, services are accessible from Windows:

- **Frontend**: http://localhost/
- **Backend API**: http://localhost:8080/api
- **H2 Console** (if enabled): http://localhost:8080/h2-console

---

## Next: Deploy to AWS EC2

After testing locally in WSL:

1. Update `ansible/inventory.ini`:
   ```ini
   [production]
   your_ec2_instance.compute.amazonaws.com ansible_user=ubuntu
   ```

2. Copy your EC2 SSH key:
   ```bash
   cp ~/.ssh/your_ec2_key.pem /mnt/c/Users/Asus/.ssh/
   ```

3. Run playbook on EC2:
   ```bash
   ansible-playbook -i inventory.ini deploy.yml -l production
   ```

---

## Tips

- Use `--check` flag to do a dry run before actual deployment
- Set `deploy_env=production` for prod deployments: `-e "deploy_env=production"`
- Monitor logs in real-time: `docker logs -f service-now-app`
- H2 data persists in `/tmp/request_management_db*` on the WSL filesystem

Enjoy deploying! 🚀
