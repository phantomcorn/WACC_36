FROM openjdk:11
RUN apt-get update
RUN apt-get install -y make
RUN apt-get install -y ruby
COPY ./kotlinc /kotlin
ENV PATH=/kotlin/bin:$PATH
