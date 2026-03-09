public class Intersection {
    private String name;

    public Intersection(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other){
       switch (other){
           case null:
               return false;
           case Intersection intersection:
               return this.name == intersection.name;
           default: return false;
       }
    }
}
