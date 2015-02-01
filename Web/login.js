function login() {
  var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
  var user = {
  	email : document.form.email.value,
  	password : document.form.password.value
  }
  var handler = function(error) {
  	if(error === null) {
  		alert("Invalid username/password combination!");
  	} else {
  		window.open("InputDemographics.html", "_self");
  	}
  };
  if(document.getElementById("checkbox").checked) {
  	ref.authWithPassword(user, handler, "default");
  } else {
  	ref.authWithPassword(user, handler, "sessionOnly");
  }
}
document.getElementById("form").addEventListener("submit", function(e) {
    e.preventDefault();
    login();
});