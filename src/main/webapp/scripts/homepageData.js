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
    makeCall("GET", "./Homepage", null,
        function(req) {
            if (req.readyState == 4) {
                var message = req.responseText;
                if (req.status == 200) {
                    var con = JSON.parse(message);
                    document.getElementById("var_username").innerText = "Logged in: @" + con.username;
                    document.getElementById("id_product_title").innerText = con.prodName;
                   //TODO document.getElementById("id_product_image")
                    document.getElementById("id_product_descript").innerText = con.prodDescription;
                    //document.getElementById("id_userEmail").innerText = user.email;

                }
            } else {
                //display error
            }
        }
    );
});