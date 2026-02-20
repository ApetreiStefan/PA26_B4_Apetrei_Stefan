import Graphics.*;
public class Main{
    public static void main(String[] args){
//        if(args.length != 1){
//            System.out.println("Utilizare incorecta!");
//            return;
//        }
    Screen n = new Screen("matrice.txt");
    Box temp = new Box();

    temp.detectBox(n);
    n.afiseazaMatrice();

    }
}