
HOST := localhost
PORT := 6865
NCMDS := 1000

all: build run

build:
	daml build
	cp .daml/dist/quickstart-0.0.1.dar application/src/main/resources/
	sbt application/assembly

run:
	java -jar application/target/scala-2.12/application-assembly-0.0.1.jar $(HOST) $(PORT) $(NCMDS)
