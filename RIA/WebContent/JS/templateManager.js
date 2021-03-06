class TemplateManager
{
    constructor()
    {
        this.templates = undefined;
        this.containers = undefined;
        this.domElementIds = undefined;
        this.contents = undefined;
        this.contentElementToExpand = undefined;

        // callback per la definizione di comportamenti su oggetti come bottoni
        // vengono passati: collezione di oggetti, indici del template, documento del template
        this.action = () => {};
    }

    createTemplate()
    {
        this.containers = ['container'];
        let templateDocument = this.createDocument(`<template><ul class="list-group" id="container"></ul></template>`);
        let indexes = [];
        this._loadTemplate(templateDocument.content, this.contents, 0, indexes);
        return templateDocument.innerHTML;
    }

    loadTemplate()
    {
        let indexes = [];
        this._loadTemplate(document, this.contents, 0, indexes);
    }
    
    _loadTemplate(node, contents, depth, indexes)
    {
        // Creazione del documento dai template passati (depth è l'indice)
        let templateDocument = this.createDocument(this.templates[depth]);
        let instances = [];
    
        // Inserimento dei dati nel template (ripetuto il template se collezione di oggetti)
        if (contents) {
            instances = contents instanceof Array ? 
                        this.createChildrenElements(contents, templateDocument, indexes, depth) :
                        this.createChildElement(contents, templateDocument, indexes, depth);
        }
    
        // iniezione dei template creati (e con dati) nel contenitore definito 
        instances.forEach(instance => node.getElementById(this.containers[depth]).appendChild(instance));
    }

    createChildElement(contents, templateDocument, indexes, depth) {
        let instance = this.createAndFillWithContent(contents, 0, templateDocument, indexes, depth);
        return [ instance ];
    }

    createChildrenElements(contents, templateDocument, indexes, depth)
    {
        return contents.map((content, index) => this.createAndFillWithContent(content, index, templateDocument, indexes, depth));
    }

    createAndFillWithContent(content, index, templateDocument, indexes, depth) {
        var clone = templateDocument.content.cloneNode(true);
        let indexes_clone = [...indexes];
        indexes_clone.push(index);
        this.fillTemplateWithValues(clone, content, depth, indexes_clone);
        this.action(this.contents, indexes_clone, clone);
        return clone;
    }
    
    createDocument(html)
    {
        let templateDocument = document.implementation.createHTMLDocument("templateDocument");
        templateDocument.body.innerHTML = html;
        return templateDocument.getElementsByTagName("template")[0];
    }
    
    fillTemplateWithValues(clone, content, depth, indexes, i)
    {
        let links = this.domElementIds[depth];
        // se c'è un link con chiave null, passo tutto l'oggetto nel formatter
        if (links[null]) 
            this.setValueToDomElement(clone.getElementById(links[null].id), content, links[null].formatter);
        else
            Object.keys(content).forEach(key => {
                if (links[key]) {
                    if (this.isSubTemplate(key)) this._loadTemplate(clone, content[key], depth + 1, indexes, i);
                    else this.setValueToDomElement(clone.getElementById(links[key].id), content[key], links[key].formatter);
                }
            });
    }

    isSubTemplate(contentKey) {
        for (let i in this.contentElementToExpand)
            if (contentKey == this.contentElementToExpand[i]) return true;
        return false;
    }
    
    setValueToDomElement(element, value, formatter)
    {
        if (!element) return;
        if (formatter) value = formatter(value);

        if (element instanceof HTMLImageElement) element.src = value;
        else if (element instanceof HTMLButtonElement) element.innerText = value;
        else element.innerHTML = value;
    }
}