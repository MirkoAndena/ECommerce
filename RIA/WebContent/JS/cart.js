class Purchase
{
    constructor(seller, article, quantity, price)
    {
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
        return seller == this.seller && article == this.article && price == this.price;
    }

    toString() {
        return `${this.article.name} ${round(this.price)} € per ${this.quantity} = ${round(this.price * this.quantity)} €`;
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
            let purchase = new Purchase(seller, article, quantity, price);
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
        else sellerCart.purchases.push(purchaseToAdd);
        sellerCart.price.total += purchaseToAdd.quantity * purchaseToAdd.price;
    }

    updateShipmentPrice(sellerCart) {
        // TODO
        sellerCart.price.shipment = 9.99;
    }
}