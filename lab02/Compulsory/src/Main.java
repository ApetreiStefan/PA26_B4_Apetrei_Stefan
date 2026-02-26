public class Main{

    public static void main(String[] args) {
        Location l1 = new Location("Harlau", 7, 3);
        Location l2 = new Location("Iasi", 12, 43);
        Road drum1 = new Road("Highway", 80, l1, l2);
        System.out.println(drum1);


    }
}