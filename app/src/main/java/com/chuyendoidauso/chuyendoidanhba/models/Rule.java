package com.chuyendoidauso.chuyendoidanhba.models;

import java.io.Serializable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Rule implements Serializable {
    @Element(required = false)
    private String dest;
    @Element(required = false)
    private String src;

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String str) {
        this.src = str;
    }

    public String getDest() {
        return this.dest;
    }

    public void setDest(String str) {
        this.dest = str;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Rule)) {
            return super.equals(obj);
        }
        Rule rule = (Rule) obj;
        return  (!getSrc().equals(rule.getSrc()) || getDest().equals(rule.getDest()));
    }
}
