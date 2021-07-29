var PageHandler = function() {
    this.currentState = undefined;
    this.pages = createPages();

    createPages = function() {
        var home = new Home('Home', 'HomePage');
        return [ home ]; 
    }

    this.requestForPage = function(page) {
        httpRequest("GET", page, null, result => this.handleResponse(result));
    }

    this.handleResponse = function(result) {
        this.currentState = result['page'];
        let page = this.enablePage(this.currentState);
        page.load(result);
    }

    this.enablePage = function(pageToEnable) {
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