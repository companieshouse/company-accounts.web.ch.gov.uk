# company-accounts.web.ch.gov.uk
The Companies House web application for handling company accounts
These screens allow clients to interact with company accounts data.

### Requirements

- Java 21
- Maven
- Git
- Accounts platform

### Get Started Using Docker

#### Docker Set-up

1. Start Docker
2. On the command line, export the intended AWS profile using command: 'export AWS_PROFILE={development-eu-west-2}'
3. Login to AWS using Single-sign-on: 'AWS SSO LOGIN'
4. Then run 'aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 169942020521.dkr.ecr.eu-west-1.amazonaws.com'

#### Enable company accounts development workflow
1. To enable the accounts module when you are tilting up, run the command: './bin/chs-dev modules enable accounts'
2. To enable development workflow and make changes to company-accounts-web, run the command: './bin/chs-dev development enable company-accounts-web-ch-gov-uk' 
3. To start up the services enabled through the accounts module activation, run the command: 'tilt up' and stop them using 'tilt down'
4. When services are up and running, open http://chs.local in the browser and log in to CHS platform using username demo@ch.gov.uk and password.

### Deployment

Go to Concourse (https://ci.platform.aws.chdev.org/teams/team-aardvark/pipelines/company-accounts.web.ch.gov.uk), select CIDEV to take you to the builds, and when you are ready to deploy - click the (+) button on the green banner in the top right of the page.

If you cannot access the builds, you may require access rights to be granted. Raise a ticket for these and follow the steps below for manual deployment.

### Manual Deployment
- login to AWS
- Go to the development release bucket in the S3 folder - development-eu-west-2.release.ch.gov.uk
- Within this bucket, navigate to the company-accounts.web.ch.gov.uk/ folder
- Use the ‘Last modified’ tab to sort the zip files by date and select the most recent (verifying that the release number is the same as the artefact on github)
- Select this file, click ‘actions’ and ‘copy’ - then copy this to the release folder titled - release.ch.gov.uk
- The go to the CIDEV deployer, select ‘deploy an app’ at the top, select company-accounts-web and put in the release number
- You can use Marathon to verify that the service is running and suspend or destroy any old version of the service which are still running

### Test
Use command ‘make test’ to run test suite locally

## Terraform ECS

### What does this code do?

The code present in this repository is used to define and deploy a dockerised container in AWS ECS.
This is done by calling a [module](https://github.com/companieshouse/terraform-modules/tree/main/aws/ecs) from terraform-modules. Application specific attributes are injected and the service is then deployed using Terraform via the CICD platform 'Concourse'.


Application specific attributes | Value                                | Description
:---------|:-----------------------------------------------------------------------------|:-----------
**ECS Cluster**        | filing-maintain                                  | ECS cluster (stack) the service belongs to
**Load balancer**      | {env}-chs-chgovuk                                | The load balancer that sits in front of the service
**Concourse pipeline**     |[Pipeline link](https://ci-platform.companieshouse.gov.uk/teams/team-development/pipelines/company-accounts.web.ch.gov.uk) <br> [Pipeline code](https://github.com/companieshouse/ci-pipelines/blob/master/pipelines/ssplatform/team-development/company-accounts.web.ch.gov.uk)                                  | Concourse pipeline link in shared services


### Contributing
- Please refer to the [ECS Development and Infrastructure Documentation](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/4390649858/Copy+of+ECS+Development+and+Infrastructure+Documentation+Updated) for detailed information on the infrastructure being deployed.

### Testing
- Ensure the terraform runner local plan executes without issues. For information on terraform runners please see the [Terraform Runner Quickstart guide](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/1694236886/Terraform+Runner+Quickstart).
- If you encounter any issues or have questions, reach out to the team on the **#platform** slack channel.

### Vault Configuration Updates
- Any secrets required for this service will be stored in Vault. For any updates to the Vault configuration, please consult with the **#platform** team and submit a workflow request.

### Useful Links
- [ECS service config dev repository](https://github.com/companieshouse/ecs-service-configs-dev)
- [ECS service config production repository](https://github.com/companieshouse/ecs-service-configs-production)
