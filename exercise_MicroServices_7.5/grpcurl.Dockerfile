FROM ubuntu:22.04

RUN apt-get update && \
    apt-get install -y wget && \
    wget https://github.com/fullstorydev/grpcurl/releases/download/v1.9.2/grpcurl_1.9.2_linux_x86_64.tar.gz && \
    tar -xvzf grpcurl_1.9.2_linux_x86_64.tar.gz && \
    mv grpcurl /usr/local/bin

CMD ["sleep", "infinity"]