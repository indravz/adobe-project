# Cognito User Pool
resource "aws_cognito_user_pool" "user_pool" {
  name = "adobe-project-user-pool"

  username_attributes = ["email"]
  mfa_configuration   = "OFF"
  auto_verified_attributes = ["email"]

  tags = {
    Name = "Adobe Project User Pool"
  }
}

// Cognito Client
resource "aws_cognito_user_pool_client" "user_pool_client" {
  name               = "adobe-project-client-with-secret"
  user_pool_id       = aws_cognito_user_pool.user_pool.id
  generate_secret    = false

  allowed_oauth_flows           = ["code", "implicit"]
  allowed_oauth_scopes          = ["email", "openid"]
  supported_identity_providers  = ["COGNITO"]
  callback_urls                 = ["http://localhost:3000/callback"]  # Your React App URL
  logout_urls                   = ["http://localhost:3000/logout"]  # Logout URL
}

output "user_pool_client_id" {
  value = aws_cognito_user_pool_client.user_pool_client.id
}

output "user_pool_client_secret" {
  value = aws_cognito_user_pool_client.user_pool_client.client_secret
  sensitive = true
}

#####============================#########===============#########

/*
resource "aws_cognito_user_pool" "backend_user_pool" {
  name = "backend-user-pool"
}

resource "aws_cognito_resource_server" "api_resource_server" {
  name         = "backend-api-server"
  identifier   = "backend-api"
  user_pool_id = aws_cognito_user_pool.backend_user_pool.id

  scope {
    scope_name        = "read"
    scope_description = "Read access to the backend API"
  }

  scope {
    scope_name        = "write"
    scope_description = "Write access to the backend API"
  }
}

resource "aws_cognito_user_pool_domain" "backend_user_pool_domain" {
  domain       = "adobe-project-backend"
  user_pool_id = aws_cognito_user_pool.backend_user_pool.id
}

resource "aws_cognito_user_pool_client" "backend_user_pool_client" {
  name         = "backend-user-client"
  user_pool_id = aws_cognito_user_pool.backend_user_pool.id

  generate_secret = true

  allowed_oauth_flows                    = ["client_credentials"]
  allowed_oauth_flows_user_pool_client  = true
  allowed_oauth_scopes = [
    "backend-api/read",
    "backend-api/write"
  ]

  explicit_auth_flows = [
    "ALLOW_ADMIN_USER_PASSWORD_AUTH",
    "ALLOW_CUSTOM_AUTH",
    "ALLOW_USER_PASSWORD_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH"
  ]
}

output "backend_user_pool_client_id" {
  value = aws_cognito_user_pool_client.backend_user_pool_client.id
}

output "backend_user_pool_client_secret" {
  value     = aws_cognito_user_pool_client.backend_user_pool_client.client_secret
  sensitive = true
}
*/