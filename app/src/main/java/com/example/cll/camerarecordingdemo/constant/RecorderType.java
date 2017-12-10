package com.example.cll.camerarecordingdemo.constant;

/**
 * Created by cll on 2017/12/10.
 */

public enum RecorderType {

    STATUS(0,"status"),
    PREVIEW(1, "yes"),
    NO_PREVIEW(2, "none");


    private int id;
    private String value;
    RecorderType (final int id, final String value){
        this.id = id;
        this.value = value;
    }
    public int getId(){
        return id;
    }
    public String getValue(){
        return value;
    }

    public static RecorderType get(final int id){
        for (RecorderType type : values()){
            if (type.getId() == id){
                return type;
            }
        }
        return null;
    }
}
