/* $Id$ */
package org.wings.recorder;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

import java.io.IOException;
import java.util.*;

/**
 * @author hengels
 */
public abstract class Script {
    private HttpClient client;
    private float delay = 1.0f;
    private String url;

    public Script() {
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
    }

    public void setDelay(float delay) { this.delay = delay; }

    public void setTimeout(int timeout) { client.setConnectionTimeout(timeout); }

    public void setUrl(String url) {
        if (url.endsWith("/"))
            this.url = url;
        else
            this.url = url + "/";
    }

    public void delay(long time) {
        if (delay > 0.0) {
            try {
                Thread.sleep((long)(time * delay));
            }
            catch (InterruptedException e) { /* shit happens */ }
        }
    }

    public String send(GET request) throws IOException {
        GetMethod get = new GetMethod(url + request.getResource());
        addHeaders(request, get);

        NameValuePair[] nvps = eventsAsNameValuePairs(request);
        get.setQueryString(nvps);

        int result = client.executeMethod(get);
        return get.getResponseBodyAsString();
    }

    public String send(POST request) throws IOException {
        PostMethod post = new PostMethod(url + request.getResource());
        addHeaders(request, post);

        NameValuePair[] nvps = eventsAsNameValuePairs(request);
        post.setRequestBody(nvps);

        int result = client.executeMethod(post);
        return post.getResponseBodyAsString();
    }

    private void addHeaders(Request request, GetMethod post) {
        List headers = request.getHeaders();
        for (Iterator iterator = headers.iterator(); iterator.hasNext();) {
            Request.Header header = (Request.Header)iterator.next();
            post.addRequestHeader(header.getName(), header.getValue());
        }
    }

    private NameValuePair[] eventsAsNameValuePairs(Request request) {
        List events = request.getEvents();
        int size = 0;
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            Request.Event event = (Request.Event)iterator.next();
            size += event.getValues().length;
        }

        NameValuePair[] nvps = new NameValuePair[size];
        int index = 0;
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            Request.Event event = (Request.Event)iterator.next();
            String[] values = event.getValues();
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                nvps[index++] = new NameValuePair(event.getName(), value);
            }
        }
        return nvps;
    }

    public abstract void execute() throws Exception;

    public static void main(String[] args) throws Exception {
        String name = args[0];
        String url = args[1];
        float delay = args.length == 3 ? Float.parseFloat(args[2]) : 1.0f;

        Class clazz = Class.forName(name);
        Script script = (Script)clazz.newInstance();
        script.setUrl(url);
        script.setDelay(delay);
        script.execute();
    }
}
