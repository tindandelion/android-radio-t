#!/usr/bin/env bash
docker run -d \
  -v /vagrant/docker/local/etc/radio-t-server.conf:/etc/radio-t-server.conf \
  --link xmpp-server:xmpp-server \
  -p 8080:80 \
  --name radio-t-server \
  sergeymo/radio-t-server:1.0