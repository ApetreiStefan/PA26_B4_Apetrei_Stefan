import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.random.*;

public class Main {
    public static void main (String[] args){
        List<Intersection> intersections = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> new Intersection("v" + i) )
                .toList();

        List<Street> streets = new LinkedList<>(IntStream.range(0,intersections.size()-1)
                .mapToObj(i -> {
                    return new Street(intersections.get(i), intersections.get(i+1), "s" + i, (i*31 + 9)%31);
                })
                .toList());

        streets.sort( (s1,s2) -> Integer.compare(s1.getLength(), s2.getLength()));

        Set<Intersection> setOfIntersections = new HashSet<>(intersections); // verificam daca elementele din set sunt unice cu metoda equals, care este overriten in clasa Intersection


        System.out.println(setOfIntersections.size() - intersections.size());


    }

}
