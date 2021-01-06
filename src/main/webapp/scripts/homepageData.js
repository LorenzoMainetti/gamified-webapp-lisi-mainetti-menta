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
    makeCall("GET", "./HomepageData", null,
        function(req) {
            if (req.readyState == 4) {
                var message = req.responseText;
                if (req.status == 200) {
                    var con = JSON.parse(message);
                    document.getElementById("var_username").innerText = "Logged in: @" + con.username;
                    document.getElementById("id_product_title").innerText = con.prodName;
                    document.getElementById("id_product_image").src = "data:image/png;base64," + con.encodedImg;
                    document.getElementById("id_product_descript").innerText = con.prodDescription;
                    //document.getElementById("id_userEmail").innerText = user.email;
                    const table = document.getElementById("id_ReviewBody");
                    Object.keys(con.reviews).forEach(function (k) {
                        let row = table.insertRow();
                        let review = row.insertCell(0);
                        review.innerHTML = con.reviews[k];
                    })
                    if (con.userStatus == "BANNED"){
                        document.getElementById("id_action_row").innerHTML=
                            "<div id=\"bannedStuff\" >\n" +
                            " <form id=\"bannedButton\" >\n" +
                            "  <button type=\"button\" class=\"btn btn-danger\" disabled>Banned</button>\n" +
                            " </form>\n" +
                            " <blockquote class=\"blockquote text-center\">\n" +
                            "  <p class=\"mb-0\">You've been banned due to \"inappropriate language during the questionnaire submission</p>\n" +
                            "  <footer class=\"blockquote-footer\">Staff </footer>\n" +
                            " </blockquote>\n" +
                            "</div>";
                    }
                    else if(con.userStatus == "COMPLETED"){
                        document.getElementById("id_action_row").innerHTML=
                            "<div id=\"bannedStuff\" >\n" +
                            " <form id=\"bannedButton\" >\n" +
                            "  <button type=\"button\" class=\"btn btn-success\" disabled>Completed</button>\n" +
                            " </form>\n" +
                            " <blockquote class=\"blockquote text-center\">\n" +
                            "  <p class=\"mb-0\">You've already submitted the questionnaire, thank you</p>\n" +
                            "  <footer class=\"blockquote-footer\">Staff </footer>\n" +
                            " </blockquote>\n" +
                            "</div>";
                    }
                    else if (con.userStatus == "NOT_COMPLETED"){
                        document.getElementById("id_action_row").innerHTML=
                            "<form action=\"questionnaire.html\" method=\"post\">\n" +
                            " <input type=\"submit\" name=\"button1\" value=\"Fill Questionnaire\"/>\n" +
                            "</form>";
                    }
                }
            } else {
                //display error
            }
        }
    );
});