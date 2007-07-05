/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.UploadFilterManager;
import org.wings.util.SStringBuilder;
import org.wings.util.LocaleCharSet;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * A utility class to handle <tt>multipart/form-data</tt> requests,
 * the kind of requests that support file uploads.  This class can
 * receive arbitrarily large files (up to an artificial limit you can set),
 * and fairly efficiently too. And it knows and works around several browser
 * bugs that don't know how to upload files correctly.
 * <p/>
 * A client can upload files using an HTML form with the following structure.
 * Note that not all browsers support file uploads.
 * <blockquote><pre>
 * &lt;FORM ACTION="/servlet/Handler" METHOD=POST
 *          ENCTYPE="multipart/form-data"&gt;
 * What is your name? &lt;INPUT TYPE=TEXT NAME=submitter&gt; &lt;BR&gt;
 * Which file to upload? &lt;INPUT TYPE=FILE NAME=file&gt; &lt;BR&gt;
 * &lt;INPUT TYPE=SUBMIT&GT;
 * &lt;/FORM&gt;
 * </pre></blockquote>
 * <p/>
 * The full file upload specification is contained in experimental RFC 1867,
 * available at <a href="http://ds.internic.net/rfc/rfc1867.txt">
 * http://ds.internic.net/rfc/rfc1867.txt</a>.
 *
 * @author Holger Engels
 */
public class MultipartRequest
        extends HttpServletRequestWrapper {
    private final transient static Log log = LogFactory.getLog(MultipartRequest.class);

    private static final int DEFAULT_MAX_POST_SIZE = 1024 * 1024;  // 1 Meg

    private int maxSize;
    private boolean urlencodedRequest;

    private final HashMap parameters = new HashMap();  // name - value
    private final HashMap files = new HashMap();       // name - UploadedFile
    private HashMap map;

    /**
     * @param request       the servlet request
     * @throws IOException if the uploaded content is larger than 1 Megabyte
     *                     or there's a problem reading or parsing the request
     */
    public MultipartRequest(HttpServletRequest request) throws IOException {
        this(request, DEFAULT_MAX_POST_SIZE);
    }

    /**
     * @param request     the servlet request
     * @param maxPostSize the maximum size of the POST content
     * @throws IOException if the uploaded content is larger than
     *                     <tt>maxPostSize</tt> or there's a problem reading or parsing the request
     */
    public MultipartRequest(HttpServletRequest request,
                            int maxPostSize) throws IOException {
        super(request);

        if (request == null)
            throw new IllegalArgumentException("request cannot be null");
        if (maxPostSize <= 0)
            throw new IllegalArgumentException("maxPostSize must be positive");

        maxSize = maxPostSize;

        processRequest(request);
    }

    /**
     * Returns the names of all the parameters as an Enumeration of
     * Strings.  It returns an empty Enumeration if there are no parameters.
     *
     * @return the names of all the parameters as an Enumeration of Strings
     */
    public Enumeration getParameterNames() {
        if (urlencodedRequest) return super.getParameterNames();

        final Iterator iter = parameters.keySet().iterator();
        return new Enumeration() {
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            public Object nextElement() {
                return iter.next();
            }

        };
    }

    /**
     * Returns the names of all the uploaded files as an Enumeration of
     * Strings.  It returns an empty Enumeration if there are no uploaded
     * files.  Each file name is the name specified by the form, not by
     * the user.
     *
     * @return the names of all the uploaded files as an Enumeration of Strings
     */
    public Iterator getFileNames() {
        return files.keySet().iterator();
    }

    public String[] getParameterValues(String name) {
        if (urlencodedRequest)
            return super.getParameterValues(name);
        ArrayList v = (ArrayList) parameters.get(name);
        if (v == null) return null;
        String result[] = new String[v.size()];
        return (String[]) v.toArray(result);
    }

    public Map getParameterMap() {
        if (urlencodedRequest)
            return super.getParameterMap();
        if (map == null) {
            map = new HashMap();
            for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                List list = (List) entry.getValue();
                String[] values = (String[]) list.toArray(new String[list.size()]);
                map.put(entry.getKey(), values);
            }
        }
        return map;
    }

    /**
     * Returns the filename of the specified file, or null if the
     * file was not included in the upload. The filename is the name
     * specified by the user. It is not the name under which the file is
     * actually saved.
     *
     * @param name the file name
     * @return the filesystem name of the file
     */
    public String getFileName(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getFileName();  // may be null
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the fileid of the specified file, or null if the
     * file was not included in the upload. The fileid is the name
     * under which the file is saved in the filesystem.
     *
     * @param name the file name
     * @return the filesystem name of the file
     */
    public String getFileId(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getId();  // may be null
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the content type of the specified file (as supplied by the
     * client browser), or null if the file was not included in the upload.
     *
     * @param name the file name
     * @return the content type of the file
     */
    public String getContentType(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getContentType();  // may be null
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a File object for the specified file saved on the server's
     * filesystem, or null if the file was not included in the upload.
     *
     * @param name the file name
     * @return a File object for the named file
     */
    public File getFile(String name) {
        try {
            UploadedFile file = (UploadedFile) files.get(name);
            return file.getFile();  // may be null
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Indicates if this class was successfully able to parse request as multipart request.
     */
    public final boolean isMultipart() {
        return !urlencodedRequest;
    }

    /**
     * Store exception as request parameter.
     */
    protected void setException(String param, Exception ex) {
        parameters.clear();
        files.clear();

        putParameter(param, "exception");
        putParameter(param, ex.getMessage());
    }

    /**
     * Parses passed request and stores contained parameters.
     *
     * @throws IOException On unrecoverable parsing bugs due to old Tomcat version.
     */
    protected void processRequest(HttpServletRequest req)
            throws IOException {
        String type = req.getContentType();
        if (type == null || !type.toLowerCase().startsWith("multipart/form-data")) {
            urlencodedRequest = true;
            return;
        }
        urlencodedRequest = false;

        for (Iterator iterator = req.getParameterMap().entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            parameters.put(entry.getKey(), new ArrayList(Arrays.asList((String[]) entry.getValue())));
        }

        String boundaryToken = extractBoundaryToken(type);
        if (boundaryToken == null) {
            /*
             * this could happen due to a bug in Tomcat 3.2.2 in combination
             * with Opera.
             * Opera sends the boundary on a separate line, which is perfectly
             * correct regarding the way header may be constructed 
             * (multiline headers). Alas, Tomcat fails to read the header in 
             * the content type line and thus we cannot read it.. haven't 
             * checked later versions of Tomcat, but upgrading is
             * definitly needed, since we cannot do anything about it here.
             * (JServ works fine, BTW.) (Henner)
             */
            throw new IOException("Separation boundary was not specified (BUG in Tomcat 3.* with Opera?)");
        }

        MultipartInputStream mimeStream = null;

        ByteArrayOutputStream headerByteArray;
        SStringBuilder content = new SStringBuilder();
        HashMap headers = null;
        int currentByte = 0;
        int currentPos = 0;
        int currentTransformByte = 0;
        String currentParam = null;
        File uploadFile = null;
        OutputStream fileStream = null;
        boolean done;
        int last = -1;

        try {
            mimeStream = new MultipartInputStream(req.getInputStream(), req.getContentLength(), maxSize);
            while (currentByte != -1) {
                // Read MIME part header line
                done = false;
                headerByteArray = new ByteArrayOutputStream();
                while ((currentByte = mimeStream.read()) != -1 && !done) {
                    headerByteArray.write(currentByte);
                    done = (last == '\n' && currentByte == '\r');
                    last = currentByte;
                }
                if (currentByte == -1)
                    break;

                headers = parseHeader(headerByteArray.toString(req.getCharacterEncoding()));
                headerByteArray.reset();
                currentParam = (String) headers.get("name");

                if (headers.size() == 1) {                // .. it's not a file
                    byte[] bytes = new byte[req.getContentLength()];
                    currentPos = 0;
                    while ((currentByte = mimeStream.read()) != -1) {
                        bytes[currentPos] = (byte) currentByte;
                        currentPos++;
                        if (currentPos >= boundaryToken.length()) {
                            int i;
                            for (i = 0; i < boundaryToken.length(); i++) {
                                if (boundaryToken.charAt(boundaryToken.length() - i - 1) != bytes[currentPos - i - 1]) {
                                    i = 0;
                                    break;
                                }
                            }
                            if (i == boundaryToken.length()) {  // end of part ..
                                ByteArrayInputStream bais = new ByteArrayInputStream(bytes, 0, currentPos - boundaryToken.length() - 4);
                                InputStreamReader ir;
                                if (req.getCharacterEncoding() != null)
                                // It's common behaviour of browsers to encode their form input in the character
                                // encoding of the page, though they don't declare the used characterset explicetly
                                // for backward compatibility.
                                    ir = new InputStreamReader(bais, req.getCharacterEncoding());
                                else
                                    ir = new InputStreamReader(bais);
                                content.setLength(0);
                                while ((currentTransformByte = ir.read()) != -1) {
                                    content.append((char) currentTransformByte);
                                }

                                putParameter(currentParam, content.toString());
                                break;
                            }
                        }
                    }
                } else {                                      // .. it's a file
                    String filename = (String) headers.get("filename");
                    if (filename != null && filename.length() != 0) {
                        // The filename may contain a full path.  Cut to just the filename.
                        int slash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
                        if (slash > -1) {
                            filename = filename.substring(slash + 1);
                        }
                        String name = (String) headers.get("name");

                        String contentType = (String) headers.get("content-type");
                        try {
                            uploadFile = File.createTempFile("wings_uploaded",
                                    "tmp");
                        } catch (IOException e) {
                            log.error("couldn't create temp file in '"
                                    + System.getProperty("java.io.tmpdir")
                                    + "' (CATALINA_TMPDIR set correctly?)",
                                    e);
                            throw e;
                        }

                        UploadedFile upload = new UploadedFile(filename,
                                contentType,
                                uploadFile);
                        fileStream = new FileOutputStream(uploadFile);

                        fileStream = UploadFilterManager.createFilterInstance(name, fileStream);

                        AccessibleByteArrayOutputStream byteArray = new AccessibleByteArrayOutputStream();
                        byte[] bytes = null;

                        int blength = boundaryToken.length();
                        int i;
                        while ((currentByte = mimeStream.read()) != -1) {
                            byteArray.write(currentByte);
                            for (i = 0; i < blength; i++) {
                                if (boundaryToken.charAt(blength - i - 1) != byteArray.charAt(-i - 1)) {
                                    i = 0;
                                    if (byteArray.size() > 512 + blength + 2)
                                        byteArray.writeTo(fileStream, 512);
                                    break;
                                }
                            }
                            if (i == blength)   // end of part ..
                                break;
                        }
                        bytes = byteArray.toByteArray();
                        fileStream.write(bytes, 0, bytes.length - blength - 4);
                        fileStream.close();

                        files.put(name, upload);
                        putParameter(name, upload.toString());
                    } else {                  // workaround for some netscape bug
                        int i;
                        int blength = boundaryToken.length();
                        while ((currentByte = mimeStream.read()) != -1) {
                            content.append((char) currentByte);
                            if (content.length() >= blength) {
                                for (i = 0; i < blength; i++) {
                                    if (boundaryToken.charAt(blength - i - 1) != content.charAt(content.length() - i - 1)) {
                                        i = 0;
                                        break;
                                    }
                                }
                                if (i == blength)
                                    break;
                            }
                        }
                    }
                }

                currentByte = mimeStream.read();
                if (currentByte == '\r' && mimeStream.read() != '\n')
                    log.error("No line return char? " + currentByte);
                if (currentByte == '-' && mimeStream.read() != '-')
                    log.error("?? No clue " + currentByte);
            }
        } catch (IOException ex) {
            // cleanup and store the exception for notification of SFileChooser
            log.warn("upload", ex);
            if (uploadFile != null) uploadFile.delete();
            setException(currentParam, ex);
        } finally {
            try { fileStream.close(); } catch (Exception ign) {}
            try { mimeStream.close(); } catch (Exception ign) {}
        }
    }

    private static class AccessibleByteArrayOutputStream extends ByteArrayOutputStream {
        public byte charAt(int index) {
            if (count + index < 0) {
                log.warn("count: " + count + ", index: " + index + ", buffer: " + new String(buf));
                return -1;
            }
            if (index < 0)
                return buf[count + index];
            if (index < count)
                return buf[index];
            return -1;
        }

        public byte[] getBuffer() {
            return buf;
        }

        public void writeTo(OutputStream out, int num)
                throws IOException {
            out.write(buf, 0, num);
            System.arraycopy(buf, num, buf, 0, count - num);
            count = count - num;
        }
    }

    private HashMap parseHeader(String header) {
        int lastHeader = -1;
        String[] headerLines;
        HashMap nameValuePairs = new HashMap();

        StringTokenizer stLines = new StringTokenizer(header, "\r\n", false);
        headerLines = new String[stLines.countTokens()];

        // Get all the header lines
        while (stLines.hasMoreTokens()) {
            String hLine = stLines.nextToken();
            if (hLine.length() == 0) continue;
            /* if the first character is a space, then
             * this line is a header continuation.
             * (opera sends multiline headers..)
             */
            if (lastHeader >= 0 && Character.isWhitespace(hLine.charAt(0)))
                headerLines[lastHeader] += hLine;
            else
                headerLines[++lastHeader] = hLine;
        }

        for (int i = 0; i <= lastHeader; ++i) {
            String currentHeader = headerLines[i];
            if (currentHeader.startsWith("Content-Type")) {
                String contentType = currentHeader
                        .substring(currentHeader.indexOf(':') + 1);
                int semiColonPos = contentType.indexOf(';');
                if (semiColonPos != -1)
                    contentType = contentType.substring(0, semiColonPos);
                nameValuePairs.put("content-type", contentType.trim());
                continue;
            }

            if (!currentHeader.startsWith("Content-Disposition"))
                continue;

            StringTokenizer stTokens = new StringTokenizer(currentHeader, ";", false);
            
            // Get all the tokens from each line
            if (stTokens.countTokens() > 1) {
                stTokens.nextToken();    // Skip fist Token Content-Disposition: form-data
                StringTokenizer stnameValue = new StringTokenizer(stTokens.nextToken(), "=", false);
                nameValuePairs.put(stnameValue.nextToken().trim(), trim(stnameValue.nextToken(), "\""));

                // This is a file
                if (stTokens.hasMoreTokens()) {
                    stnameValue = new StringTokenizer(stTokens.nextToken(), "=", false);

                    String formType = stnameValue.nextToken().trim();  // String Object default function
                    String filePath = trim(stnameValue.nextToken(), "\""); // Our own trim function.
                    // If is a DOS file get rid of drive letter and colon  "e:"
                    if (filePath.indexOf(":") != -1)
                        filePath = filePath.substring((filePath.indexOf(":") + 1));

                    // get rid of PATH
                    filePath = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
                    nameValuePairs.put(formType, filePath);
                }
            }
        }
        return nameValuePairs;
    }

    /**
     * This method gets the substring enclosed in trimChar  ; "string" returns string
     */
    private String trim(String source, String trimChar) {
        String target = "";
        //Blank space from both sides
        source = source.trim();

        // Make sure a substring is enclosed between specified characters
        if (source.indexOf(trimChar) != -1 && (source.lastIndexOf(trimChar) >= (source.indexOf(trimChar) + 1)))
        // Remove double character from both sides
            target = source.substring(source.indexOf(trimChar) + 1, source.lastIndexOf(trimChar));

        return target;
    }

    private static class MultipartInputStream extends InputStream {
        ServletInputStream istream = null;
        int len, pos, maxLength;

        public MultipartInputStream(ServletInputStream istream, int len, int maxLength) {
            this.istream = istream;
            this.len = len;
            this.pos = 0;
            this.maxLength = maxLength;
        }

        /**
         * @return bytes available in stream.
         */
        public int available() throws IOException {
            return len - pos - 1;
        }

        /**
         * @return Next byte in Request.
         * @throws IOException
         */
        public int read() throws IOException {
            if (pos >= maxLength)
                throw new IOException("Size (" + len + ") exceeds maxlength " + maxLength);

            if (pos >= len)
                return -1;
            pos++;

            return istream.read();
        }

        public int read(byte b[]) throws IOException {
            return read(b, 0, b.length);
        }

        public int read(byte b[], int off, int num) throws IOException {
            if (off > 0)
                istream.skip(off);

            if (pos >= len)
                return -1;

            if (num > len - pos)
                num = len - pos;

            num = istream.read(b, 0, num);
            pos += num;

            if (pos >= maxLength)
                throw new IOException("Size (" + len + ") exceeds maxlength " + maxLength);

            return num;
        }

        public long skip(long num) throws IOException {
            if (pos >= len)
                return -1;

            if (num > len - pos)
                num = len - pos;

            num = istream.skip(num);
            pos += num;

            if (pos >= maxLength)
                throw new IOException("Size (" + len + ") exceeds maxlength " + maxLength);

            return num;
        }

        public void close() throws IOException {
            //Ignore closing of the input stream ..
        }
    }

    /**
     * Stores a parameter identified in this request.
     */
    protected void putParameter(String name, String value) {
        ArrayList v = (ArrayList) parameters.get(name);
        // there is no Parameter yet; create one
        if (v == null) {
            v = new ArrayList(2);
            parameters.put(name, v);
        }
        v.add(value);
    }

    /**
     * Extracts and returns the boundary token from a line.
     */
    private String extractBoundaryToken(String line) {
        int index = line.indexOf("boundary=");
        if (index == -1) {
            return null;
        }
        String boundary = line.substring(index + 9);  // 9 for "boundary="
        // The real boundary is always preceeded by an extra "--"
        //boundary = "--" + boundary;

        return boundary;
    }

    /**
     * Extracts and returns the content type from a line, or null if the line was empty.
     *
     * @throws IOException if the line is malformatted.
     */
    private String extractContentType(String line) throws IOException {
        String contentType = null;

        // Convert the line to a lowercase string
        String origline = line;
        line = origline.toLowerCase();

        // Get the content type, if any
        if (line.startsWith("content-type")) {
            int start = line.indexOf(" ");
            if (start == -1) {
                throw new IOException("Content type corrupt: " + origline);
            }
            contentType = line.substring(start + 1);
        } else if (line.length() != 0) {  // no content type, so should be empty
            throw new IOException("Malformed line after disposition: " + origline);
        }

        return contentType;
    }

    private static long uniqueId = 0;

    private static final synchronized String uniqueId() {
        uniqueId++;
        return System.currentTimeMillis() + "." + uniqueId;
    }


    /**
     * A class to hold information about an uploaded file.
     */
    class UploadedFile {
        private String fileName;
        private String type;
        private File uploadedFile;

        UploadedFile(String fileName, String type, File f) {
            this.uploadedFile = f;
            this.fileName = fileName;
            this.type = type;
        }

        /**
         * @return Path of uploaded file
         */
        public String getDir() {
            if (uploadedFile != null)
                return uploadedFile.getParentFile().getPath();
            else
                return null;
        }

        /**
         * @return Filename passed by browser
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * @return MIME type passed by browser
         */
        public String getContentType() {
            return type;
        }

        /**
         * @return Uploaded file
         */
        public File getFile() {
            return uploadedFile;
        }

        /**
         * @return Uploaded file name
         */
        public String getId() {
            if (uploadedFile != null)
                return uploadedFile.getName();
            else
                return null;
        }


        /**
         * create a URL-encoded form of this uploaded file, that contains
         * all parameters important for this file. The parameters returned
         * are 'dir', 'name', 'type' and 'id'
         * <ul>
         * <li>'dir' contains the directory in the filesystem, the file
         * has been stored into.</li>
         * <li>'name' contains the filename as provided by the user</li>
         * <li>'type' contains the mime-type of this file.</li>
         * <li>'id' contains the internal name of the file in the
         * filesystem.</li>
         * </ul>
         */
        public String toString() {
            String encoding = getRequest().getCharacterEncoding() != null ? getRequest().getCharacterEncoding() : LocaleCharSet.DEFAULT_ENCODING;

            try {
                SStringBuilder buffer = new SStringBuilder();
                buffer.append("dir=");
                buffer.append(URLEncoder.encode(getDir(), encoding));
                if (getFileName() != null) {
                    buffer.append("&name=");
                    buffer.append(URLEncoder.encode(getFileName(), encoding));
                }
                if (getContentType() != null) {
                    buffer.append("&type=");
                    buffer.append(URLEncoder.encode(getContentType(), encoding));
                }
                buffer.append("&id=");
                buffer.append(URLEncoder.encode(getId(), encoding));

                return buffer.toString();
            } catch (UnsupportedEncodingException e) {
                log.error(getClass().getName(), e);
                return null;
            }
        }
    }
}



