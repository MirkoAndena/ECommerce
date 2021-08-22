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
        
        // value => 0: totale carrello, 1: spese di spedizione
        let sellerCartLinks = {
            'seller': {'id': 'sellerName'},
            'price': {'id': 'cartPrice', 'formatter': value => `Totale: ${super.priceFormatter(value.total + value.shipment)} (${super.priceFormatter(value.total)} + spedizione ${super.priceFormatter(value.shipment)}` },
            'purchases': {'id': 'purchaseContainer'}
        };
    
        // value => 0: nome articolo, 1: prezzo articolo, 2: quantità
        let purchaseLinks = {
            null: {'id': 'purchase', 'formatter': value => value.toString()}
        };
    
        let initButtonClick = function(content, indexes, node) {
            // TODO invio ordine
        }
    
        // Run template with values
        let templateManager = new TemplateManager();
        templateManager.templates = [ data.cartTemplate, data.purchaseTemplate];
        templateManager.domElementIds = [ sellerCartLinks, purchaseLinks];
        templateManager.containers = ['sellerCartContainer', 'purchaseContainer'];
        templateManager.contents = sellersCarts;
        templateManager.contentElementToExpand = ['purchases'];
        templateManager.action = initButtonClick;
        templateManager.loadTemplate();
    }
}