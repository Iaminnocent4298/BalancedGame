import java.util.*;

public class island {
    private HashMap<String, Integer> resources;
    
    public island() {
        resources = new HashMap<>();
    }

    public int getResourceValue(String s) {
        if (resources.containsKey(s)) {
            return resources.get(s);
        }
        return 0;
    }

    public void setResourceValue(String s, int i) {
        if (i==0) {
            resources.remove(s);
            return;
        }
        if (resources.containsKey(s)) {
            resources.replace(s, i);
        }
        else resources.put(s,i);
    }

    public void addResourceValue(String s, int i) {
        if (resources.containsKey(s)) {
            resources.replace(s, resources.get(s)+i);
        }
        else {
            resources.put(s,i);
        }
        if (resources.get(s)==0) {
            resources.remove(s);
        }
    }

    public boolean resourceRemovable(String s, int i) {
        if (!resources.containsKey(s) || resources.get(s)<i) {
            return false;
        }
        return true;
    }
}
