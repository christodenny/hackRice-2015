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
      console.log("no error");
      var userRef = ref.child("users");
      var user = {};
      console.log("make user");
      user[window.btoa(document.form.email.value)] = {
        firstName : document.form.first_name.value,
        lastName : document.form.last_name.value
      };
      console.log("made user");
      userRef.update(user);
      ref.authWithPassword({
        email : document.form.email.value,
        password : document.form.password.value
      }, function(error) {
        if(error !== null) {
          alert("wtf");
        }
      }, {
        remember : "sessionOnly"
      });
      console.log(ref.getAuth());
      window.open("InputDemographics.html", "_self");
    }
  });
}
document.getElementById("form").addEventListener("submit", function(e) {
    e.preventDefault();
    register();
});