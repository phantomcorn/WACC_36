EXAMPLE_DIR="./wacc_examples"
SEMANTIC=$(find $EXAMPLE_DIR/invalid/semanticErr -type f)

PASSED=0
FAILED=0
TOTAL=0

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

echo "PASSED: $PASSED, FAILED: $FAILED, TOTAL: $TOTAL"

if [ $FAILED == 0 ]
then
    exit 0
else
    exit -1
fi
