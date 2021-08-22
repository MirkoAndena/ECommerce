class WebSite
{
    constructor()
    {
        this.currentState = undefined;
        this.pages = this.createPages();
    }

    createPages()
    {
        return [
            new HomePage('Home', 'HomePage'),
            new ResultsPage('Search', 'ResultPage'),
            new CartPage('Cart', 'CartPage'),
            new OrderPage('Order', 'OrderPage')
        ];
    }

    requestForPage(page, data)
    {
        HTTP.get(page, data, result => this.handleResponse(page, result));
    }
    
    handleResponse(requestedPage, result)
    {
        this.currentState = requestedPage
        let matches = this.pages.filter(page => page.name == requestedPage);
        if (matches.length > 0) {
            let page = matches[0];
            this.enablePage(page);
            page.load(result);
        }
    }
    
    enablePage(requestedPage)
    {
        this.pages.forEach(page => {
            if (page === requestedPage) page.show();
            else page.hide();
        });
    }
}