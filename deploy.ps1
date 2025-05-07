# Replace these placeholders before running:
# <account-id>: Your AWS Account ID
# <region>: Your AWS region (e.g., us-east-1)
# <repository-name>: Name of your ECR repository (e.g., adobe-project)
# <image-tag>: Version tag (e.g., v1.0.0)

$accountId = "653614599029"
$region = "us-east-1"
$repositoryName = "adobe-project"
$imageTag = "v1.0.2"

# Login to ECR
aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin "${accountId}.dkr.ecr.${region}.amazonaws.com"

# Build the Docker image
docker build -t "${repositoryName}:${imageTag}" .

# Tag the image for ECR
docker tag "${repositoryName}:${imageTag}" "${accountId}.dkr.ecr.${region}.amazonaws.com/${repositoryName}:${imageTag}"

# Push the image to ECR
docker push "${accountId}.dkr.ecr.${region}.amazonaws.com/${repositoryName}:${imageTag}"