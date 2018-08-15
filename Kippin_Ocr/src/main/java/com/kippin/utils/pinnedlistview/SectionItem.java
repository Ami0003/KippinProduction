package com.kippin.utils.pinnedlistview;

/**
 * Created by gaganpreet.singh on 4/4/2016.
 */


public  class SectionItem implements Item{

    private final String title;
    private int color ;


    public SectionItem(String title ) {
        this.title = title;
    }

    public SectionItem(String title, int color) {
        this.title = title;
        this.color= color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle(){
        return title;
    }
    @Override
    public boolean isSection() {
        return true;
    }
}