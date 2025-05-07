# ACM Certificate for - indras.adobe-project.online)
resource "aws_acm_certificate" "frontend_certificate" {
  domain_name = "adobe-project.online"
  validation_method = "DNS"
  tags = {
    Name = "Frontend Certificate"
  }
}

resource "aws_acm_certificate" "frontend_certificate_1" {
  domain_name = "indras.adobe-project.online"
  validation_method = "DNS"
  tags = {
    Name = "Frontend Certificate"
  }
}

resource "aws_acm_certificate" "cognito_auth_certificate" {
  domain_name = "auth.adobe-project.online"
  validation_method = "DNS"
  tags = {
    Name = "cognito domain Certificate"
  }
}

resource "aws_lb" "frontend_alb" {
  name               = "frontend-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups   = [aws_security_group.frontend_sg.id]
  subnets            = [aws_subnet.public_subnet_1.id, aws_subnet.public_subnet_2.id]  # Two subnets in different AZs
  enable_deletion_protection = false
  enable_http2 = true

  tags = {
    Name = "Frontend ALB"
  }
}

resource "aws_lb_target_group" "frontend_target_group" {
  name     = "frontend-target-group"
  port     = 80
  protocol = "HTTP"
  vpc_id   = aws_vpc.main.id
  target_type = "ip"  # Ensure target type is 'ip' for Fargate tasks
}

resource "aws_lb_listener" "frontend_listener" {
  load_balancer_arn = aws_lb.frontend_alb.arn
  port              = 443
  protocol          = "HTTPS"

  certificate_arn = aws_acm_certificate.frontend_certificate_1.arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.frontend_target_group.arn
  }
}

resource "aws_cloudwatch_log_group" "ecs_frontend" {
  name              = "/ecs/frontend-service"
  retention_in_days = 7
}


resource "aws_ecs_task_definition" "frontend_task" {
  family                = "frontend-task"
  execution_role_arn    = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn         = aws_iam_role.ecs_task_role.arn
  network_mode          = "awsvpc"
  container_definitions = jsonencode([{
    name      = "frontend-container"
    image     = "653614599029.dkr.ecr.us-east-1.amazonaws.com/adobe-project-ui:v1.0.5"
    essential = true
    portMappings = [
      {
        containerPort = 80
        hostPort      = 80
      }
    ]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        awslogs-group         = "/ecs/frontend-service"
        awslogs-region        = "us-east-1"
        awslogs-stream-prefix = "ecs"
      }
    }
  }])

  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"  # Lower CPU spec
  memory                   = "512"  # Lower memory spec
}

resource "aws_ecs_service" "frontend_service" {
  name            = "frontend-service"
  cluster         = aws_ecs_cluster.app_cluster.id
  task_definition = aws_ecs_task_definition.frontend_task.arn
  desired_count   = 1  # Reduced number of tasks to save cost
  launch_type     = "FARGATE"
  enable_execute_command = true
  network_configuration {
    subnets          = [aws_subnet.public_subnet_1.id, aws_subnet.public_subnet_2.id]  # Two subnets for high availability
    security_groups = [aws_security_group.frontend_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.frontend_target_group.arn
    container_name   = "frontend-container"
    container_port   = 80
  }
}
