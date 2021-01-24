var createdQuestions = 1;

function sendQuestionnaire() {
    var form = document.getElementById("creation-form");
    var sub = true;
    makeCall("POST", '../CreateQuestionnaire', form, sub,
        function (req) {
            if (req.readyState == XMLHttpRequest.DONE) {
                var message = req.responseText;
                switch (req.status) {
                    case 200:

                        break;
                    case 400: // bad request

                        break;
                    case 401: // unauthorized

                        break;
                    case 500: // server error

                        break;
                }
            }
        }
    );
}

function addQuestionDynamic() {

    createdQuestions = createdQuestions + 1;

    const questionDiv = document.getElementById("id_product_questions");

    var newQuestion = document.createElement('div');
    newQuestion.className = 'form-group';
    newQuestion.innerHTML =
        "                        <label >Question x:</label>\n" +
        "                        <input type=\"text\" class=\"form-control\"   placeholder=\"Enter question text followed by '?' \">\n"
    ;
    const label = newQuestion.getElementsByTagName("label")[0];
    const input = newQuestion.getElementsByTagName("input")[0];

    const idString = "question" + createdQuestions;
    input.id = idString;
    input.name = 'man[]';
    label.htmlFor = idString;
    label.innerText = "Question #" + createdQuestions;


    questionDiv.appendChild(newQuestion);
}