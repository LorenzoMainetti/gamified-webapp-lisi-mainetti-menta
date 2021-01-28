/**
 * AJAX call management
 */

function makeCall(method, url, formElement, sub, cback, reset = true) {
    var req = new XMLHttpRequest(); // visible by closure
    req.onreadystatechange = function () {
        cback(req)
    }; // closure
    req.open(method, url);
    if (formElement == null) {
        req.send();
    } else if (formElement instanceof FormData) {
        req.setRequestHeader("submitted", sub)
        req.send(formElement);
    } else {
        req.setRequestHeader("submitted", sub)
        req.send(new FormData(formElement));
        if (reset === true) {
            formElement.reset();
        }
    }
}


function fillQuestions(questionsList) {

    var container = document.getElementById("id_question_container");
    container.innerHTML = "";
    questionsList.forEach(question => {

        var questionBox = document.createElement("div");
        questionBox.className = "mandatory-question";
        questionBox.innerHTML =
            "             <label class=\"col-form-label\">" + question + "</label>\n" +
            "                   <div class=\"form-group\">\n" +
            "                           <textarea class=\"form-control\" name=\"man[]\" id=\"answer\" rows=\"3\" placeholder=\"Answer\" required></textarea>\n" +
            "                           <div class=\"invalid-tooltip\">\n" +
            "                                Please provide a valid answer. This field is mandatory.\n" +
            "                           </div>\n" +
            "                        </div>\n"
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
    hideError()//fancy but check if it's correct
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
    makeCall("GET", "./QuestionnairePageData", null, null,
        function (req) {

        document.getElementById("id_product_name").innerText
            if (req.readyState === 4) {
                var message = req.responseText;
                if (req.status === 200) {
                    var parsed = JSON.parse(message)
                    var con = parsed.optionalQuestions;
                    document.getElementById("id_product_name").innerText = parsed.name;
                    document.getElementById("id_product_description").innerText = "Description: \n" + parsed.description;
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

//when the user tries to submit a meeting from the home page
document.getElementById("id_submit_questionnaire").addEventListener('click', (e) => {
    var target = e.target;
    var form = document.getElementById("questionnaire_form");
    var sub = true;
    if (form.checkValidity()) {
        displayError("sending...");
        makeCall("POST", './SubmitAnswer', form, sub,
            function (req) {
                if (req.readyState === XMLHttpRequest.DONE) {
                    var message = req.responseText;
                    switch (req.status) {
                        case 200:
                            window.location.assign("./thanks.html");
                            break;
                        case 400: // bad request
                            //displayError(message);
                            window.location.assign("./banned.html");
                            break;
                        case 401: // unauthorized
                            //forceLocalLogout();
                            break;
                        case 500: // server error
                            //backToWebApp("internal server error.");
                            break;
                    }
                }
            }
        );
    }
    else{
        displayFirstPart()
        displayError("Complete all mandatory fields!")
    }
});

document.getElementById("id_cancel_questionnaire").addEventListener('click', (e) => {
    var form = document.getElementById("questionnaire_form");
    var sub = false;
    displayError("deleting...");
    makeCall("POST", './SubmitAnswer', form, sub,
        function (req) {
            if (req.readyState === XMLHttpRequest.DONE) {
                window.location.assign("./cancelled.html");
            }
        }
    );
});
