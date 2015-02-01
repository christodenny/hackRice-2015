function update() {
    var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
    
}
document.getElementById("demographics_form").addEventListener("submit", function(e) {
    e.preventDefault();
    update();
});