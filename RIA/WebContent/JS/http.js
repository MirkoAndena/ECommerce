class HTTP {
    static get(url, data, callback, reset = true) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            if (request.readyState == XMLHttpRequest.DONE) {
                let object = JSON.parse(request.response);
                callback(object);
            }
        };
        
        let params = [];
        for (let key in data) params.push(`${key}=${data[key]}`);
        let finalUrl = url + (params.length ? '?' + params.join('&') : '');

        request.open('GET', finalUrl);
        request.send();
    }

    static post(url, form, callback, reset = true) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function() {
            if (request.readyState == XMLHttpRequest.DONE) {
                let object = JSON.parse(request.response);
                callback(object);
            }
        };

        request.open('POST', url);

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
}