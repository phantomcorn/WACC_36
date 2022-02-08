EXAMPLE_DIR="./wacc_examples"
VALID=$(find $EXAMPLE_DIR/valid -type f)
SYNTAX=$(find $EXAMPLE_DIR/invalid/syntaxErr -type f)
SEMANTIC=$(find $EXAMPLE_DIR/invalid/semanticErr -type f)

PASSED=0
FAILED=0
TOTAL=0

for f in $SYNTAX
do
    OUTPUT=$(./compile $f 2> /dev/null)
    ((TOTAL++))
    if [ "${OUTPUT}" != "Syntax Error" ]
    then
        echo "Test $f failed: Should be Syntax Error, was $OUTPUT"
        ((FAILED++))
    else
        echo "Test $f passed"
        ((PASSED++))
    fi
done

for f in $SEMANTIC
do
    OUTPUT=$(./compile $f 2> /dev/null)
    ((TOTAL++))
    if [ "${OUTPUT}" != "Semantic Error" ]
    then
        echo "Test $f failed: Should be Semantic Error, was $OUTPUT"
        ((FAILED++))
   else
        echo "Test $f passed"
        ((PASSED++))
    fi
done

for f in $VALID
do
    OUTPUT=$(./compile $f 2> /dev/null)
    ((TOTAL++))
    if [ "${OUTPUT}" != "" ]
    then
        echo "Test $f failed: Should be valid, was $OUTPUT"
        ((FAILED++))
    else
        echo "Test $f passed"
        ((PASSED++))
    fi
done


echo "PASSED: $PASSED, FAILED: $FAILED, TOTAL: $TOTAL"

if [ $FAILED == 0 ]
then
    exit 0
else
    exit -1
fi
