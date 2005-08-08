/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SLabel;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.net.URL;
import java.io.ByteArrayOutputStream;

/**
 * @author hengels
 * @version $Revision$
 */
public class RSSPortlet
        extends Bird
{
    private String feed;

    public RSSPortlet(String feed) {
        this.feed = feed;
        getContentPane().add(new SLabel("<html>" + getNews()));
    }

    String getNews() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            String ctx = getSession().getServletContext().getRealPath("") + System.getProperty("file.separator");
            Source xslSource = new StreamSource(new URL("file", "", ctx + "rss.xsl").openStream());
            Transformer transformer = tFactory.newTransformer(xslSource);

            Source xmlSource = new StreamSource(new URL(feed).openStream());
            transformer.transform(xmlSource, new StreamResult(out));
            return out.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
