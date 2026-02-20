public class Screen {
    private int size;
    private int[][] m;

    public Screen(int size){
        this.m = new int[size][size];
        this.size = size;
    }

    public void makeRectangle(){
        for(int i = 0; i < size; i++){
            m[0][size-1] =
        }
    }

    public void print(){
        for(int[] i : m){
            for(int j : i){
                System.out.print(Character.toChars(j));
            }
            System.out.println();
        }
    }
}
