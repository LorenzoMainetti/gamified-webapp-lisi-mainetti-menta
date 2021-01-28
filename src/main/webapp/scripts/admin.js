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
    makeCall("GET", "../GetAdminHomePageData", null,
        function(req) {
            if (req.readyState === 4) {
                var message = req.responseText;
                if (req.status === 200) {
                    var con = JSON.parse(message);
                    if(con.adminStatus === "NOT_AVAILABLE"){
                        document.getElementById("id_adminId").innerText = con.adminId;
                        document.getElementById("id_adminEmail").innerText = con.email;
                        document.getElementById("id_product_title").innerText = "No Product";
                        document.getElementById("id_product_image").innerHTML = "";
                        document.getElementById("id_product_descript").innerText = "No product available for today";
                    }
                    else {
                        document.getElementById("id_adminId").innerText = con.adminId;
                        document.getElementById("id_adminEmail").innerText = con.email;
                        document.getElementById("id_product_title").innerText = con.prodName;
                        document.getElementById("id_product_image").src = "data:image/png;base64," + con.encodedImg;
                        document.getElementById("id_product_descript").innerText = con.prodDescription;
                    }
                }
            } else {
                //display error
            }
        }
    );
});

