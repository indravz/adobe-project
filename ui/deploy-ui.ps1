# AWS and ECR configuration
$accountId = "653614599029"
$region = "us-east-1"
$repositoryName = "adobe-project-ui"
$imageTag = "v1.0.5"

# Login to ECR
aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin "${accountId}.dkr.ecr.${region}.amazonaws.com"

# Build the Docker image (React app with Nginx)
docker build -t "${repositoryName}:${imageTag}" .

# Tag the image for ECR
docker tag "${repositoryName}:${imageTag}" "${accountId}.dkr.ecr.${region}.amazonaws.com/${repositoryName}:${imageTag}"

# Push the image to ECR
docker push "${accountId}.dkr.ecr.${region}.amazonaws.com/${repositoryName}:${imageTag}"
