
What you need to use DocBook
----------------------------

1. Download and unpack the following

   1. DocBook, Release 1.67.2, http://docbook.sourceforge.net/
   2. Saxon - XSLT processor, Release 6.5.3, http://saxon.sourceforge.net/
   3. Apache FOP, Release 0.20.5, http://xml.apache.org/fop/ 

2. Copy etc/build.properties to etc/custom-build.properties and edit custom-build.properties to suit you system's folders.

3. Run "ant dist", all project documentation should be generated now.

4. To edit DocBook XML documents I strongly recommend getting a XML and DTD aware Editor such as XMLMind XML Editor (<URL>). It takes time to get used to it, but you don't want to live without the assistance. 


Generation Schema
-----------------

  DocBook XML ---- ant html ----> HTML
    |
     ---- ant fo ----> FO ---- ant pdf ----> PDF