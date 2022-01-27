FROM openjdk:11

WORKDIR /home/wacc_36
COPY . /home/wacc_36

CMD make
