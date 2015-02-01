function login() {
  var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
  var user = {
  	email : document.form.email.value,
  	password : document.form.password.value
  }
  var handler = function(error) {
  	if(error === null) {
      window.open("ProfileHome.html", "_self");
  		// window.open("InputDemographics.html", "_self");
  	} else {
  		alert("Invalid username/password combination!");
  	}
  };
  ref.authWithPassword(user, handler, {
  	remember: document.getElementById("checkbox").checked ? "default" : "sessionOnly"
  });
}
document.getElementById("form").addEventListener("submit", function(e) {
    e.preventDefault();
    login();
});