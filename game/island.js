export class island {
    resources;
    
    constructor() {
        resources = new Map();
    }

    getResourceValue(s) {
        if (resources.has(s)) {
            return resources.get(s);
        }
        return 0;
    }

    setResourceValue(s, i) {
        if (i==0) {
            resources.delete(s);
            return;
        }
        resoureces.set(s,i);
    }

    addResourceValue(s, i) {
        if (resources.has(s)) {
            resources.set(s, resources.get(s)+i);
        }
        else {
            resources.set(s,i);
        }
        if (resources.get(s)==0) {
            resources.delete(s);
        }
    }

    resourceRemovable(s, i) {
        if (!resources.has(s) || resources.get(s)<i) {
            return false;
        }
        return true;
    }
}
