#!/bin/bash
#
# Start script for company-accounts-web


PORT=8080

exec java -jar -Dserver.port="${PORT}" -XX:MaxRAMPercentage=80 "company-accounts.web.ch.gov.uk.jar"
