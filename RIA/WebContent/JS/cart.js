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
    }

    // Return list of articles
    getCountOf(seller)
    {
        let articles = this.purchases.filter(purchase => purchase.seller === seller);
        return articles.reduce((accumulator, currentValue) => accumulator + currentValue.quantity, 0);
    }

    getTotalPriceOf(seller)
    {
        let articles = this.purchases.filter(purchase => purchase.seller === seller);
        return articles.reduce((accumulator, currentValue) => accumulator + currentValue.quantity * currentValue.price, 0);
    }
}