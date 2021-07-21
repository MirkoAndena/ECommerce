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

function loadTemplate(response, sourceElementName) {
    let template = response['template']; // Html del template
    let contents = response['contents']; // Collezione di oggetti da inserire (idhtml: testo)

    if (template) {

        // Caricamento del template
        let templateDocument = document.implementation.createHTMLDocument("templateDocument");
        var templateElement = templateDocument.getElementsByTagName("template")[0];

        var instances = [];
        if (contents && contents.length > 0) {
            contents.forEach(content => {
                var clone = templateElement.content.cloneNode(true);
                Object.keys(content).forEach(key => {
                    clone.getElementById(key).innerHTML = content[key];
                });
                instances.push(clone);
            });
        }

        instances.forEach(instance => document.getElementById(sourceElementName).appendChild(instance));
    }
}