#!/bin/bash

# So there will not be warnings because of publishing
export IVY_CREDENTIALS_FILE=$HOME/.ivy2/.credentials

if [ ! -f "$IVY_CREDENTIALS_FILE" ];
then

cat > ${IVY_CREDENTIALS_FILE} << EOM
realm=Sonatype Nexus Repository Manager
host=oss.sonatype.org
user=FAKE_USER
password=FAKE_PASSWORD
EOM

fi