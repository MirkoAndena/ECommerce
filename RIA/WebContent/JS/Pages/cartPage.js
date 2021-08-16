class CartPage extends Page
{
    constructor(name, htmlObject)
    {
        super(name, htmlObject);
    }

    load(data)
    {
        // value => 0: totale carrello, 1: spese di spedizione
        let sellerCartLinks = {
            'name': {'id': 'sellerName'},
            'price': {'id': 'cartPrice', 'formatter': value => `Totale: ${value[0] + value[1]} € (${value[0]} € + spedizione ${value[1]} €)` },
            'purchases': {'id': 'purchaseContainer'}
        };
    
        // value => 0: nome articolo, 1: prezzo articolo, 2: quantità
        let purchaseLinks = {
            'purchase': {'id': 'purchase', 'formatter': value => `${value[0]} ${value[1]} € per ${value[2]} = ${value[1] * value[2]} €`}
        };
    
        let initButtonClick = function(content, indexes, node) {
            
        }
    
        // Run template with values
        let templateManager = new TemplateManager();
        templateManager.templates = [ data.cartTemplate, data.purchaseTemplate];
        templateManager.domElementIds = [ sellerCartLinks, purchaseLinks];
        templateManager.containers = ['sellerCartContainer', 'purchaseContainer'];
        templateManager.contents = cart.getSellerCarts();
        templateManager.action = initButtonClick;
        templateManager.loadTemplate();
    }
}