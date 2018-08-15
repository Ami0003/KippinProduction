package com.kippin.utils.pinnedlistview;

/**
 * Created by gaganpreet.singh on 4/4/2016.
 */


public class EntryItem implements Item{

    public  String title;
    public  String subtitle;
    public    int itemPosition ;
    public    int subPosition ;
    public  int color;
    public String headerTitle;
    public boolean isListItem;

    public boolean isListItem() {
        return isListItem;
    }

    public void setIsListItem(boolean isListItem) {
        this.isListItem = isListItem;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public int getSubPosition() {
        return subPosition;
    }

    public void setSubPosition(int subPosition) {
        this.subPosition = subPosition;
    }

    public int getColor() {
        return color;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public EntryItem(String title, String subtitle, int itemPosition) {
        this.title = title;
        this.subtitle = subtitle;
        this.itemPosition = itemPosition;
    }
    public EntryItem(String title, String subtitle, int itemPosition,String headerTitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.itemPosition = itemPosition;
        this.headerTitle = headerTitle;
    }
    public EntryItem(String title, String subtitle, int itemPosition,String headerTitle,boolean isListItem) {
        this.title = title;
        this.subtitle = subtitle;
        this.itemPosition = itemPosition;
        this.headerTitle = headerTitle;
        this.isListItem = isListItem;
    }
    public EntryItem setColor(int color){
        this.color = color;
        return this;
    }

    public EntryItem(String title, String subtitle, int itemPosition , int subPosition,int color) {
        this.title = title;
        this.subtitle = subtitle;
        this.itemPosition = itemPosition;
        this.subPosition = subPosition;
        this.color = color;
    }

    public EntryItem(String title, String subtitle, int itemPosition , int subPosition) {
        this.title = title;
        this.subtitle = subtitle;
        this.itemPosition = itemPosition;
        this.subPosition = subPosition;
    }

    public EntryItem() {
    }

    @Override
    public boolean isSection() {
        return false;
    }

}