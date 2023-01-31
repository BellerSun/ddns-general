FROM openjdk:8u181-jdk

ENV APP_NAME ddns-general

RUN \
    mkdir -p /root/$APP_NAME && \
    mkdir -p /root/$APP_NAME/h2 && \
    mkdir -p /root/$APP_NAME/logs && \
    mkdir -p /root/$APP_NAME/target && \
    echo "#!/bin/bash" > /root/$APP_NAME/start.sh && \
    echo "echo helloBellerSun start" >> /root/$APP_NAME/start.sh  && \
    echo "nohup java -jar ./target/ddns-general.jar" >> /root/$APP_NAME/start.sh  && \
    echo "echo helloBellerSun end" >> /root/$APP_NAME/start.sh  && \
    chmod +x /root/$APP_NAME/*.sh && \
    chmod o+wx /root/$APP_NAME/logs

COPY target/ddns-general.jar /root/$APP_NAME/target/ddns-general.jar

WORKDIR /root/$APP_NAME

ENTRYPOINT ["./start.sh"]
#ENTRYPOINT ["/bin/bash"]
