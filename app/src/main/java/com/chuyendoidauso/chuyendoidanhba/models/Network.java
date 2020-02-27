package com.chuyendoidauso.chuyendoidanhba.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "network")
public class Network implements Serializable {
    @Element(required = false)
    private boolean checked = true;
    @Element(required = false)
    private String code;
    @Element
    private String name;
    @ElementList(inline = true, name = "rule", required = false)
    private List<Rule> rules;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<Rule> getRules() {
        if (this.rules == null) {
            this.rules = new ArrayList();
        }
        return this.rules;
    }

    public void setRules(List<Rule> list) {
        this.rules = list;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean z) {
        this.checked = z;
    }
}
