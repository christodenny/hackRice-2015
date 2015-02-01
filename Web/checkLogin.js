var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
var auth = ref.getAuth();
if(auth === null) {
	window.open("index.html", "_self");
}