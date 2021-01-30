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
    let question = document.createElement("th");
    question.innerHTML = "USERS/QUESTIONS";
    question.scope = "col"
    row.appendChild(question);
    //question.style.backgroundColor = "lightblue"
    Object.keys(con.questions).forEach(function (k) {
        let question = document.createElement("th");
        question.innerHTML = con.questions[k];
        question.scope = "col"
        row.appendChild(question);
        //question.style.backgroundColor = "azure";
    })
}

function insertAnswers(con, table){
    Object.keys(con.completed).forEach(function (k) {
        var username = con.completed[k];
        let row = table.insertRow();
        let user = row.insertCell(-1);
        user.innerHTML = username;
        //user.style.backgroundColor = "azure";
        for (j = 0; j < con.questions.length; j++){
            let answer = row.insertCell(-1);
            if(j < con.answers[username].length){
                answer.innerHTML = con.answers[username][j];
                //answer.style.backgroundColor="white"
            }
            else{
                answer.innerHTML = "";
                //answer.style.backgroundColor="grey"
            }
        }
    })
    Object.keys(con.canceled).forEach(function (k) {
        var username = con.canceled[k];
        let row = table.insertRow();
        let user = row.insertCell(-1);
        user.innerHTML = username;
        //user.style.backgroundColor = "azure"
        for (j = 0; j < con.questions.length; j++){
            let answer = row.insertCell(-1);
            answer.innerHTML = "canceled"
            //answer.style.backgroundColor= "lightgrey"
        }
    })
}

function populateTable(con, tableBody, tableHead){
    if (con.completed.length===0 && con.canceled.length===0) {
        let row = tableHead.insertRow();
        let noFilled = row.insertCell(-1);
        noFilled.innerHTML = "Sorry but nobody filled or cancelled this questionnaire";
        //noFilled.style.backgroundColor = "azure"
    }
    else{
        insertQuestions(con, tableHead);
        insertAnswers(con, tableBody);
    }
}


window.addEventListener("load", () => {
    var text = window.location.hash.substring(1);
    makeCall("GET", "../GetInspectionData?pid="+ text, null,
        function(req) {
            if (req.readyState === 4) {
                var message = req.responseText;
                if (req.status === 200) {

                    var con = JSON.parse(message);
                    document.getElementById("id_product_title").innerText = con.prodName;
                    document.getElementById("id_product_date").innerText = con.date.split(", 12:00:00")[0];
                    document.getElementById("id_product_image").src = "data:image/png;base64," + con.encodedImg;
                    document.getElementById("id_product_description").innerText = con.prodDescription;
                    const tableBody = document.getElementById("id_tableBody");
                    const tableHead = document.getElementById("id_tableHead");
                    populateTable(con, tableBody, tableHead);
                }
            } else {
                //display error
            }
        }
    );
});