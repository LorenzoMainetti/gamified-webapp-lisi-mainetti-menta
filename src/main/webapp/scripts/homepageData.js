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
                }
            } else {
                //display error
            }
        }
    );
});