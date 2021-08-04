class Home extends Page
{
    constructor(name, htmlObject)
    {
        super(name, htmlObject);
    }

    load(data)
    {
        let articleLinks = {
            'id': {'id': 'articleId'},
            'name': {'id': 'articleName'},
            'description': {'id': 'articleDescription'},
            'image': {'id': 'articleImage'},
            'category': {'id': 'articleCategory'},
            'sellers': {'id': 'sellerContainer'}
        };
    
        let sellerLinks = {
            'id': {'id': 'sellerId'},
            'name': {'id': 'sellerName'},
            'rating': {'id': 'sellerRating', 'formatter': value => value + " su 5"},
            'freeShippingThreshold': {'id': 'sellerFreeShippingThreshold', 'formatter': value => value == 0 ? 'gratuita' : 'gratuita con spesa superiore a ' + value.toFixed(2) + ' €'},
            'shipmentRanges': {'id': 'sellerShipmentRanges'},
            'price': {'id': 'sellerPrice', 'formatter': value => value.toFixed(2) + " €"},
            'articlesAddedToCart': {'id': 'sellerArticlesAddedToCart'},
            'totalValue': {'id': 'sellerTotalValue', 'formatter': value => value.toFixed(2) + " €"}
        };
    
        let initButtonClick = function(content, indexes, node) {
            if (indexes.length != 2) return;
    
            let article = content[indexes[0]].id;
            let seller = content[indexes[0]].sellers[indexes[1]].id;
            let price = content[indexes[0]].sellers[indexes[1]].price;
             
            // Rename fields
            node.getElementById('quantity').id = `quantity${seller}${article}`;
            node.getElementById(sellerLinks['articlesAddedToCart'].id).id = `articlesAddedToCart_${seller}_${article}`;
            node.getElementById(sellerLinks['totalValue'].id).id = `totalValue_${seller}_${article}`;

            // Define click action
            node.getElementById('addToCart').onclick = () => {
                let quantity = parseInt(document.getElementById(`quantity${seller}${article}`).value);
                cart.add(seller, article, quantity, price);

                // Upload articles added to cart of every card of this seller
                let articlesAddedToCartQuery = `[id^='articlesAddedToCart_${seller}']`;
                let articlesAddedToCartElements = document.querySelectorAll(articlesAddedToCartQuery);
                articlesAddedToCartElements.forEach(element => element.innerHTML = cart.getCountOf(seller));

                let totalValueQuery = `[id^='totalValue_${seller}']`;
                let totalValueElements = document.querySelectorAll(totalValueQuery);
                totalValueElements.forEach(element => element.innerHTML = sellerLinks['totalValue'].formatter(cart.getTotalPriceOf(seller)));
            };
        }
    
        let templateManager = new TemplateManager();
        templateManager.templates = [ data['articleTemplate'], data['sellerTemplate']];
        templateManager.domElementIds = [ articleLinks, sellerLinks];
        templateManager.containers = ['homeContainer', 'sellerContainer'];
        templateManager.contents = data['content']
        templateManager.action = initButtonClick;
        templateManager.buildPage();
    }
}