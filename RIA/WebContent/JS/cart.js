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
}

class Cart
{
    constructor()
    {
        this.purchases = [];
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
        window.sessionStorage.setItem('cart', this);
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
        for (let purchase in this.purchases)
            this.addOrCreateSellerCart(sellerCarts, purchase);

        for (let sellerCart in sellerCarts)
            this.updateShipmentPrice(sellerCart);

        return sellerCarts;
    }

    addOrCreateSellerCart(sellerCarts, purchase) {
        
        // Search for sellerCart of this seller
        let sellerCart = null;
        for (let cart in sellerCarts)
            if (cart.seller === purchase.seller) {
                sellerCart = cart;
                break;
            }

        if (!sellerCart)
            sellerCarts.push({'name': purchase.seller.name, 'price': [0, 0], 'purchases': []});
        sellerCart.purchases.push({'purchase': purchase});
        sellerCart.price[0] += purchase.quantity * purchase.price;
    }

    updateShipmentPrice(sellerCart) {
        // TODO
    }
}