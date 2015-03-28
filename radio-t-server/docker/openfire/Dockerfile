FROM debian:7

ADD openfire_3.9.1_all.deb /tmp/openfire_3.9.1_all.deb
RUN apt-get -qq update \
    && apt-get -y -qq install default-jre-headless \
    && dpkg -i /tmp/openfire_3.9.1_all.deb \
    && rm /tmp/openfire_3.9.1_all.deb

ADD files/etc /etc
ADD files/embedded-db /var/lib/openfire/embedded-db

ADD start-openfire /start-openfire
RUN chmod 755 /start-openfire

EXPOSE 9090
EXPOSE 5222

CMD ["/start-openfire"]