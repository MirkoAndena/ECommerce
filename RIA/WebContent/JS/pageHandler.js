class PageHandler
{
    constructor()
    {
        this.currentState = undefined;
        this.pages = this.createPages();
    }

    createPages()
    {
        var home = new Home('Home', 'HomePage');
        return [ home ]; 
    }

    requestForPage(page)
    {
        httpRequest("GET", page, null, result => this.handleResponse(result));
    }
    
    handleResponse(result)
    {
        this.currentState = result['page'];
        let page = this.enablePage(this.currentState);
        page.load(result);
    }
    
    enablePage(pageToEnable)
    {
        let found = null;
        this.pages.forEach(page => {
            if (page.name == pageToEnable) {
                found = page;
                page.show();
            }
            else page.hide();
        });
        return found;
    }
}