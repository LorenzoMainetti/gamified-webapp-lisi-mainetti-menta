var signup_id = "form-signup";
var login_id = "form-login";
function showLogin() {
    document.getElementById(login_id).style.display="block";
    document.getElementById(signup_id).style.display="none";
}
function showSignUp() {
    document.getElementById(signup_id).style.display="block";
    document.getElementById(login_id).style.display="none";
}