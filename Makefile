# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler

# Useful locations

ANTLR_DIR	   := antlr_config
SOURCE_DIR	   := src
ANTLR_SOURCE_DIR   := $(SOURCE_DIR)/antlr
SYMBOLS_SOURCE_DIR := $(SOURCE_DIR)/symbols
EXPR_SOURCE_DIR := $(SOURCE_DIR)/expr
STAT_SOURCE_DIR := $(SOURCE_DIR)/stat
SOURCES = $(SOURCE_DIR)/*.kt $(SYMBOLS_SOURCE_DIR)/*.kt $(EXPR_SOURCE_DIR)/*.kt $(STAT_SOURCE_DIR)/*.kt
OUTPUT_DIR	   := bin

# Project tools

ANTLR	:= antlrBuild
MKDIR	:= mkdir -p
KOTLINC := kotlinc
JAVAC	:= javac
RM	:= rm -rf

# Configure project Java flags

FLAGS 	:= -d $(OUTPUT_DIR) -cp bin:lib/antlr-4.9.3-complete.jar
JFLAGS	:= -sourcepath $(SOURCE_DIR) $(FLAGS)


# The make rules:

# run the antlr build script then attempts to compile all .java files within src/antlr
all:
	cd $(ANTLR_DIR) && ./$(ANTLR) 
	$(MKDIR) $(OUTPUT_DIR)
	$(JAVAC) $(JFLAGS) $(ANTLR_SOURCE_DIR)/*.java
	$(KOTLINC) $(FLAGS) $(SOURCES)
# clean up all of the compiled files
clean:
	$(RM) $(OUTPUT_DIR) $(SOURCE_DIR)/antlr

.PHONY: all clean
