var updateElementsWithCartItems = function(sellerLinks) {
    let sellers = cart.purchases.map(purchase => purchase.seller.id);
    let uniqueSellers = sellers.filter((value, index, self) => self.indexOf(value) == index);
    uniqueSellers.forEach(seller => updateElementsWithCartItemsOfSeller({id: seller}, sellerLinks));
}

/*
    Aggiornamento dei field "articlesAddedToCart" (numero di articoli nel carrello di uno specifico seller) e
    "totalValue" (totale prezzo degli articoli nel carrello di uno specifico seller)
*/
var updateElementsWithCartItemsOfSeller = function(seller, sellerLinks) {
    updateElements(`[id^='articlesAddedToCart_${seller.id}']`, cart.getCountOf(seller));
    updateElements(`[id^='totalValue_${seller.id}']`, sellerLinks['totalValue'].formatter(cart.getTotalPriceOf(seller)));
}

var updateElements = function(query, value) {
    let elements = document.querySelectorAll(query);
    elements.forEach(element => element.innerHTML = value);
}

var initPopper = function(node, seller, articleTemplate) {
    let articles = cart.getArticlesOfSellerWithQuantity(seller);
    if (articles.length == 0) return;

    let articleLinks = {
        null: {'id': 'articleName'}
    };

    let templateManager = new TemplateManager();
    templateManager.templates = [ articleTemplate ];
    templateManager.domElementIds = [ articleLinks ];
    templateManager.contents = articles;
    let contentHtml = templateManager.createTemplate();

    let popoverTrigger = node.querySelector('[data-bs-toggle="popover"]');
    let popover = new bootstrap.Popover(popoverTrigger, { html: true, content: contentHtml});
    popoverTrigger.onmouseover = () => popover.show();
    popoverTrigger.onmouseout = () => popover.hide();
}