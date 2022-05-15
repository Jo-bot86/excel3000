package de.materna.mini_excel;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        Excel3000 excel3000 = new Excel3000(new Parser());
        excel3000.setCell("A2", "=$B2 + 2");
        excel3000.setCell("B2", "=$C2");
        excel3000.setCell("C2", "2");
        Excel3000 excel3001 = excel3000.evaluate();
        System.out.println(excel3001.getCell("A2"));
        System.out.println(excel3001.getCell("B2"));
        System.out.println(excel3001.getCell("C2"));

        try {
            excel3001.exportFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
