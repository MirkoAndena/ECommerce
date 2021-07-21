window.onload = login_main;

function login_main() {

    // Hide error field
    document.getElementById("errorMessage").style.display = "none";

    document.getElementById("loginButton").addEventListener('click', e => {
        let form = e.target.closest("form"); // Prendo il form piu vicino
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        if (form.checkValidity() && username.length > 0 && password.length > 0) {
            let callback = response => {
                console.log(response);
                if (response.success)
                	window.location.href = "home.html";
            	else
                    onNotLogged("Credenziali non valide");
            };
            httpPostRequest('Login', form, callback, onNotLogged);
            console.log("sent");
        }
        else onNotLogged("Username o password mancante");
    });
}

function onNotLogged(message) {
    let field = document.getElementById("errorMessage");
    field.innerHTML = message;
    field.style.display = "block"
}