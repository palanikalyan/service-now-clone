# AWS EC2 Deployment Guide

## Prerequisites

1. **EC2 Instance Running**
   - Ubuntu 20.04 LTS or 22.04 LTS
   - Security group allows inbound: SSH (22), HTTP (80), HTTPS (443), Custom TCP (8080)
   - Instance has at least 2GB RAM, 20GB disk

2. **SSH Key Ready**
   - PEM file downloaded from AWS
   - Permissions set: `chmod 600 your_key.pem`

3. **Ansible in WSL**
   ```bash
   wsl
   source /tmp/ansible-env/bin/activate
   cd /mnt/c/Users/Asus/Downloads/rm/ansible
   ```

---

## Step 1: Get Your EC2 Details

In AWS Console:
1. Go to EC2 > Instances
2. Find your running instance
3. Copy the **Public DNS** or **Public IP**
   - Example: `ec2-34-123-456-789.compute-1.amazonaws.com`
   - Or: `34.123.456.789`

---

## Step 2: Update Inventory for AWS

Edit `ansible/inventory.ini`:

```ini
[production]
prod_ec2 ansible_host=YOUR_EC2_IP_OR_DNS ansible_user=ubuntu ansible_ssh_private_key_file=~/.ssh/your_key.pem
```

Replace:
- `YOUR_EC2_IP_OR_DNS` with your actual EC2 IP or DNS
- `~/.ssh/your_key.pem` with path to your PEM file

Example:
```ini
[production]
prod_ec2 ansible_host=ec2-34-123-456-789.compute-1.amazonaws.com ansible_user=ubuntu ansible_ssh_private_key_file=~/.ssh/aws-key.pem
```

Also update `[all:vars]` to use SSH (not local):

```ini
[all:vars]
ansible_connection=ssh
ansible_user=ubuntu
ansible_ssh_private_key_file=~/.ssh/your_key.pem
ansible_ssh_common_args='-o StrictHostKeyChecking=no'
```

---

## Step 3: Copy SSH Key to WSL

In PowerShell:
```powershell
# Copy your PEM file to a location accessible from WSL
Copy-Item C:\Users\Asus\Downloads\your_key.pem -Destination C:\Users\Asus\.ssh\
```

Or manually copy the PEM file to `C:\Users\Asus\.ssh\`

---

## Step 4: Test SSH Connection (in WSL)

```bash
ssh -i ~/.ssh/your_key.pem ubuntu@YOUR_EC2_IP_OR_DNS

# You should see: ubuntu@ip-xxx:~$
# If successful, type: exit
```

---

## Step 5: Deploy with Ansible (in WSL)

```bash
cd /mnt/c/Users/Asus/Downloads/rm/ansible
source /tmp/ansible-env/bin/activate

# Deploy to EC2
ansible-playbook -i inventory.ini deploy.yml -l production

# Or with verbose output:
ansible-playbook -i inventory.ini deploy.yml -l production -vvv
```

---

## Step 6: Verify Deployment on EC2

SSH into your EC2 instance:
```bash
ssh -i ~/.ssh/your_key.pem ubuntu@YOUR_EC2_IP_OR_DNS

# Inside EC2:
docker ps

# Test services:
curl http://localhost/          # Frontend
curl http://localhost:8080/api  # Backend

# View logs:
docker logs service-now-app
docker logs request-management-ui
```

---

## Step 7: Access Services from Your Computer

Once deployed:
- **Frontend**: `http://YOUR_EC2_IP/`
- **Backend**: `http://YOUR_EC2_IP:8080/api`
- **H2 Console** (if enabled): `http://YOUR_EC2_IP:8080/h2-console`

---

## Troubleshooting

### Permission Denied (publickey)
- Ensure PEM file has correct permissions: `chmod 600 ~/.ssh/your_key.pem`
- Verify security group allows SSH (port 22)

### Docker Not Found
- Ansible will install Docker automatically
- If it fails, SSH into EC2 and run manually: `sudo apt update && sudo apt install docker.io -y`

### Port Already in Use
- SSH into EC2 and check: `sudo lsof -i :8080`
- Kill process if needed: `sudo kill -9 <PID>`

### Deployment Hangs
- Check EC2 instance has enough resources (RAM, disk)
- View logs during deployment: `docker logs -f service-now-app`

---

## Next: 3-Tier Architecture (Separate Instances)

For production with **3 separate instances**:

1. **Database EC2**: RDS or standalone H2 instance
2. **Backend EC2**: service-now API
3. **Frontend EC2**: Nginx with Angular static files

This requires:
- Load balancer (ALB)
- VPC with subnets
- Security groups per tier
- Separate Ansible playbooks per tier

For now, all-in-one EC2 is fine for testing! 🚀

---

## Quick Commands

```bash
# Deploy to EC2
ansible-playbook -i inventory.ini deploy.yml -l production

# Check if reachable
ansible production -i inventory.ini -m ping

# Restart services on EC2
ansible production -i inventory.ini -m shell -a "docker restart service-now-app"

# View backend logs
ansible production -i inventory.ini -m shell -a "docker logs -n 20 service-now-app"

# Stop all containers
ansible production -i inventory.ini -m shell -a "docker stop $(docker ps -q)"
```
