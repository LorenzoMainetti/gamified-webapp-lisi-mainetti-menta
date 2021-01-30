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

function populateTable(con, table){
    Object.keys(con.pastQuestionnaires).forEach(function (k) {
        let row = table.insertRow();

        //add product date
        let date = row.insertCell(0);
        date.innerHTML = con.pastQuestionnaires[k].date.split(", 12:00:00")[0];
        //add product name
        let name = row.insertCell(1);
        name.innerHTML = con.pastQuestionnaires[k].name;

        //add inspection button
        let inspect_button = row.insertCell(2);

        var button1 = document.createElement('button');
        button1.className = "btn btn-success";
        button1.innerHTML = "Inspect";
        button1.setAttribute('content', con.pastQuestionnaires[k].id);
        button1.addEventListener('click', () => sendProductId(button1.getAttribute('content')));
        inspect_button.appendChild(button1);


        //add deletion button
        let delete_button = row.insertCell(3);
        var button2 = document.createElement("div");
        button2.innerHTML = "<form action=\"../DeleteQuestionnaire\" method=\"POST\" >\n" +
            "<input type=\"submit\" value=\"Dele6\te\" />\n" +
            "<input name=\"prodId\" class =\"btn btn-danger\" type=\"hidden\" type=\"text\"  />"

        button2.getElementsByClassName("btn btn-danger")[0].value = con.pastQuestionnaires[k].id;

        delete_button.appendChild(button2);
    })
}

function sendProductId(pid) {
    window.location.href = 'inspection.html' + '#' + pid;
}


window.addEventListener("load", () => {
    makeCall("GET", "../GetPastQuestionnairePageData", null,
        function(req) {
            if (req.readyState === 4) {
                var message = req.responseText;
                if (req.status === 200) {

                    var con = JSON.parse(message);
                    const table = document.getElementById("id_tableBody");
                    populateTable(con, table);
                }
            } else {
                //display error
            }
        }
    );
})