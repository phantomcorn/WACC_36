EXAMPLE_DIR="./wacc_examples"
VALID=$(find $EXAMPLE_DIR/valid -type f)

PASSED=0
FAILED=0
TOTAL=0

for f in $VALID
do
    f2=${f#*/}
    f3=${f2#*/}
    f4=${f3#*/}
    f5=${f4/#/${EXAMPLE_DIR}/emulate/} 
    filename=$(basename $f)
    filename2="${filename%.*}"
    lines=$(grep -n "====*" $f5 | cut -d: -f 1)
    splitLines=($lines)
    line0=$((splitLines[0] + 1))
    text=$(cat $f5 | tail -n +$line0 | head -n $((splitLines[1] - line0)))

    output=$(./compile $f)
    if [ $? != 0 ]
    then 
        echo "Test $f failed: $output"
        ((FAILED++))
        continue
    fi
    output=$(arm-linux-gnueabi-gcc -o $filename2 -mcpu=arm1176jzf-s -mtune=arm1176jzf-s $filename2.s)
    if [ $? != 0 ]
    then 
        echo "Test $f failed: $output"
        ((FAILED++))
        continue
    fi
    outputText=$(qemu-arm -L /usr/arm-linux-gnueabi/ $filename2 < /dev/null)
    if [ $? != 0 ]
    then 
        echo "Test $f failed: $outputText"
        ((FAILED++))
        continue
    fi
    
    if [ "$text" != "$outputText" ]
    then
        echo "Test $f failed: Should be $text, was $outputText"
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
