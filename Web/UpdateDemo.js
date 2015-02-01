function update() {
    var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
    var auth = ref.getAuth(); // Gets the current user session
    if(auth === null) {
      window.open("index.html", "_self");
    }
    else {
      var form = document.getElementById("demographics_form");
      var email = auth.password.email;
      var name = "users/" + window.btoa(email);
      // console.log(name);
       var user = ref.child(name);
       var dict = {
          "age": form["age"].value,
          "location": form["location"].value,
          "gender": form["gender"].value,
          "student": form["student"].value
       }
       // console.log(dict);
       user.update(dict);
       window.open("ProfileHome.html", "_self");
    }
}
document.getElementById("demographics_form").addEventListener("submit", function(e) {
    e.preventDefault();
    update();
});