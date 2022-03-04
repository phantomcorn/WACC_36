EXAMPLE_DIR="./wacc_examples"
SYNTAX=$(find $EXAMPLE_DIR/invalid/syntaxErr -type f)

PASSED=0
FAILED=0
TOTAL=0

for f in $SYNTAX
do
    OUTPUT=$(./compile $f 2> /dev/null)
    if [ $? != 100 ]
    then
        echo "Test $f failed: Should be Syntax Error, was $OUTPUT"
        ((FAILED++))
    else
        echo "Test $f passed"
        ((PASSED++))
    fi
    ((TOTAL++))
done

echo "PASSED: $PASSED, FAILED: $FAILED, TOTAL: $TOTAL"

if [ $FAILED == 0 ]
then
    exit 0
else
    exit 1
fi
