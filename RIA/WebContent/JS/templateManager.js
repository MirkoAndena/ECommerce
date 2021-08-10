class TemplateManager
{
    constructor()
    {
        this.templates = undefined;
        this.containers = undefined;
        this.domElementIds = undefined;
        this.contents = undefined;

        // callback per la definizione di comportamenti su oggetti come bottoni
        // vengono passati: collezione di oggetti, indici del template, documento del template
        this.action = () => {};
    }

    loadTemplate()
    {
        let indexes = [];
        this._loadTemplate(document, this.contents, 0, indexes);
    }
    
    _loadTemplate(node, contents, depth, indexes)
    {
        // Creazione del documento dai template passati (depth è l'indice)
        let templateDocument = this.createDocument(depth);
        let instances = [];
    
        // Inserimento dei dati nel template (ripetuto il template se collezione di oggetti)
        if (contents && contents.length > 0)
            contents.forEach((content, index) => {
                var clone = templateDocument.content.cloneNode(true);
                let indexes_clone = [...indexes];
                indexes_clone.push(index);
                this.fillTemplateWithValues(clone, content, depth, indexes_clone);
                this.action(this.contents, indexes_clone, clone);
                instances.push(clone);
            });
    
        // iniezione dei template creati (e con dati) nel contenitore definito 
        instances.forEach(instance => node.getElementById(this.containers[depth]).appendChild(instance));
    }
    
    createDocument(depth)
    {
        let templateDocument = document.implementation.createHTMLDocument("templateDocument");
        templateDocument.body.innerHTML = this.templates[depth];
        return templateDocument.getElementsByTagName("template")[0];
    }
    
    fillTemplateWithValues(clone, content, depth, indexes, i)
    {
        Object.keys(content).forEach(key => {
            let link = this.domElementIds[depth][key];
            let isSubTemplate = content[key] && Array.isArray(content[key]);
            if (isSubTemplate) this.loadTemplate(clone, content[key], depth + 1, indexes, i);
            else this.setValueToDomElement(clone.getElementById(link.id), content[key], link.formatter);
        });
    }
    
    setValueToDomElement(element, value, formatter)
    {
        if (!element) return;
        if (element instanceof HTMLImageElement) element.src = value;
        else if (formatter) element.innerHTML = formatter(value);
        else element.innerHTML = value;
    }
}