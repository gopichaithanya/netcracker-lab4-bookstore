package com.netcracker.helper;

import java.io.Serializable;
import java.util.ArrayList;

public class ThinEntityWrapper implements Serializable {
    /**
     * This field store entity bean's ID.
     */
    private String id;
    /**
     * Contains information related to the bean.
     */
    private String info;

    /**
     * Contains bean's additional information. For example book bean in this field can store
     * information about image (reference to image).
     */
    private String addInfo;

    //--------------------------------- Constructors --------------------------------------
    public ThinEntityWrapper() {}


    public ThinEntityWrapper(String id, String info) {
        this.id = id;
        this.info = info;
    }

    public ThinEntityWrapper(String id, String info, String addInfo) {
        this.id = id;
        this.info = info;
        this.addInfo = addInfo;
    }

    //-------------------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public String getAddInfo() {
        return addInfo;
    }
}



