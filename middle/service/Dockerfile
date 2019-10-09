FROM 10.45.136.151:1180/middle/jdk:1.8.0_144

#工作目录
WORKDIR /usr/local/

VOLUME ["/data","/logs"]

#定义参数
ARG JAR_FILE

#拷贝本地文件到镜像中
COPY ${JAR_FILE} app.jar

# open port
EXPOSE 9002

# start app service when container running
CMD java -Xbootclasspath/a:/config -jar app.jar


