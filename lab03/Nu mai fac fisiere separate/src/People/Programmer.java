package People;

public class Programmer extends Person{
    private int bugsInCode = 0;

    public Programmer(String name){
        super(name);
    }


    public int getBugsInCode() {
        return bugsInCode;
    }

    public void setBugsInCode(int bugsInCode) {
        this.bugsInCode = bugsInCode;
    }
}
