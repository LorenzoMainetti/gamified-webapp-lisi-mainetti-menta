/**
 * AJAX call management
 */

function makeCall(method, url, formElement, cback, reset = true) {
    var req = new XMLHttpRequest(); // visible by closure
    req.onreadystatechange = function () {
        cback(req)
    }; // closure
    req.open(method, url);
    if (formElement == null) {
        req.send();
    } else if (formElement instanceof FormData) {
        req.send(formElement);
    } else {
        req.send(new FormData(formElement));
        if (reset === true) {
            formElement.reset();
        }
    }
}

function sendQuestionnaire () {

}
function fillQuestions(questionsList) {

    var container = document.getElementById("id_question_container");
    container.innerHTML = "";
    questionsList.forEach(question => {

        var questionBox = document.createElement("div");
        questionBox.className = "mandatory-question";
        questionBox.innerHTML =
            "             <label class=\"col-form-label\">" + question + "</label>\n" +
            "               <form class=\"needs-validation\" novalidate>\n" +
            "                   <div class=\"form-group\">\n" +
            "                           <textarea class=\"form-control\" id=\"answer\" rows=\"3\" placeholder=\"Answer\" required></textarea>\n" +
            "                           <div class=\"invalid-tooltip\">\n" +
            "                                Please provide a valid answer. This field is mandatory.\n" +
            "                           </div>\n" +
            "                        </div>\n" +
            "               </form>"
        container.appendChild(questionBox);
    })
}

function forceLocalLogout() {
    //todo
}

function displayFirstPart() {
    let form1 = document.getElementById("id_mandatory");
    let form2 = document.getElementById("id_optional");
    form1.style.display = "block";
    form2.style.display = "none";
}

function displaySecondPart() {
    let form1 = document.getElementById("id_mandatory");
    let form2 = document.getElementById("id_optional");
    form1.style.display = "none";
    form2.style.display = "block";
}
function displayError (errorText) {
    let error = document.getElementById("id_quest_error");
    error.textContent = errorText;
    error.style.display = "block";
}

function hideError() {
    let error = document.getElementById("id_quest_error");
    error.textContent = "";
    error.style.display = "none";
}

function initForms() {
    displayFirstPart();
    hideError();
}
window.addEventListener("load", () => {
    initForms();

    makeCall("GET", "./QuestionnairePageData", null,
        function (req) {
            if (req.readyState == 4) {
                var message = req.responseText;
                if (req.status == 200) {
                    var con = JSON.parse(message).optionalQuestions;
                    fillQuestions(con);
                }
            } else {
                switch (req.status) {
                    case 400: // bad request
                        displayError("bad request")
                        break;
                    case 401: // unauthorized
                        displayError("unauthorized");
                        forceLocalLogout();
                        break;
                    case 500: // server error
                        displayError("internal server error");
                        forceLocalLogout();
                        break;
                }
            }
        }
    );
});