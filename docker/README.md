To use with Docker, first make sure you have an AS4 key pair (`as4.pfx` in this example)
and have a server running on the intended domain name (`as4.example.com` here).


```sh
scp as4.pfx root@as4.example.com:/root/as4.pfx
ssh root@as4.example.com
mkdir /root/as4Keys
mv as4.pfx /root/as4Keys/
touch docker.env
echo DOMAIN=as4.example.com >> docker.env
echo EMAIL=letsencrypt@example.com >> docker.env
echo CONFIG_FILE=/as4Keys/phase4.properties >> docker.env
echo STOREPASS=secret >> docker.env
echo KEYPASS=secret >> docker.env
echo KEYNAME=as4 >> docker.env

git clone https://github.com/michielbdejong/phase4
cd phase4
git checkout docker
docker build -t phase4 -f docker/Dockerfile .
docker run -d --network="host" --name phase4 --env-file /root/docker.env -v /root/as4Keys:/as4Keys -v /root/tlsKeys:/etc/letsencrypt phase4
docker exec phase4 sh init.sh
docker restart phase4
