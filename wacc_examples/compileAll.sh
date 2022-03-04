VALID=$(find compile -type f)

for f in $VALID
do
    echo $f
    f2=${f#*/}
    f3=${f2/#/emulate/} 

    filename=$(basename $f)
    filename2="${filename%.*}"
    lines=$(grep -n "===========================================================" $f | cut -d: -f 1)
    splitLines=($lines)
    line0=$((splitLines[0] + 1))
    cat $f | tail -n +$line0 | head -n $((splitLines[1] - line0)) > temp
    sed -e 's/^\w*\ *//' temp > asm.s
    arm-linux-gnueabi-gcc -o $filename2 -mcpu=arm1176jzf-s -mtune=arm1176jzf-s asm.s
    qemu-arm -L /usr/arm-linux-gnueabi/ $filename2 < /dev/null > $f3
    echo $? > $f3.code
done
