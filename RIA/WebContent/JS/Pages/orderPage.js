class OrderPage extends Page
{
    constructor(name, htmlObject)
    {
        super(name, htmlObject);
    }

    load(data)
    {
        if (data.orders.length > 0) {
            document.getElementById('tableHeaders').style.display = 'block';
            document.getElementById('noOrdersMessage').style.display = 'none';
        } else {
            document.getElementById('tableHeaders').style.display = 'none';
            document.getElementById('noOrdersMessage').style.display = 'block';
        }

        document.getElementById('ordersContainer').innerHTML = '';

        // Sistemazione campi dell oggetto per template
        data.orders.forEach(order => {
            order.purchases.forEach(purchase => {
                purchase.id = purchase.article.id;
                purchase.name = purchase.article.name;
                purchase.description = purchase.article.description;
                purchase.image = purchase.article.image;
                purchase.category = purchase.article.category;
                purchase.totalPrice = purchase.price * purchase.quantity;
                delete purchase.article;
            });
        });
    
        let orderLinks = {
            'id': {'id': 'orderId'},
            'userAddress': {'id': 'orderUserAddress'},
            'shipmentDate': {'id': 'orderShipmentDate'},
            'seller': {'id': 'orderSeller'},
            'total': {'id': 'orderTotal'},
            'purchases': {'id': 'orderPurchasesContainer'}
        };
    
        let purchaseLinks = {
            'id': {'id': 'articleId'},
            'name': {'id': 'articleName'},
            'description': {'id': 'articleDescription'},
            'image': {'id': 'articleImage'},
            'category': {'id': 'articleCategory'},
            'totalPrice': {'id': 'purchasePrice', 'formatter': super.priceFormatter}
        };
    
        // Run template with values
        let templateManager = new TemplateManager();
        templateManager.templates = [ data.ordersTemplate, data.order_purchase_template];
        templateManager.domElementIds = [ orderLinks, purchaseLinks];
        templateManager.containers = ['ordersContainer', 'orderPurchasesContainer'];
        templateManager.contents = data.orders;
        templateManager.contentElementToExpand = ['purchases'];
        templateManager.loadTemplate();
    }
}