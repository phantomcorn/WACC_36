# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler

# Useful locations

ANTLR_DIR	   := antlr_config
SOURCE_DIR	   := src
ANTLR_SOURCE_DIR   := $(SOURCE_DIR)/antlr
SYMBOLS_SOURCE_DIR := $(SOURCE_DIR)/symbols
EXPR_SOURCE_DIR := $(SOURCE_DIR)/expr
STAT_SOURCE_DIR := $(SOURCE_DIR)/stat
FUNC_SOURCE_DIR := $(SOURCE_DIR)/func
VISITOR_SOURCE_DIR := $(SOURCE_DIR)/visitor
CODEGEN_SOURCE_DIR := $(SOURCE_DIR)/codegen
INSTR_SOURCE_DIR := $(SOURCE_DIR)/instr
REGISTER_SOURCE_DIR := $(SOURCE_DIR)/register
SOURCES = $(SOURCE_DIR)/*.kt 
SOURCES += $(SYMBOLS_SOURCE_DIR)/*.kt 
SOURCES += $(EXPR_SOURCE_DIR)/*.kt 
SOURCES += $(STAT_SOURCE_DIR)/*.kt
SOURCES += $(FUNC_SOURCE_DIR)/*.kt
SOURCES += $(VISITOR_SOURCE_DIR)/*.kt
SOURCES += $(CODEGEN_SOURCE_DIR)/*.kt
SOURCES += $(INSTR_SOURCE_DIR)/*.kt
SOURCES += $(REGISTER_SOURCE_DIR)/*.kt
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
