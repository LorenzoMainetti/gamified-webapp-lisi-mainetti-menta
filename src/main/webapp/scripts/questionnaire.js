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

function addFields(number) {
    // Container <div> where dynamic content will be placed
    var container = document.getElementById("id_mandatory");
    // Clear previous contents of the container
    while (container.hasChildNodes()) {
        container.removeChild(container.lastChild);
    }
    for (let i=0; i<number; i++){
        // Append a node with a random text
        container.appendChild(document.createTextNode("Question " + (i+1)));
        // Create an <input> element, set its type and name attributes
        var input = document.createElement("input");
        input.type = "text";
        input.name = "question" + i;
        container.appendChild(input);
        // Append a line break
        container.appendChild(document.createElement("br"));
    }
}

window.addEventListener("load", () => {
    makeCall("GET", "./Questionnaire", null,
        function(req) {
            if (req.readyState == 4) {
                var message = req.responseText;
                if (req.status == 200) {
                    var con = JSON.parse(message)

                    // Number of inputs to create
                    var number = con.length;
                    //create fields
                    addFields(number);



                    /*document.getElementById("id_product_name").innerText = con.prodName;
                    document.getElementById("id_product_description").innerText = con.prodDescriprion;
                    const table = document.getElementById("id_questionnaire");

                    //const container = document.getElementById("id_questionnaire");
                    Object.keys(con.questions).forEach(function (k) {
                        let row = table.insertRow();
                        let question = row.insertCell(0);
                        question.innerHTML = con.questions[k].question;

                        //let question = container.insertAdjacentElement("afterbegin", con.question[k].innerText);
                        //question.innerHTML = con.questions[k].question;
                        //container.appendChild(document.createElement("textarea"))
                    })*/
                }
            } else {
                //display error
            }
        }
    );
});