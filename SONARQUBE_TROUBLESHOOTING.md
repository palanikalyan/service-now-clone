# 🔧 SONARQUBE TROUBLESHOOTING GUIDE

## ERROR: "sonarqube.cloud: Name or service not known"

This error means:
1. The URL in `SONARQUBE_HOST` is wrong or empty
2. The secret is not being read by GitHub Actions
3. DNS cannot resolve the hostname

---

## ✅ SOLUTION STEPS

### Step 1: Verify Secrets Are Set

1. Go to: https://github.com/palanikalyan/service-now-clone
2. Settings → Secrets and variables → Actions
3. Check if these exist:
   - ✅ `SONARQUBE_HOST` (should have a value)
   - ✅ `SONAR_TOKEN` (should have a value)

If either is missing or empty, **you must add them**.

### Step 2: Check Secret Values

**The secret should be:**
- `SONARQUBE_HOST`: Full URL (e.g., `https://sonarcloud.io` or `https://sonarqube.example.com`)
- `SONAR_TOKEN`: Token string (starts with `squ_` or similar)

### Step 3: Common Mistakes

❌ **Wrong:** `sonarqube.cloud` (without https://)
✅ **Correct:** `https://sonarcloud.io`

❌ **Wrong:** `sonarcloud.io` (without https://)
✅ **Correct:** `https://sonarcloud.io`

❌ **Wrong:** Empty secret value
✅ **Correct:** Filled with actual URL

### Step 4: How to Add/Update Secrets

1. Go to: https://github.com/palanikalyan/service-now-clone/settings/secrets/actions
2. Click: "New repository secret"
3. For SONARQUBE_HOST:
   - Name: `SONARQUBE_HOST`
   - Value: `https://sonarcloud.io` (copy exactly)
   - Click "Add secret"
4. For SONAR_TOKEN:
   - Name: `SONAR_TOKEN`
   - Value: Your token (from SonarCloud/SonarQube)
   - Click "Add secret"

### Step 5: Verify in Workflow

After adding secrets, the workflow will print debug info showing if they're set.

Go to: GitHub → Actions → SonarQube Static Analysis → Latest run
Look for "Debug - Check Secrets" step to see values.

---

## 🚀 QUICK FIX

If you're sure secrets are correct, try this:

1. Delete old secrets (if any)
2. Add fresh ones:
   ```
   SONARQUBE_HOST = https://sonarcloud.io
   SONAR_TOKEN = [your actual token from SonarCloud]
   ```
3. Re-run workflow: GitHub Actions → SonarQube → Re-run jobs

---

## IF STILL FAILING

### Option A: Skip SonarQube (Temporary)
Use the simple build workflow instead:
```
.github/workflows/backend-docker.yml
```

This doesn't require SonarQube and just builds/pushes Docker image.

### Option B: Use Alternative Workflow
Create a workflow without SonarQube requirement:
```yaml
name: Build Backend
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      - run: |
          cd service-now
          chmod +x mvnw
          ./mvnw clean package -DskipTests
```

### Option C: Disable SonarQube Step
In the workflow, you can add `if: false` to skip it:
```yaml
- name: Build with Maven (SonarQube)
  if: false  # Disabled
  run: |
    ...
```

---

## VERIFICATION CHECKLIST

```
☐ 1. Go to GitHub Secrets page
☐ 2. Verify SONARQUBE_HOST exists and has value
☐ 3. Verify SONAR_TOKEN exists and has value  
☐ 4. Check values start with:
     - SONARQUBE_HOST: https://
     - SONAR_TOKEN: squ_ (or similar)
☐ 5. Re-run workflow
☐ 6. Check "Debug - Check Secrets" output
☐ 7. Confirm values are printed (not empty)
```

---

## REFERENCE

| Secret | Expected Value | Example |
|--------|---|---------|
| SONARQUBE_HOST | Full HTTPS URL | `https://sonarcloud.io` |
| SONAR_TOKEN | Token from SonarCloud | `squ_abc123xyz...` |
| GITHUB_TOKEN | Auto-provided | (GitHub provides this) |

---

**Check your secrets and try again!** ✅
