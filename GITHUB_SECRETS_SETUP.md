# 🔐 SETUP GITHUB SECRETS FOR SONARQUBE

## ERROR YOU'RE SEEING

```
Error: Failed to query server version: Call to URL [***/api/v2/analysis/version] 
failed: sonarqube.cloud: Name or service not known
```

**Reason:** `SONARQUBE_TOKEN` secret is not set in GitHub

---

## ✅ SOLUTION: ADD GITHUB SECRETS

### Step 1: Go to GitHub Repository Settings

1. Go to: https://github.com/palanikalyan/service-now-clone
2. Click: **Settings** (top right)
3. Left sidebar: **Secrets and variables** → **Actions**

### Step 2: Create New Secrets

Click **"New repository secret"** and add:

#### Secret 1: SONARQUBE_TOKEN
- **Name:** `SONARQUBE_TOKEN`
- **Value:** Get from SonarQube Cloud
  
  **How to get:**
  1. Go to: https://sonarcloud.io
  2. Sign in with GitHub
  3. Click profile icon → My Account → Security
  4. Generate token (copy the long string)
  5. Paste into GitHub secret

- Click **"Add secret"**

#### Secret 2: DOCKER_USERNAME (optional, for Docker push)
- **Name:** `DOCKER_USERNAME`
- **Value:** Your Docker Hub username
- Click **"Add secret"**

#### Secret 3: DOCKER_PASSWORD (optional, for Docker push)
- **Name:** `DOCKER_PASSWORD`
- **Value:** Your Docker Hub access token (NOT password!)
  
  **How to get:**
  1. Go to: https://hub.docker.com
  2. Account settings → Security → Access tokens
  3. Create token
  4. Copy and paste into GitHub secret

- Click **"Add secret"**

---

## 📋 SONARCLOUD.IO SETUP (If you don't have it)

### 1. Create SonarCloud Account
- Go to: https://sonarcloud.io
- Click: "Sign in with GitHub"
- Authorize GitHub access

### 2. Create Organization
- Click: "Create new organization"
- Name: Use your GitHub username
- Bind to GitHub organization

### 3. Create Project
- After org is created, click: "Analyze new project"
- Select: `palanikalyan/service-now-clone`
- Language: Java
- Create project

### 4. Get Your Organization Key
- SonarCloud Dashboard → Organization settings
- Copy **Organization Key** (e.g., `palanikalyan`)

### 5. Generate Token
- Click profile icon → My Account → Security
- Click: "Generate token"
- Name: `github-actions`
- Copy the token (it starts with `squ_`)

---

## 🔧 UPDATE WORKFLOW IF NEEDED

The workflow now uses:
- **Host:** `https://sonarcloud.io` (hardcoded)
- **Organization:** `palanikalyan` (hardcoded - update if different!)
- **Token:** From `SONARQUBE_TOKEN` secret

If your organization key is different, update the workflow:

```yaml
-Dsonar.organization=YOUR_ORG_KEY \
```

---

## ✅ VERIFY SECRETS ARE SET

1. Go to: https://github.com/palanikalyan/service-now-clone/settings/secrets/actions
2. You should see:
   - ✅ SONARQUBE_TOKEN
   - ✅ DOCKER_USERNAME (optional)
   - ✅ DOCKER_PASSWORD (optional)

---

## 🚀 NOW TRY AGAIN

1. Push code:
   ```powershell
   git add .
   git commit -m "Update SonarQube workflow for SonarCloud"
   git push origin main
   ```

2. Go to GitHub → Actions tab

3. See "SonarQube Static Analysis" running

4. Wait for completion

5. Check SonarQube Cloud dashboard: https://sonarcloud.io/dashboard?id=service-now-clone

---

## TROUBLESHOOTING

### Secret not found error
- Verify secret name matches exactly (case-sensitive)
- Check it's added to repository, not organization

### Still getting "Name or service not known"
- Make sure SonarQube token is valid
- Check organization key matches

### Analysis fails with 401
- Token may have expired
- Generate new token and update secret

### Analysis incomplete
- Check logs in GitHub Actions
- Look for actual error message
- Common: Missing project key, wrong organization

---

## QUICK REFERENCE

| Item | Value |
|------|-------|
| SonarCloud URL | https://sonarcloud.io |
| GitHub Org | palanikalyan |
| Project Key | service-now-clone |
| Token Location | SonarCloud → My Account → Security |
| Secrets Location | GitHub Repo → Settings → Secrets |

---

**After adding secrets, re-run the workflow and it should work!** ✅
