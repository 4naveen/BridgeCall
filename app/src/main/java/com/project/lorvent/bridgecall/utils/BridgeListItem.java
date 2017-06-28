package com.project.lorvent.bridgecall.utils;

/**
 * Created by lorvent on 11/14/2015.
 */
public class BridgeListItem {

    private int id;
    String name,number,before_dial,after_dial;

    public BridgeListItem(){}

    public BridgeListItem(String name, String num){
        this.name=name;
        this.number=num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBefore_dial() {
        return before_dial;
    }

    public void setBefore_dial(String before_dial) {
        this.before_dial = before_dial;
    }

    public String getAfter_dial() {
        return after_dial;
    }

    public void setAfter_dial(String after_dial) {
        this.after_dial = after_dial;
    }
}
