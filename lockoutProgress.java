public class lockoutProgress {
    private String type;
    private double value;
    public lockoutProgress(String t, double v) {
        type = t;
        value = v;
    }
    //GETTERS
    public String getType() {return type;}
    public double getValue() {return value;}
    //SETTERS
    public void setValue(double v) {value = v;}
}