#!/bin/bash

CHARSETPATH="/opt/mozilla/res/unixcharset.properties"
OUTPATH="../src/org/wings/util/charset.properties"

echo "Using $CHARSETPATH"

grep -v "^#" $CHARSETPATH | \
awk 'BEGIN{FS="="}
/locale.all.[a-zA-Z_]+=/{
    print substr($0, 12)
}' > $OUTPATH

echo "Wrote to $OUTPATH"
