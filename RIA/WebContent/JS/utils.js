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
        request.send(new FormData(form));
        if (reset === true) form.reset();
    }
    else {
        let formData = new FormData();
        Object.keys(form).forEach(key => {
            formData.append(key, form[key]);
        });
        request.send(formData);
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