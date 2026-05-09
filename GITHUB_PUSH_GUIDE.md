# GitHub Push Instructions & Deployment Guide

## Prerequisites

- GitHub account (https://github.com)
- Git installed locally
- SSH key or Personal Access Token configured (optional but recommended)

---

## Step 1: Create GitHub Repository

### Create on GitHub.com

1. Go to https://github.com/new
2. Enter **Repository name**: `equipment-borrowing-system` (or your preferred name)
3. Add **Description**: "Equipment Borrowing Management System - Microservices Architecture"
4. Choose **Public** or **Private**
5. Do NOT initialize with README, .gitignore, or license (we already have them)
6. Click **Create repository**

You'll be provided with a repository URL - copy it!

### Example URLs
```
HTTPS: https://github.com/yourusername/equipment-borrowing-system.git
SSH: git@github.com:yourusername/equipment-borrowing-system.git
```

---

## Step 2: Add Remote and Push

### Add Remote Repository

```bash
cd C:\Users\G0V3LHD\Documents\assignment

# Using HTTPS (simpler for beginners)
git remote add origin https://github.com/yourusername/equipment-borrowing-system.git

# OR using SSH (requires SSH key setup)
git remote add origin git@github.com:yourusername/equipment-borrowing-system.git

# Verify remote was added
git remote -v
```

### Push to GitHub

```bash
# Push master branch to GitHub
git branch -M main  # Rename to main (optional, GitHub default)
git push -u origin main

# Or if keeping master
git push -u origin master
```

### Enter Credentials

**If using HTTPS:**
- GitHub will ask for username and token
- Use Personal Access Token (not password)

**Generate Personal Access Token:**
1. Go to GitHub Settings → Developer settings → Personal access tokens
2. Click "Generate new token"
3. Select scopes: `repo` (full control)
4. Copy token and use as password

**If using SSH:**
- SSH key should be automatically detected
- Configure SSH key: https://docs.github.com/en/authentication/connecting-to-github-with-ssh

---

## Step 3: Verify Push

```bash
# Check if push was successful
git log --oneline -5

# View on GitHub
# Open: https://github.com/yourusername/equipment-borrowing-system
```

---

## Quick Reference Commands

```bash
# Clone the repository
git clone https://github.com/yourusername/equipment-borrowing-system.git

# View git status
git status

# Create new branch for feature
git checkout -b feature/new-feature

# Add changes
git add .

# Commit changes
git commit -m "Description of changes"

# Push branch
git push origin feature/new-feature

# Create Pull Request on GitHub UI
# Then merge after review

# Pull latest changes
git pull origin main
```

---

## Branching Strategy

### Recommended Branch Structure

```
main/master
  ↓
develop (development branch)
  ├─ feature/user-authentication
  ├─ feature/equipment-management
  ├─ feature/borrowing-system
  └─ bugfix/cors-issue
```

### Create Development Branch

```bash
git checkout -b develop
git push -u origin develop
```

### Feature Development Workflow

```bash
# Start new feature from develop
git checkout develop
git pull origin develop
git checkout -b feature/new-feature

# Make changes, commit
git commit -am "Add new feature description"

# Push feature branch
git push origin feature/new-feature

# Create Pull Request on GitHub
# After review and approval, merge to develop
```

---

## CI/CD Integration (Optional)

### GitHub Actions for Automated Testing

Create `.github/workflows/build.yml`:

```yaml
name: Build & Test

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up Java
      uses: actions/setup-java@v2
      with:
        java-version: '21'
    
    - name: Build with Maven
      run: |
        cd user-service && mvn clean package && cd ..
        cd equipment-service && mvn clean package && cd ..
        cd borrowing-service && mvn clean package && cd ..
        cd api-gateway/api-gateway && mvn clean package && cd ../../
    
    - name: Run Tests
      run: |
        cd user-service && mvn test && cd ..
        cd equipment-service && mvn test && cd ..
        cd borrowing-service && mvn test && cd ..
```

---

## Deployment Options

### Option 1: Heroku Deployment

1. Create Heroku account: https://www.heroku.com
2. Install Heroku CLI
3. Create Procfile in project root:

```
web: java -jar api-gateway/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
```

4. Deploy:
```bash
heroku login
heroku create your-app-name
git push heroku main
```

### Option 2: AWS EC2

1. Launch EC2 instance
2. Install Java 21, Maven, MySQL
3. Clone repository
4. Build and run services

### Option 3: Docker & DockerHub

1. Create DockerHub account: https://hub.docker.com
2. Build and push images:

```bash
# Login to DockerHub
docker login

# Build images
docker build -t yourusername/equipment-borrow-user-service ./user-service
docker build -t yourusername/equipment-borrow-equipment-service ./equipment-service
docker build -t yourusername/equipment-borrow-borrowing-service ./borrowing-service
docker build -t yourusername/equipment-borrow-api-gateway ./api-gateway/api-gateway
docker build -t yourusername/equipment-borrow-frontend ./frontend

# Push images
docker push yourusername/equipment-borrow-user-service
docker push yourusername/equipment-borrow-equipment-service
docker push yourusername/equipment-borrow-borrowing-service
docker push yourusername/equipment-borrow-api-gateway
docker push yourusername/equipment-borrow-frontend
```

---

## GitHub Issues & Project Management

### Create Issues for Tracking

```
Title: Implement Equipment Search Feature
Description:
- Add search endpoint to equipment service
- Support filtering by name, category, availability
- Add Elasticsearch integration

Labels: enhancement, feature-request
Assignee: @yourusername
```

### Project Boards

Go to GitHub Repository → Projects → New Project
- Create kanban board
- Add columns: To Do, In Progress, Done
- Link issues to board

---

## Documentation on GitHub

### Create Wiki

Go to Repository → Wiki
Add pages for:
- System Architecture
- Developer Guide
- Deployment Guide
- Troubleshooting

### GitHub Pages (Optional)

1. Enable Pages in Settings
2. Create gh-pages branch
3. Add documentation HTML/Markdown
4. Access at: https://yourusername.github.io/equipment-borrowing-system

---

## Release Management

### Create Release

```bash
# Create and tag release
git tag -a v1.0.0 -m "Initial production release"
git push origin v1.0.0

# On GitHub: Releases → Draft new release
# Select tag, add changelog, mark as release
```

### Semantic Versioning

- MAJOR.MINOR.PATCH (e.g., 1.0.0)
- MAJOR: Breaking changes
- MINOR: New features
- PATCH: Bug fixes

---

## Troubleshooting

### Authentication Issues

```bash
# Cache credentials
git config --global credential.helper cache

# Remove cached credentials (if wrong)
git credential-manager erase <url>

# Or use SSH with proper key setup
ssh-keygen -t ed25519 -C "your-email@example.com"
```

### Push Rejected - Upstream Changes

```bash
# Pull latest changes first
git pull origin main

# Resolve conflicts if any, then push
git push origin main
```

### Accidentally Committed Large Files

```bash
# Remove from git history
git rm --cached large-file.jar
echo "large-file.jar" >> .gitignore
git commit -m "Remove large file from tracking"
git push origin main
```

---

## Repository Settings Checklist

- [ ] Add collaborators (Settings → Collaborators → Add people)
- [ ] Set branch protection rules (Settings → Branches)
- [ ] Configure PR requirements (require approval before merge)
- [ ] Set up issue templates (/.github/ISSUE_TEMPLATE/)
- [ ] Set up PR template (/.github/pull_request_template.md)
- [ ] Enable discussions (Settings → General → Discussions)
- [ ] Set up GitHub Pages documentation
- [ ] Configure secrets for CI/CD (Settings → Secrets)

---

## Project Communication

### Use Issues for
- Bug reports
- Feature requests
- Documentation improvements
- Technical discussions

### Use Discussions for
- Questions and answers
- Show and tell
- Ideas and brainstorming
- General announcements

### PR Comments for
- Code review feedback
- Suggestions for improvements
- Approval/Request changes

---

## Monthly Maintenance

- [ ] Review open issues
- [ ] Merge stale branches
- [ ] Update dependencies (check for security updates)
- [ ] Review and archive old releases
- [ ] Update documentation
- [ ] Check GitHub Actions for failures

---

## Further Resources

- GitHub Docs: https://docs.github.com
- Git Documentation: https://git-scm.com/doc
- GitHub Actions: https://github.com/features/actions
- GitHub CLI: https://cli.github.com/

---

## Next Steps

1. ✅ Create GitHub repository
2. ✅ Add remote and push code
3. ✅ Set up collaborators
4. ✅ Configure branch protection
5. ✅ Set up GitHub Pages documentation
6. ✅ Configure GitHub Actions CI/CD
7. ✅ Set up issue templates
8. ✅ Deploy to production (Heroku/AWS/Docker)
9. ✅ Monitor and maintain

---

## Support & Questions

For questions about git workflow, open an issue on GitHub with the label `documentation` or `question`.

Last Updated: May 9, 2026
Maintained by: Development Team

