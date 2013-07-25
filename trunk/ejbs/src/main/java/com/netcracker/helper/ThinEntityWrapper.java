package com.netcracker.helper;

import java.io.Serializable;
import java.util.ArrayList;

public class ThinEntityWrapper implements Serializable {
    private String id;
    private String info;

    public ThinEntityWrapper() {}

    public ThinEntityWrapper(String id, String info) {
        this.id = id;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }


}
