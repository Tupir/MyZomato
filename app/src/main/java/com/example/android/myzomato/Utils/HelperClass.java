package com.example.android.myzomato.Utils;

/**
 * Created by PepovPC on 11/6/2017.
 */

public class HelperClass {









    public static String sendCuisineName(int id){

        String cuisine;

        switch (id) {
            case 0:  cuisine = "Cafe";
                break;
            case 1:  cuisine = "Czech";
                break;
            case 2:  cuisine = "Slovak";
                break;
            case 3:  cuisine = "Grill";
                break;
            case 4:  cuisine = "Sushi";
                break;
            case 5:  cuisine = "Japanese";
                break;
            case 6:  cuisine = "Burger";
                break;
            case 7:  cuisine = "French";
                break;
            case 8:  cuisine = "Mexican";
                break;
            case 9: cuisine = "Indian";
                break;
            case 10: cuisine = "Pizza";
                break;
            default: cuisine = "Invalid month";
                break;
        }

        return cuisine;
    }



}
