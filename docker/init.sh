#!/bin/bash

## Assumes $DOMAIN is set
## Assumes $EMAIL is set

certbot certonly --standalone -d $DOMAIN --non-interactive --agree-tos -m=$EMAIL
cp /etc/$DOMAIN/privkey.pem /root/tomcat-key.pem
cp /etc/$DOMAIN/fullchain.pem /root/tomcat-cert.pem
cp /etc/$DOMAIN/chain.pem /root/tomcat-rsa-chain.pem
