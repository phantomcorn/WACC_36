EXAMPLE_DIR="./wacc_examples"
VALID=$(find $EXAMPLE_DIR/valid -type f)

PASSED=0
FAILED=0
TOTAL=0

for f in $VALID
do
    OUTPUT=$(./compile $f)
    if [ $? != 0 ]
    then
        echo "Test $f failed: Should be valid, was $OUTPUT"
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
