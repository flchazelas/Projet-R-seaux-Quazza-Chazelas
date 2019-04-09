package com.example.izycode.izyCodePackage;

import java.util.*;

class Create{
    private IzyBit bit;
    private IzyBit[] matrix;
    private byte textSize;
    private String text;

    public Create(){}

    //initialize the Create object
    Create(String text){
        this.bit = new IzyBit();
        this.textSize = (byte) text.length();
        this.matrix = bit.createMatrix(textSize);
        this.text = text;
    }

    //simply print the array on the screen
    /*public void printArray(){
        for(IzyBit bit : matrix){
            bit.printBinaryVersion();
        }
    }*/

    //store the following sentence into
    //an array of IzyBit data(binnary mode)
    void insertText(){
        char [] tab = this.text.toCharArray();

        int i = 0;

        for(IzyBit bit : this.matrix){
            bit.setBitUnit(tab[i]);
            i++;
        }
    }

    //print the array in matrix's version
    //on the screen
    void printMatrix(){
        for(IzyBit bit : matrix){
            for(boolean bool : bit.getBitUnit()){
                System.out.print(
                        (bool ? "1" : "0")
                );
            }
            System.out.println();
        }
    }
}