VALID=$(find valid -type f)

for f in $VALID
do
    f2=${f#*/}
    f3=${f2/#/emulate/} 
    echo "" | ./refCompile $f -x > $f3
done
