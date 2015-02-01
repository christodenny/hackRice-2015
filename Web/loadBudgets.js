function updateVisualization(name, email, title, maxMoney, snapshot) {
    var visualize = document.getElementById("dataVisualization");
    setTimeout(function() {
        google.load("visualization", "1", {'callback':'alert("2 seconds");', packages:["corechart"]}, 2000);
    });


    function drawChart() {
        alert("function start");
        // Ranges
        // 0-5
        // 5-20
        // 20-50
        // 50+
        var keys = ['0-5', '5-20', '20-50', '50+'];
        var dict = [['Price Range', 'Number Purchases'], [keys[0], 0], 
                    [keys[1], 0], [keys[2], 0], [keys[3], 0]];

        var obj = snapshot.val().users[email].Purchases;
        for(var key in obj) {
            if(obj.hasOwnProperty(key)) {
                if(key.indexOf(name) > -1) {
                    var value = obj[key].split(",");
                    var category = value[0];
                    var info = value[1];
                    var cost = parseFloat(value[2]);
                    if(category === title) {
                        if(0 <= cost && cost <= 5) {
                            dict[1][1]++;
                        } else if (5 < cost && cost <= 20) {
                            dict[2][1]++;
                        } else if (20 < cost && cost <= 50) {
                            dict[3][1]++;
                        } else {
                            dict[4][1]++;
                        }
                    }
                }
            }
        }

        alert("before toDataTable");

        var data = google.visualization.arrayToDataTable(dict);

        var options = {
          title: 'My Purchases for ' + title
        };

        alert("before piechart visualization");

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        alert("drawing chart");
        chart.draw(data, options);
    }             

    google.setOnLoadCallback(drawChart);
}

var ref = new Firebase("https://intense-heat-8336.firebaseio.com");
var user = ref.getAuth();
var email = window.btoa(user.password.email);
var budget = "users/" + email + "/Budget";
console.log(budget);

// ref.on(budget, function(snapshot){
//     console.log(snapshot);
//     var obj = snapshot.val();
//     console.log(obj);
// }, function(error) {
//     console.log(error);
// });

ref.once('value', function(snapshot) {
    var sidebar = document.getElementById("theBigSidebar");
    var obj = snapshot.val().users[email].Budget;
    for(var key in obj) {
        if(obj.hasOwnProperty(key)) {
            var title = key;
            var maxMoney = obj[key];
            // <li class="active"><a onClick="#">Hello</a></li>
            var child = document.createElement("li");
            child.class="active";
            var grandchild = document.createElement("a");
            grandchild.addEventListener('click', function() { 
                updateVisualization(user.firstName, email, title, maxMoney, snapshot);
            });
            grandchild.innerText=title;
            child.appendChild(grandchild);
            sidebar.appendChild(child);
        }
    }
}, function(error) {
    console.log(error);
});

