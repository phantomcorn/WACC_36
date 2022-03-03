filename=$(basename $@)
filename2="${filename%.*}"
OUTPUT=$(./compile $@)
arm-linux-gnueabi-gcc -o $filename2 -mcpu=arm1176jzf-s -mtune=arm1176jzf-s $filename2.s
qemu-arm -L /usr/arm-linux-gnueabi/ $filename2
