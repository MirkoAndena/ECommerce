class CartPage extends Page
{
    constructor(name, htmlObject)
    {
        super(name, htmlObject);
    }

    load(data)
    {
        let sellersCarts = cart.getSellerCarts();
        if (sellersCarts.length > 0) document.getElementById('cartMessage').style.display = 'none';
        document.getElementById('sellerCartContainer').innerHTML = '';

        let round = function(value) {
            return Math.round((value + Number.EPSILON) * 100) / 100;
        }
        
        // value => 0: totale carrello, 1: spese di spedizione
        let sellerCartLinks = {
            'seller': {'id': 'sellerName'},
            'price': {'id': 'cartPrice', 'formatter': value => `Totale: ${round(value.total) + round(value.shipment)} € (${round(value.total)} € + spedizione ${round(value.shipment)} €)` },
            'purchases': {'id': 'purchaseContainer'}
        };
    
        // value => 0: nome articolo, 1: prezzo articolo, 2: quantità
        let purchaseLinks = {
            null: {'id': 'purchase', 'formatter': value => value.toString()}
        };
    
        let initButtonClick = function(content, indexes, node) {
            
        }
    
        // Run template with values
        let templateManager = new TemplateManager();
        templateManager.templates = [ data.cartTemplate, data.purchaseTemplate];
        templateManager.domElementIds = [ sellerCartLinks, purchaseLinks];
        templateManager.containers = ['sellerCartContainer', 'purchaseContainer'];
        templateManager.contents = sellersCarts;
        templateManager.action = initButtonClick;
        templateManager.loadTemplate();
    }
}