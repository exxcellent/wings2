package org.wings.plaf.css;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.wings.plaf.Update;

public class UpdateHandler implements Update.Handler {

    protected String name;
    protected List parameters;

    public UpdateHandler(String name) {
        if (name == null)
            throw new IllegalArgumentException("Handler name must not be null!");

        this.name = name;
        parameters = new ArrayList(5);
    }

    public String getName() {
        return "wingS.update." + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addParameter(Object o) {
        parameters.add(encode(o));
    }

    public Iterator getParameters() {
        return parameters.iterator();
    }

    public Object getParameter(int index) {
        return parameters.get(index);
    }

    public Object removeParameter(int index) {
        return parameters.remove(index);
    }

    public void addParameter(int index, Object o) {
        parameters.add(index, encode(o));
    }

    public Object setParameter(int index, Object o) {
        return parameters.set(index, encode(o));
    }

    private String encode(Object o) {
        StringBuffer sb = new StringBuffer();
        if (o instanceof String) {
            sb.append("\"");
            sb.append(escape((String) o));
            sb.append("\"");
        } else
            sb.append(String.valueOf(o));
        return sb.toString();
    }

    private String escape(String s) {
        if (s == null)
            return null;

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                if (ch >= '\u0000' && ch <= '\u001F') {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int j = 0; j < 4 - ss.length(); ++j) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    public Object listToJsArray(List list) {
        return new JSArray(list);
    }

    public Object mapToJsObject(Map map) {
        return new JSObject(map);
    }

    public String toString() {
        return getClass().getName() + "[" + name + "]";
    }

    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;

        UpdateHandler handler = (UpdateHandler) object;

        if (!this.getName().equals(handler.getName()))
            return false;
        if (!Arrays.equals(parameters.toArray(), handler.parameters.toArray()))
            return false;

        return true;
    }

    private class JSArray {

        private List list;

        public JSArray(List list) {
            this.list = list;
        }

        public String toString() {
            Iterator i = list.iterator();
            StringBuffer sb = new StringBuffer("[");
            if (i.hasNext())
                sb.append(encode(i.next()));
            while (i.hasNext())
                sb.append(",").append(encode(i.next()));
            return sb.append("]").toString();
        }

        public boolean equals(Object object) {
            return list.equals(object);
        }
    }

    private class JSObject {

        private Map map;

        public JSObject(Map map) {
            this.map = map;
        }

        public String toString() {
            Iterator i = map.entrySet().iterator();
            StringBuffer sb = new StringBuffer("{");
            if (i.hasNext())
                sb.append(toString((Map.Entry) i.next()));
            while (i.hasNext()) {
                sb.append(",").append(toString((Map.Entry) i.next()));
            }
            return sb.append("}").toString();
        }

        private String toString(Map.Entry entry) {
            StringBuffer sb = new StringBuffer();
            sb.append("\"");
            sb.append(escape(entry.getKey().toString()));
            sb.append("\":");
            sb.append(encode(entry.getValue()));
            return sb.toString();
        }

        public boolean equals(Object object) {
            return map.equals(object);
        }
    }

}