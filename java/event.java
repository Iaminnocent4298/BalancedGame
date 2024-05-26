public class event {
    private int intensity;
    private String type;
    private int begin;
    private int end;
    public event() {
        intensity = 0;
        type = "";
        begin = -1;
        end = -1;
    }
    public event(int i, String t, int b, int e) {
        intensity = i;
        type = t;
        begin = b;
        end = e;
    }
    //GET
    public int getIntensity() {return intensity;}
    public String getType() {return type;}
    public int getBegin() {return begin;}
    public int getEnd() {return end;}
    //SET
    public void setIntensity(int i) {intensity = i;}
    public void setType(String s) {type = s;}
    public void setBegin(int i) {begin = i;}
    public void setEnd(int i) {end = i;}
}