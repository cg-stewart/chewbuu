terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  backend "s3" {
    # Configure your backend settings here
  }
}

provider "aws" {
  region = "us-east-1"
}

# VPC and Networking
module "vpc" {
  source = "terraform-aws-modules/vpc/aws"

  name = "chewbuu-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["us-east-1a", "us-east-1b", "us-east-1c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]

  enable_nat_gateway = true
  single_nat_gateway = true
}

# ECS Cluster
resource "aws_ecs_cluster" "main" {
  name = "chewbuu-cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

# S3 Buckets
resource "aws_s3_bucket" "videos" {
  bucket = "chewbuu-videos"
}

resource "aws_s3_bucket_versioning" "videos" {
  bucket = aws_s3_bucket.videos.id
  versioning_configuration {
    status = "Enabled"
  }
}

# DynamoDB Tables
resource "aws_dynamodb_table" "chat_messages" {
  name           = "chat-messages"
  billing_mode   = "PAY_PER_REQUEST"
  hash_key       = "chat_id"
  range_key      = "timestamp"

  attribute {
    name = "chat_id"
    type = "S"
  }

  attribute {
    name = "timestamp"
    type = "N"
  }
}

# Cognito User Pool
resource "aws_cognito_user_pool" "main" {
  name = "chewbuu-users"

  password_policy {
    minimum_length    = 8
    require_lowercase = true
    require_numbers   = true
    require_symbols   = true
    require_uppercase = true
  }

  schema {
    name                = "age"
    attribute_data_type = "Number"
    required           = true
    mutable            = false
  }
}

# RDS Instance
resource "aws_db_instance" "main" {
  identifier        = "chewbuu-db"
  engine           = "postgres"
  engine_version   = "15.5"
  instance_class   = "db.t3.micro"
  allocated_storage = 20

  db_name  = "chewbuu"
  username = "chewbuu_admin"
  password = "CHANGE_ME_BEFORE_APPLYING"

  vpc_security_group_ids = [aws_security_group.rds.id]
  db_subnet_group_name   = aws_db_subnet_group.main.name

  backup_retention_period = 7
  skip_final_snapshot    = true
}

# Security Groups
resource "aws_security_group" "rds" {
  name        = "chewbuu-rds-sg"
  description = "Security group for RDS"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs_tasks.id]
  }
}
