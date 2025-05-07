terraform {
  required_providers {
    aws = {
      version = "~>5.80.0"
    }
  }

   backend "s3" {
      bucket = "terraform-backend-adobe-project"
      key = "backend/terraform.tfstate"
      region = "us-east-1"
      use_lockfile = true
      encrypt = true

    }

  //required_version = "~> 1.6.6" #terraform-cli version
}

provider "aws" {
  # region = "us-east-2"
  region = "us-east-1"
}

