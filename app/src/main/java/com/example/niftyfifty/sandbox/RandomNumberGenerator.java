package com.example.niftyfifty.sandbox;

import java.util.ArrayList;
import java.util.Random;

public class RandomNumberGenerator {

    public static void main( String args[] ) {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("Java");
        list1.add("JavaFx");
        list1.add("WebGL");
        list1.add("OpenCV");
        ArrayList<String> list2 = new ArrayList<String>();
        list2.add("JavaFx");
        list2.add("Java");
        list2.add("WebGL");
        list2.add("OpenCV");
        System.out.println(list2);
        System.out.println(list1.equals(list2));
    }
}
