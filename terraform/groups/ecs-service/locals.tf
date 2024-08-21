# Define all hardcoded local variable and local variables looked up from data resources

locals {
  stack_name                 = "filing-maintain" # this must match the stack name the service deploys into
  name_prefix                = "${local.stack_name}-${var.environment}"
  global_prefix              = "global-${var.environment}"
  service_name               = "company-accounts-web"
  container_port             = "8080"
  docker_repo                = "company-accounts.web.ch.gov.uk"
  kms_alias                  = "alias/${var.aws_profile}/environment-services-kms"
  lb_listener_rule_priority  = 66
  lb_listener_paths          = ["/company/*/select-account-type","/company/*/transaction/*/company-accounts/*/cic/accounts-start","/company/*/transaction/*/company-accounts/*/cic/approval","/accounts/cic/before-you-start","/accounts/cic/cant-file-online-yet","/accounts/cic/*/cant-file-online-yet","/accounts/company/*/cic/details","/accounts/cic/criteria","/accounts/cic/*/criteria","/accounts/cic/full-accounts-criteria","/accounts/cic/*/full-accounts-criteria","/accounts/cic/cics-file-paper","/accounts/cic/*/cics-file-paper","/company/*/transaction/*/company-accounts/*/cic/review","/accounts/cic/select-account-type","/accounts/cic/*/select-account-type","/company/*/cic/steps-to-complete","/company/*/transaction/*/company-accounts/*/cic/company-activity","/company/*/transaction/*/company-accounts/*/cic/consultation","/company/*/transaction/*/company-accounts/*/cic/consultation-with-stakeholders-selection","/company/*/transaction/*/company-accounts/*/cic/directors-remuneration","/company/*/transaction/*/company-accounts/*/cic/directors-remuneration-selection","/company/*/transaction/*/company-accounts/*/cic/transfer-of-assets","/company/*/transaction/*/company-accounts/*/cic/transfer-of-assets-selection","/company/*/corporation-tax","/company/*/dormant/criteria","/accounts/full-accounts-criteria","/accounts/alternative-filing-options","/accounts/company/*/details","/accounts/corporation-tax","/accounts/criteria","/accounts/select-account-type","/company/*/micro-entity/criteria","/company/*/transaction/*/company-accounts/*/pay-filing-fee","/company/*/transaction/*/company-accounts/*/small-full/approved-accounts","/company/*/transaction/*/company-accounts/*/small-full/accounts-reference-date","/company/*/transaction/*/company-accounts/*/small-full/accounts-reference-date-question","/company/*/transaction/*/company-accounts/*/small-full/add-or-remove-directors","/company/*/transaction/*/company-accounts/*/small-full/note/add-or-remove-loans","/company/*/transaction/*/company-accounts/*/small-full/approval","/company/*/transaction/*/company-accounts/*/small-full/balance-sheet","/company/*/transaction/*/company-accounts/*/small-full/basis-of-preparation","/company/*/transaction/*/company-accounts/*/small-full/directors-report/company-policy-on-disabled-employees","/company/*/transaction/*/company-accounts/*/small-full/directors-report/company-policy-on-disabled-employees-question","/company/*/transaction/*/company-accounts/*/small-full/creditors-after-more-than-one-year","/company/*/transaction/*/company-accounts/*/small-full/creditors-within-one-year","/company/*/small-full/criteria","/company/*/transaction/*/company-accounts/*/small-full/current-assets-investments","/company/*/transaction/*/company-accounts/*/small-full/debtors","/company/*/transaction/*/company-accounts/*/small-full/directors-report/additional-information","/company/*/transaction/*/company-accounts/*/small-full/directors-report/additional-information-question","/company/*/transaction/*/company-accounts/*/small-full/directors-report/approval","/company/*/transaction/*/company-accounts/*/small-full/directors-report-question","/company/*/transaction/*/company-accounts/*/small-full/directors-report/review","/company/*/transaction/*/company-accounts/*/small-full/employees","/company/*/transaction/*/company-accounts/*/small-full/financial-commitments","/company/*/transaction/*/company-accounts/*/small-full/financial-commitments-question","/company/*/transaction/*/company-accounts/*/small-full/fixed-assets-investments","/company/*/transaction/*/company-accounts/*/small-full/intangible-fixed-assets-amortisation","/company/*/transaction/*/company-accounts/*/small-full/note/intangible-assets","/company/*/transaction/*/company-accounts/*/small-full/notes/add-or-remove-loans/additional-information","/company/*/transaction/*/company-accounts/*/small-full/notes/add-or-remove-loans/additional-information-question","/company/*/transaction/*/company-accounts/*/small-full/notes/loans-to-directors-question","/company/*/transaction/*/company-accounts/*/small-full/off-balance-sheet-arrangements","/company/*/transaction/*/company-accounts/*/small-full/off-balance-sheet-arrangements-question","/company/*/transaction/*/company-accounts/*/small-full/other-accounting-policies","/company/*/transaction/*/company-accounts/*/small-full/directors-report/political-and-charitable-donations","/company/*/transaction/*/company-accounts/*/small-full/directors-report/political-and-charitable-donations-question","/company/*/transaction/*/company-accounts/*/small-full/directors-report/principal-activities","/company/*/transaction/*/company-accounts/*/small-full/directors-report/principal-activities-question","/company/*/transaction/*/company-accounts/*/small-full/profit-and-loss","/company/*/transaction/*/company-accounts/*/small-full/profit-and-loss-question","/company/*/transaction/*/company-accounts/*/small-full/resume","/company/*/transaction/*/company-accounts/*/resume","/company/*/transaction/*/company-accounts/*/small-full/review","/company/*/transaction/*/company-accounts/*/small-full/balance-sheet-statements","/company/*/small-full/steps-to-complete","/company/*/transaction/*/company-accounts/*/small-full/steps-to-complete","/company/*/transaction/*/company-accounts/*/small-full/stocks","/company/*/transaction/*/company-accounts/*/small-full/note/tangible-assets","/company/*/transaction/*/company-accounts/*/small-full/tangible-depreciation-policy","/company/*/transaction/*/company-accounts/*/small-full/turnover-policy","/company/*/transaction/*/company-accounts/*/small-full/valuation-information"]
  healthcheck_path           = "/company-accounts-web/healthcheck" #healthcheck path for company accounts web
  healthcheck_matcher        = "200"
  s3_config_bucket           = data.vault_generic_secret.shared_s3.data["config_bucket_name"]
  app_environment_filename   = "company-accounts.web.ch.gov.uk.env"
  use_set_environment_files  = var.use_set_environment_files
  application_subnet_ids     = data.aws_subnets.application.ids
  application_subnet_pattern = local.stack_secrets["application_subnet_pattern"]

  stack_secrets   = jsondecode(data.vault_generic_secret.stack_secrets.data_json)
  service_secrets = jsondecode(data.vault_generic_secret.service_secrets.data_json)

  vpc_name = local.stack_secrets["vpc_name"]

  # create a map of secret name => secret arn to pass into ecs service module
  # using the trimprefix function to remove the prefixed path from the secret name
  secrets_arn_map = {
    for sec in data.aws_ssm_parameter.secret :
    trimprefix(sec.name, "/${local.name_prefix}/") => sec.arn
  }

  global_secrets_arn_map = {
    for sec in data.aws_ssm_parameter.global_secret :
    trimprefix(sec.name, "/${local.global_prefix}/") => sec.arn
  }

  global_secret_list = flatten([for key, value in local.global_secrets_arn_map :
    { "name" = upper(key), "valueFrom" = value }
  ])

  ssm_global_version_map = [
    for sec in data.aws_ssm_parameter.global_secret :
      { "name"  = "GLOBAL_${var.ssm_version_prefix}${replace(upper(basename(sec.name)), "-", "_")}", "value" = sec.version }
  ]

  service_secrets_arn_map = {
    for sec in module.secrets.secrets :
    trimprefix(sec.name, "/${local.service_name}-${var.environment}/") => sec.arn
  }

  service_secret_list = flatten([for key, value in local.service_secrets_arn_map :
    { "name" = upper(key), "valueFrom" = value }
  ])

  ssm_service_version_map = [
    for sec in module.secrets.secrets :
      { "name"  = "${replace(upper(local.service_name), "-", "_")}_${var.ssm_version_prefix}${replace(upper(basename(sec.name)), "-", "_")}", "value" = sec.version }
  ]

  # secrets to go in list
  task_secrets = concat(local.global_secret_list,local.service_secret_list)

  task_environment = concat(local.ssm_global_version_map,local.ssm_service_version_map,[
    { "name" : "PORT", "value" : local.container_port }
  ])
}
