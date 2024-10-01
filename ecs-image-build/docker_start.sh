#!/bin/bash
#
# Start script for company-accounts-web


PORT=8080

exec java -jar -Dserver.port="${PORT}" "company-accounts.web.ch.gov.uk.jar"
