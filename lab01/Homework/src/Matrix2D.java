import graphics.Screen;

public class Matrix2D{
    public static long startTime = System.nanoTime();
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Utilizare incorecta!");
            return;
        }


        int temp = Integer.parseInt(args[0]);
        Screen m = new Screen(temp);

        if(args[1].equals("circle")){
            m.makeCircle();
        }
        else if(args[1].equals("rectangle")){
            m.makeRectangle();
        } // 25000 a dat crash pentru mine, dar functiona cu Xmx4G

        if(temp > 100){
            System.out.println(String.format("Numar mare, nu mai afisam " + (System.nanoTime() - startTime)));
        }

        m.print(); // folosesc intellij si am rulat folosind "run configurations"

    }

}