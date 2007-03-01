This is a Rhino / Den Edwards based packer of javascript:

Source: 
  http://shaneosullivan.wordpress.com/2006/10/26/very-powerful-javascript-compressor-called-packer-announced/
  http://dojotoolkit.org/docs/compressor_system.html
  
  
Very powerful Javascript compressor called Packer announced

A new version of the Packer utility for compressing Javascript has been released by Dean Edwards and David McNab, and it’s amazing how small it can compress javascript code to. I took a compressed version of Dojo’s main file, dojo.js and compressed it further with Packer, and it went from 201k to 113k! This is a massive difference, especially given the fact that Dojo’s existing compressor is already very effective - the original code, before it was compressed with Dojo’s compressor, was 409k.

To use this utility, you need two things: Rhino and packer.js .

Rather than download Rhino separately, I used the version that comes with Dojo, though not in the bundled versions. You have to check out the Dojo source code from the Subversion repository (see here for instructions), and Rhino is in the /buildscripts/lib/custom_rhino.jar file. Alternatively, if you don’t want to download the Dojo source tree, you can just download that single jar file from here. Oh, and you’ll obviously have to have Java installed also, as Rhino is Java based.

Using the compressor is extremely simple. Open a command window and type:

java -jar /path/to/custom_rhino.jar packer.js inputfile.js outputfile.js

You’ll be in for quite a long wait, as the compression takes a loooonnnnggg time, but believe me it’s worth it. I wouldn’t recommend building this into your build process that you run on a regular basis, but before releasing production code it would be a good idea to run your files through Packer.

One caveat is that, due to the fact that the resulting file is absolutely bloody tiny, no attempt has been made at making it readable. All new line characters have been removed, all non-global variables have been renamed to someting unintelligible etc, so all you have to read is a single line of apparent gibberish. However, it works perfectly well and, as I previously mentioned, is bloody tiny! So, give it a go, you won’t be disappointed.

Here is a link to a discussion on the topic on the Dojo-interest forum.

