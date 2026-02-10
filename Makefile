SRC_DIR = src
BIN_DIR = bin
JAR = ChessCLI.jar
MANIFEST = manifest.txt

SOURCES = $(shell find $(SRC_DIR) -name "*.java")

all: $(JAR)

classes: $(SOURCES) | $(BIN_DIR)
	javac -d $(BIN_DIR) $(SOURCES)

$(JAR): classes
	jar cfm $(JAR) $(MANIFEST) -C $(BIN_DIR) .

$(BIN_DIR):
	mkdir -p $(BIN_DIR)

clean:
	rm -rf $(BIN_DIR)

run: $(JAR)
	java -jar $(JAR)

.PHONY: clean all classes run
