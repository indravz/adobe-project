resource "aws_db_instance" "db_old" {
  allocated_storage                     = 20
  auto_minor_version_upgrade            = true
  availability_zone                     = "us-east-1c"
  backup_retention_period               = 1
  backup_target                         = "region"
  backup_window                         = "06:57-07:27"
  ca_cert_identifier                    = "rds-ca-rsa2048-g1"
  copy_tags_to_snapshot                 = true
  customer_owned_ip_enabled             = false
  db_subnet_group_name                  = "default-vpc-079cea719faa6ed62"
  dedicated_log_volume                  = false
  delete_automated_backups              = true
  deletion_protection                   = false
  engine                                = "postgres"
  engine_lifecycle_support              = "open-source-rds-extended-support-disabled"
  engine_version                        = "17.2"
  iam_database_authentication_enabled   = true
  identifier                            = "tradingapp"
  instance_class                        = "db.t4g.micro"
  iops                                  = 0
  kms_key_id                            = "arn:aws:kms:us-east-1:653614599029:key/83fa2b3c-ef06-4410-b592-f98c5d3dc1c7"
  license_model                         = "postgresql-license"
  maintenance_window                    = "wed:04:18-wed:04:48"
  max_allocated_storage                 = 1000
  monitoring_interval                   = 0
  multi_az                              = false
  network_type                          = "IPV4"
  option_group_name                     = "default:postgres-17"
  parameter_group_name                  = "default.postgres17"
  performance_insights_enabled          = true
  performance_insights_kms_key_id       = "arn:aws:kms:us-east-1:653614599029:key/83fa2b3c-ef06-4410-b592-f98c5d3dc1c7"
  performance_insights_retention_period = 7
  port                                  = 5432
  publicly_accessible                   = true
  skip_final_snapshot                   = true
  storage_encrypted                     = true
  storage_type                          = "gp2"
  tags                                  = {}
  tags_all                              = {}
  username                              = "postgres"
  vpc_security_group_ids                = [
    "sg-00943b55cbc859a0b",
  ]
}
