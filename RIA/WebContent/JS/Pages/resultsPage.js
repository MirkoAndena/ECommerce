class ResultsPage extends Page
{
    constructor(name, htmlObject)
    {
        super(name, htmlObject);
    }

    load(data)
    {
        let searched = document.getElementById('searchField').value;
        document.getElementById('resultText').innerHTML = data.articles.length ? `Risultati per '${searched}'` : 'Nessun articolo trovato';
        if (!data.articles.length) document.getElementById('resultsContainer').innerHTML = '';
        document.getElementById('selectedContainer').innerHTML = '';
    
        let foundLinks = {
            'id': {'id': 'articleId'},
            'name': {'id': 'buttonSelect'},
            'minPrice': {'id': 'articlePrice'},
            'sellerCount': {'id': 'sellerCount'}
        };

        let selectArticle = function(content, indexes, node) {
            let entry = content[indexes[0]];
            node.getElementById('buttonSelect').onclick = () => {
                HTTP.get('Search', {selected_article: entry.id}, data => { 
                    
                    // Remove current content
                    document.getElementById('selectedContainer').innerHTML = '';

                    // Showing article with sellers
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
                        let sellerIndex = indexes[1];
                
                        let article = content;
                        let seller = content.sellers[sellerIndex];
                        let price = content.sellers[sellerIndex].price;
                         
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
                    
                    let selectedTemplateManager = new TemplateManager();
                    selectedTemplateManager.templates = [ data.articleTemplate, data.sellerTemplate];
                    selectedTemplateManager.domElementIds = [ articleLinks, sellerLinks];
                    selectedTemplateManager.containers = ['selectedContainer', 'sellerContainer'];
                    selectedTemplateManager.contents = data.selected;
                    selectedTemplateManager.action = initButtonClick;
                    selectedTemplateManager.loadTemplate();

                });
            };
        }

        // List articles template manager
        let templateManager = new TemplateManager();
        templateManager.templates = [ data.articleItemListTemplate ];
        templateManager.domElementIds = [ foundLinks ];
        templateManager.containers = ['resultsContainer'];
        templateManager.contents = data.articles;
        templateManager.action = selectArticle;
        templateManager.loadTemplate();
    }
}