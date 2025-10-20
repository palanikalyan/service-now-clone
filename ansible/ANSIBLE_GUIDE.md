# Ansible & CI/CD Integration Guide (H2 Database)

## Quick Start (Local Testing)

### Prerequisites
- Ansible 2.9+
- Docker & Docker Compose
- Python 3.8+

### Install Ansible
```bash
pip install ansible
# Also install docker module for Ansible:
pip install docker
```

### Test Locally on Localhost
```bash
cd ansible
ansible-playbook -i inventory.ini deploy.yml
# Or with specific tags:
ansible-playbook -i inventory.ini deploy.yml --tags h2
ansible-playbook -i inventory.ini deploy.yml --tags backend
ansible-playbook -i inventory.ini deploy.yml --tags frontend
```

### Test with variable overrides
```bash
ansible-playbook -i inventory.ini deploy.yml \
  -e "deploy_env=development"
```

---

## AWS EC2 Deployment

### 1. Create/Configure an EC2 Instance
- Launch an Ubuntu 20.04 LTS or 22.04 LTS instance
- Ensure security group allows inbound traffic on ports: 22 (SSH), 80 (HTTP), 8080 (Backend)
- Note the instance public IP or DNS

### 2. Update Inventory
Edit `ansible/inventory.ini`:
```ini
[production]
your_ec2_instance.compute.amazonaws.com ansible_user=ubuntu ansible_ssh_private_key_file=~/.ssh/your_key.pem
```

### 3. Run Playbook on EC2
```bash
cd ansible
ansible-playbook -i inventory.ini deploy.yml -l production
```

### 4. Verify Services
```bash
# SSH into the instance:
ssh -i ~/.ssh/your_key.pem ubuntu@your_ec2_instance.compute.amazonaws.com

# Check running containers:
docker ps

# Test services:
curl http://localhost/          # Frontend
curl http://localhost:8080/api  # Backend
# H2 database is embedded, no separate connection needed
```

---

## Roles & Variables

### H2 Database Role (Database Layer)
**Variables** (`roles/mysql/vars/main.yml`):
- `h2_enabled`: false (embedded mode) or true (external H2 server)
- `db_type`: "h2"
- `h2_file_path`: File path for H2 data storage

**Handlers**:
- `restart h2`: Restarts the H2 container (only if external)

**Notes**: H2 is embedded in Spring Boot by default. No separate container needed unless you set `h2_enabled: true`.

### Backend Role (service-now)
**Variables** (`roles/backend/vars/main.yml`):
- `backend_container_name`: service-now-app
- `backend_port`: 8080
- `backend_image`: service-now-backend:latest
- `backend_java_opts`: JVM options (-Xms512m -Xmx1024m)
- `backend_db_type`: "h2"
- `backend_db_url`: "jdbc:h2:file:/tmp/request_management_db;MODE=MYSQL"
- `backend_jpa_hibernate_dialect`: "org.hibernate.dialect.H2Dialect"

**Handlers**:
- `restart backend`: Restarts the backend container
- `rebuild backend`: Rebuilds and restarts the backend

**Key Changes for H2**:
- Spring properties passed as environment variables:
  - `SPRING_DATASOURCE_URL`: H2 JDBC URL
  - `SPRING_DATASOURCE_DRIVER_CLASS_NAME`: org.h2.Driver
  - `SPRING_JPA_HIBERNATE_DIALECT`: org.hibernate.dialect.H2Dialect
  - `SPRING_JPA_HIBERNATE_DDL_AUTO`: update (auto-creates tables)

### Frontend Role (request-management-ui)
**Variables** (`roles/frontend/vars/main.yml`):
- `frontend_container_name`: request-management-ui
- `frontend_port`: 80
- `frontend_image`: request-management-ui:latest

**Handlers**:
- `restart frontend`: Restarts the frontend container
- `rebuild frontend`: Rebuilds and restarts the frontend

---

## CI/CD Integration with GitHub Actions

Add to `.github/workflows/deploy-with-ansible.yml` (create new file if needed):

```yaml
name: Deploy with Ansible (H2)

on:
  push:
    branches:
      - main

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Set up Ansible
        run: |
          pip install ansible docker

      - name: Deploy to EC2
        env:
          EC2_HOST: ${{ secrets.EC2_HOST }}
          EC2_USER: ubuntu
          EC2_KEY: ${{ secrets.EC2_SSH_KEY }}
        run: |
          mkdir -p ~/.ssh
          echo "$EC2_KEY" > ~/.ssh/id_rsa
          chmod 600 ~/.ssh/id_rsa
          ssh-keyscan -H $EC2_HOST >> ~/.ssh/known_hosts
          
          cd ansible
          ansible-playbook -i inventory.ini deploy.yml \
            -e "ansible_host=$EC2_HOST" \
            -e "deploy_env=production"
```

### Required GitHub Secrets
- `EC2_HOST`: Public IP or DNS of EC2 instance
- `EC2_SSH_KEY`: Private SSH key (PEM format)

---

## Application Configuration for H2

Your `application.properties` or `application.yml` should include (Ansible will override via env vars):

```properties
# H2 Configuration
spring.datasource.url=jdbc:h2:file:/tmp/request_management_db;MODE=MYSQL
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Or in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:h2:file:/tmp/request_management_db;MODE=MYSQL
    driverClassName: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
  h2:
    console:
      enabled: true
      path: /h2-console
```

---

## Testing & Rollback

### Dry Run (Check Mode)
```bash
ansible-playbook -i inventory.ini deploy.yml --check
```

### Limit to specific hosts/tags
```bash
# Only run on development:
ansible-playbook -i inventory.ini deploy.yml -l development

# Only deploy backend:
ansible-playbook -i inventory.ini deploy.yml --tags backend
```

### Rollback (restart services)
```bash
ansible all -i inventory.ini -m docker_container -a "name=service-now-app state=stopped"
ansible all -i inventory.ini -m docker_container -a "name=service-now-app state=started"
```

### Access H2 Console (if enabled)
```bash
# If H2 console is enabled on backend:
curl http://localhost:8080/h2-console
# Or open in browser: http://your_ec2_ip:8080/h2-console
```

---

## Troubleshooting

### Connection issues
```bash
# Test SSH connectivity:
ansible all -i inventory.ini -m ping
```

### View logs
```bash
# Check Ansible logs:
tail -f ansible.log

# Check backend logs (includes H2):
ansible all -i inventory.ini -m shell -a "docker logs service-now-app"

# Check H2 file persistence:
ansible all -i inventory.ini -m shell -a "ls -la /tmp/request_management_db*"
```

### Rerun with verbose output
```bash
ansible-playbook -i inventory.ini deploy.yml -vvv
```

---

## Database Persistence

- H2 files stored in: `/tmp/request_management_db*` (on the Docker host)
- For production, consider using a named volume or external storage
- To use in-memory H2 (no persistence): change `backend_db_url` to `jdbc:h2:mem:request_management_db;MODE=MYSQL`

---

## Notes

- All services use Docker containers bridged on `app-network`.
- H2 is embedded in Spring Boot for simplicity; no separate DB container.
- Database auto-creates tables on first run (`spring.jpa.hibernate.ddl-auto=update`).
- For production, consider separate H2 instance or upgrade to PostgreSQL/MySQL.

