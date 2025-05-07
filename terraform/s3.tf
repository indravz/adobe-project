resource "aws_s3_bucket" "terraform_backend_adobe_project" {
    bucket = "terraform-backend-adobe-project"
    object_lock_enabled = true
    tags = {
      Name = "terraform-backend-adobe-project"
    }

}

resource "aws_s3_bucket_versioning" "terraform_backend_adobe_project" {
    bucket = "terraform-backend-adobe-project"
    versioning_configuration {
      status = "Enabled"
    }

}