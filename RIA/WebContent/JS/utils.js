function httpRequest(method, url, form, callback, reset) {
    var request = new XMLHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState == XMLHttpRequest.DONE)
            callback(request.response);
    };

    request.open(method, url);

    if (form == null) request.send();
    else request.send(new FormData(form));

    if (form !== null && reset === true)
      form.reset();
}

function httpGetRequest(url, form, callback, reset = true) {
    return httpRequest("GET", url, form, callback, reset);
}

function httpPostRequest(url, form, callback, reset = true) {
    return httpRequest("POST", url, form, callback, reset);
}