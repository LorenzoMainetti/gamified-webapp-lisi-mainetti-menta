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

function insertQuestions(con, table){
    let row = table.insertRow();
    let question = row.insertCell(-1);
    question.innerHTML = "QUESTIONS";
    Object.keys(con.questions).forEach(function (k) {
        let question = row.insertCell(-1);
        question.innerHTML = con.questions[k];
    })
}

function insertAnswers(con, table){
    Object.keys(con.completed).forEach(function (k) {
        var username = con.completed[k];
        let row = table.insertRow();
        let user = row.insertCell(-1);
        user.innerHTML = username;
        for (j = 0; j < con.questions.length; j++){
            let answer = row.insertCell(-1);
            if(j < con.answers[username].length){
                answer.innerHTML = con.answers[username][j];
            }
            else{
                answer.innerHTML = "";
            }
        }
    })
    Object.keys(con.canceled).forEach(function (k) {
        var username = con.canceled[k];
        let row = table.insertRow();
        let user = row.insertCell(-1);
        user.innerHTML = username;
        for (j = 0; j < con.questions.length; j++){
            let answer = row.insertCell(-1);
            answer.innerHTML = "/"
        }
    })
}

function populateTable(con, table){
    insertQuestions(con, table);
    insertAnswers(con, table);
}

window.addEventListener("load", () => {
    makeCall("GET", "../GetInspectionData", null,
        function(req) {
            if (req.readyState == 4) {
                var message = req.responseText;
                if (req.status == 200) {

                    var con = JSON.parse(message);
                    document.getElementById("id_product_title").innerText = con.prodName;
                    document.getElementById("id_product_date").innerText = con.date;
                    document.getElementById("id_product_image").src = "data:image/png;base64," + con.encodedImg;
                    document.getElementById("id_product_description").innerText = con.prodDescription;
                    const table = document.getElementById("id_tableBody");
                    populateTable(con, table);
                }
            } else {
                //display error
            }
        }
    );
});