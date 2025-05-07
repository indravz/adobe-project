# IAM Role: ECS Task Execution Role
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecsTaskExecutionRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      },
    ]
  })
}

# Attach AWS-managed policy for ECS task execution
resource "aws_iam_role_policy_attachment" "ecs_execution_policy_attachment" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy" "ecs_task_execution_logs_policy" {
  name   = "ecsTaskExecutionLogsPolicy"
  role   = aws_iam_role.ecs_task_execution_role.name
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action   = ["logs:CreateLogStream", "logs:PutLogEvents", "logs:*"]
        Effect   = "Allow"
        Resource = "*"
      }
    ]
  })
}

# Attach policy for ECS task to access ECR (Required for pulling images)
resource "aws_iam_policy" "ecs_ecr_policy" {
  name        = "EcsEcrPolicy"
  description = "Allow ECS tasks to pull images from ECR"
  policy      = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action   = ["ecr:GetAuthorizationToken", "ecr:BatchGetImage", "ecr:BatchCheckLayerAvailability"]
        Effect   = "Allow"
        Resource = "arn:aws:ecr:us-east-1:653614599029:repository/adobe-project"  # Change this to your ECR repo ARN
      }
    ]
  })
}

# Attach ECR access policy to ECS Task Execution Role
resource "aws_iam_role_policy_attachment" "ecs_ecr_policy_attachment" {
  policy_arn = aws_iam_policy.ecs_ecr_policy.arn
  role       = aws_iam_role.ecs_task_execution_role.name
}

# IAM Role: ECS Task Role (for application-level access)
resource "aws_iam_role" "ecs_task_role" {
  name = "ecsTaskRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy" "ecs_exec_policy" {
  name = "ecs-exec-policy"
  role = aws_iam_role.ecs_task_execution_role.name

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "ssmmessages:CreateControlChannel",
          "ssmmessages:CreateDataChannel",
          "ssmmessages:OpenControlChannel",
          "ssmmessages:OpenDataChannel"
        ],
        Resource = "*"
      }
    ]
  })
}
