package com.example.izycode.izyCodePackage;

import java.util.*;


public class IzyBit {
    //global variable to contain unitBit size
    private byte UNITSIZE = (byte) 8;

    private char letter;
    private boolean []bitUnit;

    //initialize bitUnit to an array of 8 bits(represented by booleans)
    IzyBit(){
        this.bitUnit = new boolean[8];
    }

    boolean[] getBitUnit(){
        return this.bitUnit;
    }

    public char getLetter(){
        return this.letter;
    }

    //set unitBit to contain the binary representation
    //of a char(letter)
    void setBitUnit(char letter){
        //contains the ASCII code of the char
        byte byteLetter = (byte) letter;
        this.letter = letter;

        int inc = UNITSIZE - 1;

        while(byteLetter != 0 && inc > 0){
            //if letter % 2 == 0 we store false in
            //boolean array to represent 0 bit
            //else we store true to represent 1 bit
            this.bitUnit[inc] =
                    (byteLetter % 2 !=0);

            byteLetter = (byte) (byteLetter / 2);
            inc--;
        }
    }

    IzyBit[] createMatrix(byte size){
        IzyBit[] array = new IzyBit[size];

        for(byte i = 0; i < size; i++){
            array[i] = new IzyBit();
        }

        return array;
    }


    void printBinaryVersion(){
        for(boolean bit : bitUnit){
            System.out.print(
                    !bit ? 0 : 1
            );
        }
    }

}