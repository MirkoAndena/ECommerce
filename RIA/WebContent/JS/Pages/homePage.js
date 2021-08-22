class HomePage extends Page
{
    constructor(name, htmlObject)
    {
        super(name, htmlObject);
    }

    load(data)
    {
        document.getElementById('homeContainer').innerHTML = '';

        /*
        Link:
            id: id del componente html in cui inserire il valore (div)
            formatter: funzione che modifica il valore da visualizzare ad esempio aggiungendo il simbolo del euro
        */

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
            'shipmentRangesStringValue': {'id': 'sellerShipmentRanges'},
            'price': {'id': 'sellerPrice', 'formatter': super.priceFormatter},
            'articlesAddedToCart': {'id': 'sellerArticlesAddedToCart'},
            'totalValue': {'id': 'sellerTotalValue', 'formatter': super.priceFormatter}
        };
    
        let initButtonClick = function(content, indexes, node) {
            if (indexes.length != 2) return;
            let articleIndex = indexes[0];
            let sellerIndex = indexes[1];
    
            let article = content[articleIndex];
            let seller = content[articleIndex].sellers[sellerIndex];
            let price = content[articleIndex].sellers[sellerIndex].price;
             
            // Rename fields
            node.getElementById('quantity').id = `quantity${seller.id}${article.id}`;
            node.getElementById(sellerLinks['articlesAddedToCart'].id).id = `articlesAddedToCart_${seller.id}_${article.id}`;
            node.getElementById(sellerLinks['totalValue'].id).id = `totalValue_${seller.id}_${article.id}`;

            // Define add to cart button click action
            node.getElementById('addToCart').onclick = () => {
                let quantity = parseInt(document.getElementById(`quantity${seller.id}${article.id}`).value);
                cart.add(seller, article, quantity, price);

                // Upload articles added to cart of every card of this seller
                let articlesAddedToCartQuery = `[id^='articlesAddedToCart_${seller.id}']`;
                let articlesAddedToCartElements = document.querySelectorAll(articlesAddedToCartQuery);
                articlesAddedToCartElements.forEach(element => element.innerHTML = cart.getCountOf(seller));

                let totalValueQuery = `[id^='totalValue_${seller.id}']`;
                let totalValueElements = document.querySelectorAll(totalValueQuery);
                totalValueElements.forEach(element => element.innerHTML = sellerLinks['totalValue'].formatter(cart.getTotalPriceOf(seller)));

                website.requestForPage('Cart');
            };
        }
    
        // Run template with values
        let templateManager = new TemplateManager();
        templateManager.templates = [ data.articleTemplate, data.sellerTemplate];
        templateManager.domElementIds = [ articleLinks, sellerLinks];
        templateManager.containers = ['homeContainer', 'sellerContainer'];
        templateManager.contents = data.content;
        templateManager.contentElementToExpand = ['sellers'];
        templateManager.action = initButtonClick;
        templateManager.loadTemplate();
    }
}