# HTTP API
resource "aws_apigatewayv2_api" "http_api" {
  name          = "backend-http-api"
  protocol_type = "HTTP"
  description   = "HTTP API Gateway for private ALB integration"

  cors_configuration {
    allow_origins     = ["https://indras.adobe-project.online", "http://localhost"]
    allow_methods     = ["GET", "POST", "OPTIONS", "DELETE" , "PUT" , "OPTIONS"]
    allow_headers     = ["authorization", "content-type"]
    allow_credentials = true
    max_age           = 3600
  }
}

# VPC Link for ALB
resource "aws_apigatewayv2_vpc_link" "vpc_link" {
  name = "private-alb-vpc-link"

  subnet_ids = [
    aws_subnet.private_subnet_1.id,
    aws_subnet.private_subnet_2.id
  ]

  security_group_ids = [aws_security_group.internal_all_ports_sg.id]
}

resource "aws_apigatewayv2_integration" "alb_integration" {
  api_id                 = aws_apigatewayv2_api.http_api.id
  integration_type       = "HTTP_PROXY"
  integration_method     = "ANY"
  integration_uri        = aws_lb_listener.backend_listener.arn
  connection_type        = "VPC_LINK"
  connection_id          = aws_apigatewayv2_vpc_link.vpc_link.id
  payload_format_version = "1.0"
}

resource "aws_apigatewayv2_route" "proxy_route" {
  api_id    = aws_apigatewayv2_api.http_api.id
  route_key = "ANY /api/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"


   authorization_type = "JWT"
  authorizer_id      = aws_apigatewayv2_authorizer.cognito_auth.id
}

resource "aws_apigatewayv2_stage" "default_stage" {
  api_id      = aws_apigatewayv2_api.http_api.id
  name        = "$default"
  auto_deploy = true

  default_route_settings {
    throttling_burst_limit = 5
    throttling_rate_limit  = 2
  }

  access_log_settings {
    destination_arn = "arn:aws:logs:us-east-1:653614599029:log-group:apigwlogs"
    format = jsonencode({
      httpMethod     = "$context.httpMethod"
      ip             = "$context.identity.sourceIp"
      protocol       = "$context.protocol"
      requestId      = "$context.requestId"
      requestTime    = "$context.requestTime"
      responseLength = "$context.responseLength"
      routeKey       = "$context.routeKey"
      status         = "$context.status"
    })
  }
}


resource "aws_apigatewayv2_authorizer" "cognito_auth" {
  api_id = aws_apigatewayv2_api.http_api.id

  authorizer_type = "JWT"
  identity_sources = ["$request.header.Authorization"]
  name = "cognito-authorizer"

  jwt_configuration {
    audience = [aws_cognito_user_pool_client.user_pool_client.id, "1igkruov9qivq8vibuerkqf3mo"]
    issuer   = format("https://cognito-idp.us-east-1.amazonaws.com/%s", aws_cognito_user_pool.user_pool.id)
  }
}
