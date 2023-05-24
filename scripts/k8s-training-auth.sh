#!/bin/bash
set -o errexit
set -o pipefail

CHALLENGE="${1}"
ACCOUNT="${2}"

ZONE="${3}"
PROJECT="cloudogu-trainings"
BUCKET="schulungsumgebung"

USAGE="usage: k8s-training-auth challenge accountname"

if [ "${CHALLENGE}" = "" ]; then
  echo "challenge is required"
  echo "${USAGE}"
  exit 1
fi

if [ "${ACCOUNT}" = "" ]; then
  echo "accountname is required"
  echo "${USAGE}"
  exit 2
fi

if [ "${ZONE}" = "" ]; then

  echo "Default zone europe-west3-a is used"
  ZONE="europe-west3-a"

fi

ACCOUNT_FILE=$(mktemp)
trap "{ rm -f ${ACCOUNT_FILE}; }" EXIT
curl --silent -o "${ACCOUNT_FILE}" "https://storage.googleapis.com/${BUCKET}/${CHALLENGE}/${ACCOUNT}.json"

gcloud auth activate-service-account --key-file="${ACCOUNT_FILE}"
gcloud container clusters get-credentials "${CHALLENGE}-${ACCOUNT}" --zone "${ZONE}" --project "cloudogu-trainings"
