package de.materna.guava_demo;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

public class Application {

    public static void main(String[] args) {
        RangeSet range = TreeRangeSet.create();
        range.add(Range.closedOpen(2.,3.));
        System.out.println(range.contains(2.));
        System.out.println(range.contains(3.));
        System.out.println(range.rangeContaining(2.4));
    }
}
