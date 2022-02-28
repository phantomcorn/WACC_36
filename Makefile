# Useful locations

ANTLR_DIR	   := antlr_config
SOURCE_DIR	   := src
ANTLR_SOURCE_DIR   := $(SOURCE_DIR)/antlr
PARSE_SOURCE_DIR := $(SOURCE_DIR)/parse
SYMBOLS_SOURCE_DIR := $(PARSE_SOURCE_DIR)/symbols
EXPR_SOURCE_DIR := $(PARSE_SOURCE_DIR)/expr
STAT_SOURCE_DIR := $(PARSE_SOURCE_DIR)/stat
FUNC_SOURCE_DIR := $(PARSE_SOURCE_DIR)/func
SEMANTICS_SOURCE_DIR := $(PARSE_SOURCE_DIR)/semantics
CODEGEN_SOURCE_DIR := $(SOURCE_DIR)/codegen
INSTR_SOURCE_DIR := $(CODEGEN_SOURCE_DIR)/instr
OPERAND2_SOURCE_DIR := $(INSTR_SOURCE_DIR)/operand2
REGISTER_SOURCE_DIR := $(INSTR_SOURCE_DIR)/register
ARM_SOURCE_DIR := $(INSTR_SOURCE_DIR)/arm
UTILS_SOURCE_DIR := $(CODEGEN_SOURCE_DIR)/utils
LOADABLE_SOURCE_DIR := $(INSTR_SOURCE_DIR)/loadable
SOURCES = $(SOURCE_DIR)/*.kt 
SOURCES += $(SYMBOLS_SOURCE_DIR)/*.kt 
SOURCES += $(EXPR_SOURCE_DIR)/*.kt 
SOURCES += $(STAT_SOURCE_DIR)/*.kt
SOURCES += $(FUNC_SOURCE_DIR)/*.kt
SOURCES += $(SEMANTICS_SOURCE_DIR)/*.kt
SOURCES += $(CODEGEN_SOURCE_DIR)/*.kt
SOURCES += $(INSTR_SOURCE_DIR)/*.kt
SOURCES += $(OPERAND2_SOURCE_DIR)/*.kt
SOURCES += $(REGISTER_SOURCE_DIR)/*.kt
SOURCES += $(ARM_SOURCE_DIR)/*.kt
SOURCES += $(UTILS_SOURCE_DIR)/*.kt
SOURCES += $(LOADABLE_SOURCE_DIR)/*.kt
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
