function add(a, b) {
    return a + b;
}

var f1 = function(name) {
    print("JS: " + name);
    return "Greeting from JS";
}
var f2 = function(obj) {
    print("JS Class: " + Object.prototype.toString.call(obj));
}