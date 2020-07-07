package com.example.bitterreminder;

public class LocationItem {
    private int mImageResource;
    private String textTop;
    private String textBottom;

    public LocationItem(int image, String text1, String text2)
    {
        mImageResource = image;
        textTop = text1;
        textBottom = text2;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getTextTop()
    {
        return textTop;
    }

    public String getTextBottom()
    {
        return textBottom;
    }
}
