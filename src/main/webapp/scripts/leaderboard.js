/**
 * AJAX call management
 */

function makeCall(method, url, formElement, cback, reset = true) {
    var req = new XMLHttpRequest(); // visible by closure
    req.onreadystatechange = function() {
        cback(req)
    }; // closure
    req.open(method, url);
    if (formElement == null) {
        req.send();
    } else if(formElement instanceof FormData) {
        req.send(formElement);
    } else {
        req.send(new FormData(formElement));
        if ( reset === true) {
            formElement.reset();
        }
    }
}

window.addEventListener("load", () => {
    makeCall("GET", "./Leaderboard", null,
        function(req) {
            if (req.readyState === 4) {
                var message = req.responseText;
                if (req.status === 200) {
                    var con = JSON.parse(message);
                    if(con.leaderboard != null) {
                        const table = document.getElementById("id_tableBody");
                        Object.keys(con.leaderboard).forEach(function (k) {
                            let row = table.insertRow();
                            let username = row.insertCell(0);
                            username.innerHTML = con.leaderboard[k].userId;
                            let score = row.insertCell(1);
                            score.innerHTML = con.leaderboard[k].score;
                        })
                    }
                    else{
                        document.getElementById("id_Leaderboard_Table").innerHTML =
                            "<div " +
                            " <blockquote class=\"blockquote text-center\">\n" +
                            "  <p class=\"mb-0\">Sorry no product for today, we will be back soon!</p>\n" +
                            "  <footer class=\"blockquote-footer\">Staff</footer>\n" +
                            " </blockquote>\n" +
                            "</div>";
                    }
                }
            } else {
                //display error
            }
        }
    );
});