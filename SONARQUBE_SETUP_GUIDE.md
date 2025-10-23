# 🔍 SonarQube Static Analysis Setup Guide

## WHAT'S INCLUDED

✅ GitHub Actions Pipeline (`.github/workflows/sonarqube-analysis.yml`)
✅ GitLab CI Pipeline (`.gitlab-ci-sonarqube.yml`)
✅ Backend (Java/Maven) Analysis
✅ Frontend (Angular/TypeScript) Analysis
✅ Quality Gate Checks
✅ PR/MR Comments with Results

---

## SETUP STEPS

### STEP 1: Set Up SonarQube Server

**Option A: Use SonarQube Cloud (Easiest)**
1. Go to: https://sonarqube.cloud
2. Sign up with GitHub account
3. Create organization
4. Create project
5. Generate token

**Option B: Self-Hosted SonarQube**
1. Install Docker
2. Run:
   ```bash
   docker run -d --name sonarqube \
     -e SONARQUBE_JDBC_URL=jdbc:postgresql://db:5432/sonarqube \
     -e SONARQUBE_JDBC_USERNAME=sonar \
     -e SONARQUBE_JDBC_PASSWORD=sonar \
     -p 9000:9000 \
     sonarqube:latest
   ```
3. Access: http://localhost:9000
4. Login: admin/admin
5. Create project and generate token

---

### STEP 2: Get SonarQube Credentials

1. SonarQube Dashboard → My Account → Security
2. Generate token (e.g., `squ_abc123xyz...`)
3. Copy:
   - **SONARQUBE_HOST**: e.g., `https://sonarqube.cloud`
   - **SONARQUBE_TOKEN**: e.g., `squ_abc123xyz...`

---

### STEP 3: Add GitHub Secrets

1. GitHub Repository → Settings → Secrets and variables → Actions
2. Click "New repository secret"
3. Add:
   - Name: `SONARQUBE_HOST`
     Value: `https://sonarqube.cloud` (or your SonarQube URL)
   - Name: `SONARQUBE_TOKEN`
     Value: `squ_abc123xyz...` (your token)

---

### STEP 4: Add GitLab Variables

1. GitLab Project → Settings → CI/CD → Variables
2. Click "Add variable"
3. Add:
   - Key: `SONARQUBE_HOST`
     Value: `https://sonarqube.cloud` (or your SonarQube URL)
     Protected: ✓
   - Key: `SONARQUBE_TOKEN`
     Value: `squ_abc123xyz...` (your token)
     Protected: ✓

---

### STEP 5: Place Workflow Files

**For GitHub Actions:**
```
repository-root/
├── .github/
│   └── workflows/
│       └── sonarqube-analysis.yml  ← Already created
```

**For GitLab CI:**
Rename and place in repository root:
```
repository-root/
├── .gitlab-ci-sonarqube.yml  → Rename to .gitlab-ci.yml (if not exists)
│   OR merge with existing .gitlab-ci.yml
```

---

## HOW TO USE

### GitHub Actions

**Automatically triggered on:**
- Push to main, develop, feature/* branches
- Pull requests to main, develop branches

**Manual trigger:**
1. Go to: Actions tab
2. Select: "SonarQube Static Analysis"
3. Click: "Run workflow"

**View results:**
1. Go to: Actions tab
2. Click on workflow run
3. Check logs and artifacts
4. PR will have SonarQube comment

---

### GitLab CI

**Automatically triggered on:**
- Commits to main, develop branches
- Merge requests to main, develop branches

**Manual trigger:**
1. Go to: CI/CD → Pipelines
2. Click: "Run pipeline"
3. Select branch

**View results:**
1. Go to: CI/CD → Pipelines
2. Click on pipeline
3. Check job logs and artifacts
4. MR will have SonarQube comment

---

## WHAT GETS ANALYZED

### Backend (Java)
- Lines of Code (ncloc)
- Code Complexity
- Code Duplications
- Security Issues
- Bug Detection
- Maintainability
- Test Coverage

### Frontend (TypeScript/Angular)
- Lines of Code
- Type Safety Issues
- Security Vulnerabilities
- Performance Issues
- Code Smells
- Test Coverage

---

## QUALITY GATE

Default SonarQube Quality Gate checks:
- ✅ Coverage ≥ 80%
- ✅ Duplicated Lines < 3%
- ✅ No new security hotspots
- ✅ No bugs
- ✅ No critical issues

**If Quality Gate FAILS:**
- Pipeline will fail (allow_failure: true for GitLab)
- PR/MR will show status
- Merge will be blocked (configurable)

---

## SONARQUBE DASHBOARD

After pipeline runs:
1. Go to SonarQube Dashboard
2. Select your project
3. View metrics:
   - **Code Quality**: Issues, smells, duplications
   - **Security**: Vulnerabilities, hotspots
   - **Coverage**: Line/Branch coverage
   - **Maintainability**: Technical debt, ratings

---

## PIPELINE METRICS

Both pipelines generate:

### Artifacts
- SonarQube reports (.json)
- Code quality reports
- Metrics export

### PR/MR Comments
- Link to SonarQube dashboard
- Analysis status
- Metrics summary

### Logs
- Build output
- Analysis output
- Quality gate status

---

## COMMON ISSUES & FIXES

### Issue: "SONARQUBE_TOKEN not found"
**Fix:**
1. Check GitHub Secrets / GitLab Variables are set
2. Verify names match exactly
3. Test token is valid (not expired)

### Issue: "Project not found in SonarQube"
**Fix:**
1. Create project manually in SonarQube
2. Use correct `projectKey`
3. Verify SONARQUBE_HOST URL

### Issue: "Quality Gate Failed"
**Fix:**
1. Check SonarQube dashboard for issues
2. Fix high-priority bugs/vulnerabilities
3. Improve test coverage
4. Reduce code duplication

### Issue: "Maven build fails"
**Fix:**
1. Check pom.xml syntax
2. Verify Java version compatibility
3. Check Maven cache

### Issue: "No coverage data"
**Fix:**
1. Enable code coverage in build
2. Generate coverage reports (jacoco for Java)
3. Configure coverage paths in SonarQube

---

## INTEGRATION WITH EXISTING PIPELINES

### Merge with GitHub Actions

Add to existing workflow:
```yaml
- name: SonarQube Analysis
  run: |
    cd service-now
    mvn sonar:sonar ...
```

### Merge with GitLab CI

Add to `.gitlab-ci.yml`:
```yaml
include:
  - local: '.gitlab-ci-sonarqube.yml'
```

---

## NEXT STEPS

1. ✅ Set up SonarQube account/server
2. ✅ Add secrets to GitHub & GitLab
3. ✅ Push code with workflow files
4. ✅ Pipeline will run automatically
5. ✅ View results in SonarQube dashboard
6. ✅ Check PR/MR comments

---

## FILE LOCATIONS

- GitHub Actions: `.github/workflows/sonarqube-analysis.yml`
- GitLab CI: `.gitlab-ci-sonarqube.yml`

Both files are ready to use! Just:
1. Add secrets
2. Push to repository
3. Pipeline runs automatically

---

**Questions? Check SonarQube Dashboard or pipeline logs!** 🚀
