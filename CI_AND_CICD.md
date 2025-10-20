# CI/CD Notes for request-management-ui

This document records the CI/CD work completed, errors encountered, and next steps.

## What I added

1. Jenkins pipeline
   - `Jenkinsfile` in repository root.
   - Stages: Checkout, Install Dependencies, Build.
   - Initially used `sh` (Linux shell). When running on a Windows Jenkins agent this caused errors because `sh` and `nohup` are not available. Updated recommendation: replace `sh` with `bat` when using Windows agents.

2. GitLab CI pipeline
   - `.gitlab-ci.yml` added to repository root.
   - Uses `node:18` image.
   - Stages: install, build.
   - Caches `request-management-ui/node_modules/` and stores `request-management-ui/dist/` as artifacts.

3. Pushed code to GitLab on a feature branch
   - Added remote: `gitlab` -> `https://gitlab.com/palanikalyan27-group/palanikalyan27-project.git`.
   - Remote `main` contained unrelated commits and was protected (force push disallowed).
   - Created and pushed `feature/upload-local` branch. Use the GitLab UI to open a Merge Request to merge into `main`.

## Commands used

```powershell
# Add GitLab remote
git remote add gitlab https://gitlab.com/palanikalyan27-group/palanikalyan27-project.git

# Rename branch to main (locally)
git branch -M main

# If remote main is empty you can push
# git push -u gitlab main

# If remote main has commits, create a new feature branch and push
git checkout -b feature/upload-local
git push -u gitlab feature/upload-local
```

## Errors encountered and explanations

- "./mvnw: Permission denied" in GitHub Actions: fixed by adding a step to `chmod +x mvnw` in the workflow and ensure the working directory is `service-now`.
- Jenkins `nohup` / `sh` errors: caused by using `sh` on a Windows agent. Use `bat` for Windows.
- Git push rejected: remote `main` had commits and branch protection prevented force pushes. Resolved by pushing to a new branch and creating an MR.

## How to trigger GitLab pipeline

- Push to `main` (if pipeline `only: - main` is set) or push to any branch (remove `only` to run on all branches).
- Pipelines are visible under **CI/CD > Pipelines** in the GitLab UI for the project.

## Secrets and credentials

- For Docker login in GitHub Actions, add `DOCKER_USERNAME` and `DOCKER_PASSWORD` (use an Access Token) in GitHub repository Secrets.
- For GitLab, configure repository variables under **Settings > CI/CD > Variables** if needed.

## Next steps

- Merge MR in GitLab to get code into `main`.
- Optionally update `.gitlab-ci.yml` to Node 20/22 or pin the node version used by your project.
- Verify CI pipeline on GitLab runner and adjust caching or artifact paths if required.
- Mark `Finalize push` as done after MR is merged.

---
Generated: October 15, 2025
