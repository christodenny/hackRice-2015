function register() {
  if(document.form.password.value !== document.form.confirmPassword.value) {
    alert("The passwords don't match!");
    return;
  }
  var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
  ref.createUser({
    email    : document.form.email.value,
    password : document.form.password.value
  }, function(error) {
    if (error !== null) {
      console.log(error);
      alert("This username is already in use!");
    }
    else {
      var userRef = ref.child("users");
      var user = {};
      user[window.btoa(document.form.email.value)] = {
        firstName : document.form.first_name.value,
        lastName : document.form.last_name.value
      };
      userRef.update(user);
      ref.authWithPassword({
        email : document.form.email.value,
        password : document.form.password.value
      }, function(error) {
        if(error !== null) {
          alert("wtf");
        }
      });
      window.open("InputDemographics.html", "_self");
    }
  });
}
document.getElementById("form").addEventListener("submit", function(e) {
    e.preventDefault();
    register();
});