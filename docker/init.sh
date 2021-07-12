#!/bin/bash

## Assumes $DOMAIN is set
## Assumes $EMAIL is set

certbot certonly --standalone -d $DOMAIN --non-interactive --agree-tos -m=$EMAIL
cp /etc/letsencrypt/live/$DOMAIN/privkey.pem /root/tomcat-key.pem
cp /etc/letsencrypt/live/$DOMAIN/cert.pem /root/tomcat-cert.pem
cp /etc/letsencrypt/live/$DOMAIN/chain.pem /root/tomcat-rsa-chain.pem

touch /root/phase4.properties
echo org.apache.wss4j.crypto.merlin.keystore.type="PKCS12" >> /root/phase4.properties
echo org.apache.wss4j.crypto.merlin.keystore.file="/as4Keys/as4.pks" >> /root/phase4.properties
echo org.apache.wss4j.crypto.merlin.keystore.password="$STOREPASS" >> /root/phase4.properties
echo org.apache.wss4j.crypto.merlin.keystore.alias="$KEYNAME" >> /root/phase4.properties
echo org.apache.wss4j.crypto.merlin.keystore.private.password="$KEYPASS" >> /root/phase4.properties
