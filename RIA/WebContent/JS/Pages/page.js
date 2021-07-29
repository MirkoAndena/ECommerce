var Page = new function(name, htmlObject) {
    this.name = name;
    this.htmlObject = htmlObject;

    show = function() {
        document.getElementById(this.htmlObject).style.display = 'block';
    }

    hide = function() {
        document.getElementById(this.htmlObject).style.display = 'none';
    }

    load = function(data) {
        // specified on each page
    }
}