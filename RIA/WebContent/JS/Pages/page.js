class Page
{
    constructor(name, htmlObject)
    {
        this.name = name;
        this.htmlObject = htmlObject;
    }

    show()
    {
        document.getElementById(this.htmlObject).style.display = 'block';
    }

    hide()
    {
        document.getElementById(this.htmlObject).style.display = 'none';
    }

    load(data)
    {
        // specified on each page
    }
}