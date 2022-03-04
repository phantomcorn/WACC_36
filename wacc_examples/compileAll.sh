VALID=$(find valid -type f)

for f in $VALID
do
    f2=${f#*/}
    f3=${f2/#/compile/} 
    echo "" | ./refCompile $f -a > $f3
done
