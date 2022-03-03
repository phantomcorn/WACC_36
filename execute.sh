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
    text=$(sed -n "${splitLines[0]},${splitLines[1]}p" $f5)

    OUTPUT=$(./compile $f)
    arm-linux-gnueabi-gcc -o $filename2 -mcpu=arm1176jzf-s -mtune=arm1176jzf-s $filename2.s
    qemu-arm -L /usr/arm-linux-gnueabi/ $filename2
    echo "" | ${EXAMPLE_DIR}/refEmulate  > emulateOutput
    outputLines=$(grep -n "=====*" emulateOutput | cut -d: -f 1)
    echo $outputLines
    outputSplitLines=($outputLines)
    outputText=$(sed -n "${outputSplitLines[0]},${outputSplitLines[1]}p" emulateOutput)
    
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
