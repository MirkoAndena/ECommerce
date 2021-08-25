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