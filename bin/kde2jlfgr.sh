#!/bin/bash

#
# Autor: Klas Kalass <klas@freiheit.com>
# Datum: 08/10/2003
#
# Importiert in wingS: Ole Langbehn <ole@freiheit.com>
# Datum: 05/12/2005
#

if [ $# -eq 0 ] ; then
	echo "Usage: `basename $0` <kdeiconthemedir>"
	exit 1
fi

THEMEDIR="$1"
THEME="`basename $1`"
THEMEJAR="`pwd`/${THEME}-lfgr.jar"

if [ -f "$THEMEJAR" ] ; then
    echo "Theme '$THEMEJAR' exists, exiting..."
    exit 1
fi

TMPDIR=`mktemp -d`
if [ $? -ne 0 ]; then
    echo "Failed to create temp directory, exiting"
    rm -rf $TMPDIR
    exit 1
fi

# create the directories
mkdir -p ${TMPDIR}/paletteButtonGraphics/
mkdir -p ${TMPDIR}/toolbarButtonGraphics/
mkdir -p ${TMPDIR}/toolbarButtonGraphics/text/
mkdir -p ${TMPDIR}/toolbarButtonGraphics/table/
mkdir -p ${TMPDIR}/toolbarButtonGraphics/navigation/
mkdir -p ${TMPDIR}/toolbarButtonGraphics/media/
mkdir -p ${TMPDIR}/toolbarButtonGraphics/general/
mkdir -p ${TMPDIR}/toolbarButtonGraphics/development/
mkdir -p ${TMPDIR}/treeIcons/

#
# 1: kde category
# 2: kde icon size
# 3: kde icon basename (without extension)
# 4: java icon size
# 5: java category (text, table, navigation, media, general, development)
# 6: java icon basename (without size or extension)
#
function convertIcon() (
    KDEICON="$THEMEDIR/${2}x${2}/${1}/${3}.png"
    if [ -f "$KDEICON" ] ; then
# not all icons in the respective kde icon dir have the said size, so we always resize

#    if [ $2 -eq $4 ] ; then
#	convert "$KDEICON" "${TMPDIR}/toolbarButtonGraphics/${5}/${6}${4}.gif"
#    identify -verbose "${TMPDIR}/toolbarButtonGraphics/${5}/${6}${4}.gif"
#    echo ${4}x${4}
#    else

# if output size is greater than input size, we don't resize but change canvas size
    if [ $4 -gt $2 ] ; then
    convert "$KDEICON" -bordercolor transparent -border ${4}x${4} -gravity center -crop ${4}x${4}+0+0 "${TMPDIR}/toolbarButtonGraphics/${5}/${6}${4}.gif"
#    echo "$KDEICON"
#    identify "$KDEICON"
#    echo ${6}${4}.gif
#    identify "${TMPDIR}/toolbarButtonGraphics/${5}/${6}${4}.gif"
    else
	convert "$KDEICON" -resize ${4}x${4} "${TMPDIR}/toolbarButtonGraphics/${5}/${6}${4}.gif"
#    echo "$KDEICON"
#    identify "$KDEICON"
#    echo ${6}${4}.gif
#    identify "${TMPDIR}/toolbarButtonGraphics/${5}/${6}${4}.gif"
    fi
    fi
#    fi
)

# 1: kde icon size
# 2: java icon size
function convertActionIcons() (
convertIcon actions $1 text_italic $2 text Italic
convertIcon actions $1 text_bold $2 text Bold
convertIcon actions $1 text $2 text Normal
convertIcon actions $1 text_under $2 text Underline
convertIcon actions $1 text_left $2 text AlignLeft
convertIcon actions $1 text_right $2 text AlignRight
convertIcon actions $1 text_block $2 text AlignJustify
convertIcon actions $1 text_center $2 text AlignCenter

#convertIcon actions $1 _ $2 table RowDelete
#convertIcon actions $1 _ $2 table ColumnInsertAfter
#convertIcon actions $1 _ $2 table ColumnInsertBefore
#convertIcon actions $1 _ $2 table RowInsertAfter
#convertIcon actions $1 _ $2 table RowInsertBefore
#convertIcon actions $1 _ $2 table ColumnDelete

convertIcon actions $1 back $2 navigation Back
convertIcon actions $1 down $2 navigation Down
convertIcon actions $1 forward $2 navigation Forward
convertIcon actions $1 up $2 navigation Up
convertIcon actions $1 gohome $2 navigation Home

#convertIcon actions $1 _ $2 media Movie
#convertIcon actions $1 _ $2 media Pause
#convertIcon actions $1 _ $2 media Play
#convertIcon actions $1 _ $2 media StepBack
#convertIcon actions $1 _ $2 media StepForward
#convertIcon actions $1 _ $2 media Stop
#convertIcon actions $1 _ $2 media Volume
#convertIcon actions $1 _ $2 media FastForward
#convertIcon actions $1 _ $2 media Rewind

#convertIcon actions $1 _ $2 general Add
#convertIcon actions $1 _ $2 general AlignBottom
#convertIcon actions $1 _ $2 general AlignCenter
#convertIcon actions $1 _ $2 general AlignJustifyHorizontal
#convertIcon actions $1 _ $2 general AlignJustifyVertical
#convertIcon actions $1 _ $2 general AlignLeft
#convertIcon actions $1 _ $2 general AlignRight
#convertIcon actions $1 _ $2 general AlignTop
convertIcon actions $1 contexthelp $2 general ContextualHelp
convertIcon actions $1 editcopy $2 general Copy
convertIcon actions $1 editcut $2 general Cut
convertIcon actions $1 editdelete $2 general Delete
convertIcon actions $1 find $2 general Find
convertIcon actions $1 help $2 general Help
convertIcon actions $1 fileopen $2 general Open
#convertIcon actions $1 _ $2 general PageSetup
convertIcon actions $1 editpaste $2 general Paste
convertIcon actions $1 configure $2 general Preferences
convertIcon actions $1 fileprint $2 general Print
convertIcon actions $1 redo $2 general Redo
convertIcon actions $1 filesave $2 general Save
convertIcon actions $1 undo $2 general Undo
convertIcon actions $1 viewmag+ $2 general ZoomIn
convertIcon actions $1 viewmag- $2 general ZoomOut
#convertIcon actions $1 _ $2 general About
convertIcon apps $1 keditbookmarks $2 general Bookmarks
convertIcon actions $1 mail_new $2 general ComposeMail
convertIcon actions $1 edit $2 general Edit
convertIcon actions $1 fileexport $2 general Export
convertIcon actions $1 find $2 general FindAgain
convertIcon actions $1 history $2 general History
convertIcon actions $1 fileimport $2 general Import
convertIcon actions $1 filenew $2 general New
#convertIcon actions $1 _ $2 general PrintPreview
#convertIcon actions $1 _ $2 general Properties
#convertIcon actions $1 _ $2 general Refresh
#convertIcon actions $1 _ $2 general Replace
convertIcon actions $1 save_all $2 general SaveAll
convertIcon actions $1 filesaveas $2 general SaveAs
convertIcon actions $1 find $2 general Search
convertIcon actions $1 mail_send $2 general SendMail
convertIcon actions $1 stop $2 general Stop
#convertIcon actions $1 _ $2 general TipOfTheDay
convertIcon actions $1 viewmag $2 general Zoom
convertIcon actions $1 info $2 general Information
convertIcon actions $1 remove $2 general Remove

#convertIcon actions $1 _ $2 development Jar
#convertIcon actions $1 _ $2 development JarAdd
#convertIcon actions $1 _ $2 development BeanAdd
#convertIcon actions $1 _ $2 development Bean
#convertIcon actions $1 _ $2 development WebComponentAdd
#convertIcon actions $1 _ $2 development J2EEApplicationClientAdd
#convertIcon actions $1 _ $2 development J2EEApplicationClient
#convertIcon actions $1 _ $2 development EnterpriseJavaBean
#convertIcon actions $1 _ $2 development J2EEApplication
#convertIcon actions $1 _ $2 development Host
#convertIcon actions $1 _ $2 development J2EEServer
#convertIcon actions $1 _ $2 development Applet
#convertIcon actions $1 _ $2 development War
#convertIcon actions $1 _ $2 development WarAdd
#convertIcon actions $1 _ $2 development WebComponent
#convertIcon actions $1 _ $2 development Application
#convertIcon actions $1 _ $2 development Server
#convertIcon actions $1 _ $2 development EnterpriseJavaBeanJar
#convertIcon actions $1 _ $2 development ApplicationDeploy

)

# copy the kde icons to the appropriate Java LNF name
# KDE has 22x22, we need to rescale those to 24
convertActionIcons 22 24
convertActionIcons 16 16


CURRENTDIR="`pwd`"
cd "$TMPDIR"

jar cf "${THEMEJAR}" paletteButtonGraphics toolbarButtonGraphics treeIcons

# clean up the temporary dir
cd "$CURRENTDIR"
rm -rf "$TMPDIR"
