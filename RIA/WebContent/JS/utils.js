function httpRequest(method, url, form, callback, reset) {
    var request = new XMLHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState == XMLHttpRequest.DONE) {
            let object = JSON.parse(request.response);
            callback(object);
        }
    };

    request.open(method, url);

    if (form == null) request.send();
    else if (form instanceof HTMLFormElement) {
        if (form == null) request.send();
        else request.send(new FormData(form));

        if (form !== null && reset === true)
            form.reset();
    }
    else
    {
        request.send(JSON.stringify(form));
    }
}

function httpGetRequest(url, form, callback, reset = true) {
    return httpRequest("GET", url, form, callback, reset);
}

function httpPostRequest(url, form, callback, reset = true) {
    return httpRequest("POST", url, form, callback, reset);
}

function notEmpty(text) {
    if (text) return text.trim().length > 0;
    return false;
}