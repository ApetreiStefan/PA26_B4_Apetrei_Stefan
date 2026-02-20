package Graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Screen {
    private int[][] m;
    private int size;

    public Screen(String numeFisier){
        //Acum imi pot imagina de ce voiati sa avem o reprezentare String a matricei anterioare
        //Dar in loc sa dau matricea ca argument in terminal, am sa invat sa citesc din fisier
        try{
            File fisier = new File(numeFisier);
            Scanner reader = new Scanner(fisier); // throw >:(

            if (reader.hasNextInt()) {
                size = reader.nextInt();
                m = new int[size][size];
            }

            else {
                System.out.println("Ceva a mers rau!");
                return;
            }

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (reader.hasNextInt()) {
                        m[i][j] = reader.nextInt();
                    }
                }
            }
            reader.close();
        } catch(FileNotFoundException e){
            System.out.println("Eroare: In acest limbaj nu avem erori neabordate!");
            return;
        }
        System.out.println(size);
    }

    public void afiseazaMatrice(){
        for(int[] i : m){
            for(int j : i){
                if(j == 0){
                    System.out.print("_ ");
                }
                else if(j == 255){
                    System.out.print("# ");
                }
                else if(j == 2){
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    public int getSize(){return size;}
    public int getElem(int i, int j){return m[i][j];}
    public void setElem(int i, int j, int val){m[i][j] = val;}
}
