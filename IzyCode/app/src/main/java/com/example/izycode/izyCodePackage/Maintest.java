package com.example.izycode.izyCodePackage;

class Maintest {
    public static void main(String[] args){

        String str = "wesh comment ca va maggle ?";

        Create createArray = new Create(str);

        System.out.println("---Chaque ligne est la réprésentation binaire d'un caractère codé en ASCII---\n");

        System.out.println("phrase : " + str + "\nTaille : " + str.length()+"\n");

        createArray.insertText();

        createArray.printMatrix();

    }
}
