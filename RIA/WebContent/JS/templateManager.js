var TemplateManager = function() {

    this.templates = undefined;
    this.containers = undefined;
    this.domElementIds = undefined;
    this.contents = undefined;
    this.action = function(contents, indexes, node) { };

    this.buildPage = function() {
        let indexes = [];
        this.loadTemplate(document, this.contents, 0, indexes);
    }

    this.loadTemplate = function(node, contents, depth, indexes) {
        let templateDocument = this.createDocument(depth);
        let instances = [];

        if (contents && contents.length > 0)
            contents.forEach((content, index, array) => {
                var clone = templateDocument.content.cloneNode(true);
                let indexes_clone = [...indexes];
                indexes_clone.push(index);
                this.fillTemplateWithValues(clone, content, depth, indexes_clone);
                this.action(this.contents, indexes_clone, clone);
                instances.push(clone);
            });
    
        instances.forEach(instance => node.getElementById(this.containers[depth]).appendChild(instance));
    }

    this.createDocument = function(depth) {
        let templateDocument = document.implementation.createHTMLDocument("templateDocument");
        templateDocument.body.innerHTML = this.templates[depth];
        return templateDocument.getElementsByTagName("template")[0];
    }

    this.fillTemplateWithValues = function(clone, content, depth, indexes, i) {
        Object.keys(content).forEach(key => {
            let link = this.domElementIds[depth][key];
            if (Array.isArray(content[key]))
                this.loadTemplate(clone, content[key], depth + 1, indexes, i);
            else
                this.setValueToDomElement(clone.getElementById(link['id']), content[key], link['formatter']);
        });
    }

    this.setValueToDomElement = function(element, value, formatter) {
        if (!element) return;
        if (element instanceof HTMLImageElement) element.src = value;
        else if (formatter) element.innerHTML = formatter(value);
        else element.innerHTML = value;
    }
}