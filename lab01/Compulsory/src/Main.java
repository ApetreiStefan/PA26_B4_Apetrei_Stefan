public class Main {
    public static void main(String[] args) {
        IO.println("Hello World!");
        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};
        for(String it : languages){
            IO.print(String.format(it + " "));
        }
        IO.print('\n');
        int n = (int) (Math.random() * 1_000_000);

        IO.println(n);

        n = n*3;
        n = n + 0b10101;
        n = n + 0xFF;
        n = n * 6;

        IO.println(n);
        int temp = n;
        while(n > 9){
            n = 0;
            while(temp != 0){
                n = n + temp % 10;
                temp = temp / 10;
            }
            temp = n;
        }

        IO.println(n);

        IO.println(String.format("Willy nilly, this semester i will learn " + languages[n]));
    }
}
