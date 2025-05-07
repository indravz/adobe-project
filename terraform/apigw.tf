# HTTP API
resource "aws_apigatewayv2_api" "http_api" {
  name          = "backend-http-api"
  protocol_type = "HTTP"
  description   = "HTTP API Gateway for private ALB integration"
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

resource "aws_apigatewayv2_authorizer" "cognito_auth" {
  api_id = aws_apigatewayv2_api.http_api.id

  authorizer_type = "JWT"
  identity_sources = ["$request.header.Authorization"]
  name = "cognito-authorizer"

  jwt_configuration {
    audience = [aws_cognito_user_pool_client.user_pool_client.id]
    issuer   = format("https://cognito-idp.us-east-1.amazonaws.com/%s", aws_cognito_user_pool.user_pool.id)
  }
}


resource "aws_apigatewayv2_stage" "default_stage" {
  api_id      = aws_apigatewayv2_api.http_api.id
  name        = "$default"
  auto_deploy = true
}
