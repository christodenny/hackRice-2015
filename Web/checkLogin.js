var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
/*ref.authWithPassword({
    email : "christo.denny@gmail.com",
    password: "password"
}, function(error) {
    console.log(error);
});*/
var auth = ref.getAuth();

var toolbar = document.getElementById("toolbar");
if(auth === null) {
	// <li class="active"><a href="LogIn.html">Log In</a></li>
    var child = document.createElement("li");
    child.class = "active";
    var grandchild = document.createElement("a");
    grandchild.href="LogIn.html";
    grandchild.innerText = "Log In";
    child.appendChild(grandchild);
    toolbar.appendChild(child);
}
else {
    var child = document.createElement("li");
    child.class = "active";
    var grandchild = document.createElement("a");
    grandchild.href="logout.html";
    grandchild.innerText = "Log Out";
    child.appendChild(grandchild);
    toolbar.appendChild(child);
}