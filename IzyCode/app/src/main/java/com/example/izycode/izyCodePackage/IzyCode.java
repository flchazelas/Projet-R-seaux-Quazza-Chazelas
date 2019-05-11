package com.example.izycode.izyCodePackage;

import android.util.Log;

import static java.lang.String.valueOf;

public class IzyCode {
    private byte[][] izyCode;
    private int izyCodeSize;

    public IzyCode(){}

    //fill the array with succession of bits that represent its ascii representation
    public void fillIzyCode(String sentence){
        int sizeOfbyte = 8;    //size of a byte
        byte[] str;
        int tempSize = sentence.length();
        int count = 0; // used to know how much time we do the % on the number of char

        StringBuilder tempSentence = new StringBuilder();
        StringBuilder tempSentence2 = new StringBuilder();

        while(((tempSize % 10) != 0) || (tempSize != 0)){
            tempSentence.append(String.valueOf(tempSize%10));
            tempSize /= 10;
            count += 1;
        }

        //used to complete the arraysize with 0 if count is under 3
        while(count < 3){
            tempSentence.append("0");
            count++;
        }

        tempSentence2.append(sentence);

        for(int i = tempSentence.length() - 1; i > -1; i--){
            tempSentence2.append(tempSentence.charAt(i));
        }

        sentence = tempSentence2.toString();

        izyCodeSize = sentence.length();

        str = sentence.getBytes();//array that contains byte of each character of the sentence
                                    //It's used to know how much lines the array contains



        izyCode = new byte[izyCodeSize][sizeOfbyte];

        for(int i = 0; i < izyCodeSize; i++){
            byte temp = str[i];
            int j = 0;

            while(temp > 0 && j < 7){

                izyCode[i][j] = (byte)(temp % 2);
                temp /= 2;
                j++;
            }
        }

        //reverse the array
        for(int x = 0; x < izyCodeSize; x++){
            for(int y = 0; y < izyCode[x].length / 2; y++){
                byte temp = izyCode[x][y];

                izyCode[x][y] = izyCode[x][izyCode[x].length - 1 - y];
                izyCode[x][izyCode[x].length - 1 - y] = temp;
            }
        }
    }

    //returns the array of bits of the sentence
    public byte[][] getIzyCodeArrayOfBits(){
        return izyCode;
    }

    public int getIzyCodeSize(){
        return izyCodeSize;
    }
}

