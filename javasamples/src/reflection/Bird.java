package reflection;

public class Bird {
    public int weight;
    private boolean canFly;
    private String name;

    public void setBreathe(int weight) {
        this.weight = weight;
    }

    private void setCanFly() {
        this.canFly = true;
    }

    private Bird() {
        this.canFly = false;
        this.name = "namaste";
    }
}
