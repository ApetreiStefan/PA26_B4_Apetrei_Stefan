package graphics;

public class Screen {
    private int size;
    private int[][] m;

    private String whiteRectangle = "\u25ad";
    private String blackRectangle = "\u25ae";

    public Screen(int size){
        this.m = new int[size][size];
        this.size = size;
    }

    public void makeRectangle(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(i < 5 || i > size - 5 || j < 5 || j > size - 5) {
                    m[i][j] = 0;
                }
                else m[i][j] = 255;
            }
        }
    }

    public void makeCircle(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if((i-size/2) * (i-size/2) + (j-size/2) * (j-size/2) < (size/2 - 2)*(size/2 - 2)) {
                    m[i][j] = 255;
                }
                else m[i][j] = 0;
            }
        }
    }

    public void print(){
        for(int[] i : m){
            for(int j : i){
                if(j == 0){
                    System.out.print("# "); // terminalul meu nu vrea sa afiseze unicode
                                            // cum ideea e sa invat java, nu windows, am ales sa folosesc asta
                }
                if(j == 255){
                    System.out.print("_ ");
                }

            }
            System.out.println();
        }
    }
}
