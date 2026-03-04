package People;

public class Designer extends Person{
    private int carsDesigned = 0;

    public Designer(String name){
        super(name);
    }

    public int getCarsDesigned() {
        return carsDesigned;
    }

    public void setCarsDesigned(int carsDesigned) {
        this.carsDesigned = carsDesigned;
    }
}
