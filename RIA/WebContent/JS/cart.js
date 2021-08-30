class Purchase
{
    constructor(id, seller, article, quantity, price)
    {
        this.id = id;
        this.seller = seller;
        this.article = article;
        this.quantity = quantity;
        this.price = price;
    }

    add(quantity)
    {
        this.quantity += quantity;
    }

    equals(seller, article, price)
    {
        return seller.id == this.seller.id && article.id == this.article.id && price == this.price;
    }

    toString() {
        return `${this.article.name} ${this.price.toFixed(2)} € per ${this.quantity} = ${(this.price * this.quantity).toFixed(2)} €`;
    }
}

class Cart
{
    constructor(purchases)
    {
        this.purchases = [];
        for (let i in purchases)
            this.purchases.push(Object.assign(new Purchase(), purchases[i]));
    }

    static getSessionCartOrCreate() {
        let saved = window.sessionStorage.getItem('cart');
        return new Cart(saved && JSON.parse(saved));
    }
    
    getNewIndex() {
    	let index = Number(window.sessionStorage.getItem('cart_index')) || 0;
    	window.sessionStorage.setItem('cart_index', index + 1);
    	return index;
    }

    add(seller, article, quantity, price)
    {
        let found = undefined;
        this.purchases.forEach(p => 
        {
            if (p.equals(seller, article, price))
                found = p;
        });

        if (found) found.add(quantity);
        else
        {
            let purchase = new Purchase(this.getNewIndex(), seller, article, quantity, price);
            this.purchases.push(purchase);
        }

        // Save in session storage
        window.sessionStorage.setItem('cart', JSON.stringify(this.purchases));
    }

    // Return list of articles
    getCountOf(seller)
    {
        let articles = this.purchases.filter(purchase => purchase.seller.id === seller.id);
        return articles.reduce((accumulator, currentValue) => accumulator + currentValue.quantity, 0);
    }

    getTotalPriceOf(seller)
    {
        let articles = this.purchases.filter(purchase => purchase.seller.id === seller.id);
        return articles.reduce((accumulator, currentValue) => accumulator + currentValue.quantity * currentValue.price, 0);
    }

    getArticlesOfSellerWithQuantity(seller) 
    {
        return this.purchases
        .filter(purchase => purchase.seller.id == seller.id)
        .map(purchase => `${purchase.article.name} x${purchase.quantity}`);
    }

    // Can return null
    getSellerCarts() {
        if (this.purchases.length == 0) return null;

        let sellerCarts = [];
        this.purchases.forEach(purchase => this.addOrCreateSellerCart(sellerCarts, purchase));
        sellerCarts.forEach(sellerCart => this.updateShipmentPrice(sellerCart));

        return sellerCarts;
    }

    addOrCreateSellerCart(sellerCarts, purchaseToAdd) {
        
        // Search for sellerCart of this seller        
        let sellerCart = sellerCarts.find(cart => cart.seller == purchaseToAdd.seller.name);

        if (!sellerCart) {
            sellerCart = {'seller': purchaseToAdd.seller.name, 'price': {total: 0, shipment: 0}, 'purchases': []};
            sellerCarts.push(sellerCart);
        }

        // Check if there is another purchase of the same article
        let purchase = sellerCart.purchases.find(element => element.article.id === purchaseToAdd.article.id && element.price === purchaseToAdd.price);
        if (purchase) purchase.quantity += purchaseToAdd.quantity;
        else sellerCart.purchases.push(Object.assign(Object.create(Object.getPrototypeOf(purchaseToAdd)), purchaseToAdd));
        sellerCart.price.total += purchaseToAdd.quantity * purchaseToAdd.price;
    }

    updateShipmentPrice(sellerCart) {
        if (this.isShipmentFree(sellerCart)) return;
        sellerCart.price.shipment = this.shipmentPriceFromRanges(sellerCart);
    }

    isShipmentFree(sellerCart) {
        let total = sellerCart.price.total;
        let freeShippingThreshold = sellerCart.purchases[0].seller.freeShippingThreshold;
        return freeShippingThreshold == 0 || total >= freeShippingThreshold;
    }

    shipmentPriceFromRanges(sellerCart) {
        let articleCount = sellerCart.purchases.reduce((sum, value) => sum += value.quantity, 0);
        let ranges = sellerCart.purchases[0].seller.shipmentRanges;

        let endPrice = undefined;
        for (let i in ranges) {
            if (ranges[i].end == null) endPrice = ranges[i].price;
            else if (articleCount > ranges[i].start && articleCount <= ranges[i].end) return ranges[i].price;
        }
        return endPrice;
    }

    remove(sellerCart) {
        let ids = sellerCart.purchases.map(purchase => purchase.id);
        this.purchases = this.purchases.filter(purchase => purchase.id != ids.find(id => id == purchase.id));

        // Save in session storage
        window.sessionStorage.setItem('cart', JSON.stringify(this.purchases));
    }
}