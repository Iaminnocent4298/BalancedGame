export default class event {
    intensity;
    type;
    begin;
    end;
    constructor(intensity, type, begin, end) {
        this.intensity = intensity;
        this.type = type;
        this.begin = begin;
        this.end = end;
    }
    //GET
    getIntensity() {return intensity;}
    getType() {return type;}
    getBegin() {return begin;}
    getEnd() {return end;}
    //SET
    setIntensity(i) {intensity = i;}
    setType(s) {type = s;}
    setBegin(i) {begin = i;}
    setEnd(i) {end = i;}
}