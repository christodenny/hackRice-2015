function register() {
    var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
    ref.createUser({
      email    : document.form.email.value,
      password : document.form.password.value
    }, function(error) {
      if (error !== null) {
        alert("This username is already in use!");
      }
      else {
        if(document.form.password === document.form.confirmPassword) {
            var userRef = ref.child("users");
            var user = {};
            user[window.btoa(document.form.email.value)] = {
                firstName : document.form.first_name.value,
                lastName : document.form.last_name.value
            };
            userRef.update(user);
        }
        else {
            alert("The passwords don't match!");
        }
      }
    });
}
document.getElementById("form").addEventListener("submit", function(e) {
    e.preventDefault();
    register();
});