class WebSite
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
        httpGetRequest(page, null, result => this.handleResponse(result));
    }
    
    handleResponse(result)
    {
        this.currentState = result.page
        let page = this.pages.filter(page => page.name == requestedPage);
        this.enablePage(page);
        page.load(result);
    }
    
    enablePage(requestedPage)
    {
        this.pages.forEach(page => {
            if (page === requestedPage) page.show();
            else page.hide();
        });
    }
}