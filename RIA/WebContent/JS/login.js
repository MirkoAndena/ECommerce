window.onload = login_main;

function login_main() {

    // Hide error field
    document.getElementById("errorMessage").style.display = "none";

    document.getElementById("loginButton").addEventListener('click', e => {
        let form = e.target.closest("form"); // Prendo il form piu vicino
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        if (form.checkValidity() && notEmpty(username) && notEmpty(password)) {
            let callback = response => {
                if (response["logged"] == true)
                	window.location.href = "Index";
            	else
                    onNotLogged("Credenziali non valide");
            };
            httpPostRequest('Login', form, callback, onNotLogged);
        }
        else onNotLogged("Username o password mancante");
    });
}

function onNotLogged(message) {
    let field = document.getElementById("errorMessage");
    field.innerHTML = message;
    field.style.display = "block"
}