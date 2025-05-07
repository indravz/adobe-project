resource "aws_ecr_repository" "adobe_project" {
  name = "adobe-project"
}

resource "aws_ecr_repository" "adobe_project_ui" {
  name = "adobe-project-ui"
}

resource "aws_vpc_endpoint" "ecr" {
  vpc_id              = aws_vpc.main.id
  service_name        = "com.amazonaws.us-east-1.ecr.api"
  subnet_ids          = [aws_subnet.private_subnet_1.id, aws_subnet.private_subnet_2.id]
  security_group_ids  = [aws_security_group.internal_all_ports_sg.id]
  vpc_endpoint_type = "Interface"
  private_dns_enabled = true
}

resource "aws_vpc_endpoint" "ecr_docker" {
  vpc_id              = aws_vpc.main.id
  service_name        = "com.amazonaws.us-east-1.ecr.dkr"
  subnet_ids          = [aws_subnet.private_subnet_1.id, aws_subnet.private_subnet_2.id]
  vpc_endpoint_type   = "Interface"
  security_group_ids  = [aws_security_group.internal_all_ports_sg.id]
  private_dns_enabled = true
}

# S3 Endpoint
resource "aws_vpc_endpoint" "s3" {
  vpc_id       = aws_vpc.main.id
  service_name = "com.amazonaws.us-east-1.s3"
  vpc_endpoint_type = "Gateway"

  route_table_ids = [
    aws_route_table.private_route_table.id
  ]

  tags = {
    Name = "s3-vpc-endpoint"
  }
}

resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "private-route-table"
  }
}

resource "aws_route_table_association" "private_subnet_1_association" {
  subnet_id      = aws_subnet.private_subnet_1.id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_route_table_association" "private_subnet_2_association" {
  subnet_id      = aws_subnet.private_subnet_2.id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_vpc_endpoint" "cloudwatch_logs_vpc_endpoint" {
  vpc_id            = aws_vpc.main.id
  service_name      = "com.amazonaws.us-east-1.logs"
  vpc_endpoint_type = "Interface"
  subnet_ids        = [aws_subnet.private_subnet_1.id, aws_subnet.private_subnet_2.id]  # Subnets where you want the VPC endpoint
  security_group_ids = [aws_security_group.internal_all_ports_sg.id]  # Security group for the VPC endpoint
  private_dns_enabled = true

  tags = {
    Name = "CloudWatch Logs VPC Endpoint"
  }
}


