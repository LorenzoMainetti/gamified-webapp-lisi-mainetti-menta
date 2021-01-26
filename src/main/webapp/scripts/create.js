var createdQuestions = 1;


function sendQuestionnaire() {

    const bodyFormData = new FormData(document.getElementById("creation-form"));
    const errorAlert = document.getElementById("error-alert");


    axios({
        method: 'post',
        url: '../CreateQuestionnaire',
        data: bodyFormData,
        headers: {'Content-Type': 'multipart/form-data' }
    })
        .then(function (response) {
            //handle success
            alert("New product has been added.");
            document.location.href = "index.html";
        })
        .catch(function (error) {
            //handle error
            errorAlert.style.display = "block";
            errorAlert.innerText = error.response.data;
        });

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