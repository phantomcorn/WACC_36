FROM openjdk:11
RUN apt-get update
RUN apt-get install -y make
RUN apt-get install -y ruby
RUN apt-get install -y gcc-arm-linux-gnueabi
RUN apt-get install -y qemu
RUN apt-get install -y qemu-system-arm
COPY ./kotlinc /kotlin
ENV PATH=/kotlin/bin:$PATH
