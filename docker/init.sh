#!/bin/bash

## Assumes $DOMAIN is set
## Assumes $EMAIL is set

certbot certonly --standalone -d $DOMAIN --non-interactive --agree-tos -m=$EMAIL
cp /etc/letsencrypt/live/$DOMAIN/privkey.pem /root/tomcat-key.pem
cp /etc/letsencrypt/live/$DOMAIN/cert.pem /root/tomcat-cert.pem
cp /etc/letsencrypt/live/$DOMAIN/chain.pem /root/tomcat-rsa-chain.pem
