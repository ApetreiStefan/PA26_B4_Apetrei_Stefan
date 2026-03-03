import People.*;
import java.util.ArrayList;
import java.util.Collections;
public class Main{

    public static void main(String[] args){
        ArrayList<Company> companies = new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();

        companies.add(new Company("BitOffender"));
        companies.add(new Company("Amazon"));
        companies.add(new Company("BitDefender"));

        people.add(new Person("Dan"));
        people.add(new Person("Maria"));
        people.add(new Person("Ana"));

        for(var it : people){
            System.out.println(it.getID() + " " + it.getName());
        }

        for(var it : companies){
            System.out.println(it.getID() + " " + it.getName());
        }

        System.out.println();

        Collections.sort(companies);
        Collections.sort(people);

        for(var it : people){
            System.out.println(it.getID() + " " + it.getName());
        }

        for(var it : companies){
            System.out.println(it.getID() + " " + it.getName());
        }

    }
}