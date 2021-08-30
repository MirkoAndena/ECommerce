class ResultsPage extends Page
{
    constructor(name, htmlObject)
    {
        super(name, htmlObject);
    }

    load(data)
    {
        let searchField = document.getElementById('searchField');
        document.getElementById('resultText').innerHTML = data.articles.length ? `Risultati per '${searchField.value}'` : 'Nessun articolo trovato';
        searchField.value = '';

        // Rimozione contenuto precedente
        document.getElementById('resultsContainer').innerHTML = '';
    
        let foundLinks = {
            'id': {'id': 'articleId'},
            'name': {'id': 'buttonSelect'},
            'minPrice': {'id': 'articlePrice', 'formatter': super.priceFormatter },
            'sellerCount': {'id': 'sellerCount'}
        };

        let priceFormatter = super.priceFormatter;
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
                        'freeShippingThreshold': {'id': 'sellerFreeShippingThreshold', 'formatter': value => value == 0 ? 'gratuita' : `gratuita con spesa superiore a ${priceFormatter(value)}`},
                        'shipmentRangesStringValue': {'id': 'sellerShipmentRanges'},
                        'price': {'id': 'sellerPrice', 'formatter': priceFormatter},
                        'articlesAddedToCart': {'id': 'sellerArticlesAddedToCart'},
                        'totalValue': {'id': 'sellerTotalValue', 'formatter': priceFormatter}
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

                        // Init poppers with cart articles
                        initPopper(node, seller);
            
                        // Define add to cart button click action
                        node.getElementById('addToCart').onclick = () => {
                            let quantity = parseInt(document.getElementById(`quantity${seller.id}${article.id}`).value);
                            cart.add(seller, article, quantity, price);
            
                            // Update articles added to cart of every card of this seller
                            updateElementsWithCartItemsOfSeller(seller, sellerLinks);

                            website.requestForPage('Cart');
                        };
                    }
                    
                    let selectedTemplateManager = new TemplateManager();
                    selectedTemplateManager.templates = [ data.articleTemplate, data.sellerTemplate];
                    selectedTemplateManager.domElementIds = [ articleLinks, sellerLinks];
                    selectedTemplateManager.containers = ['selectedContainer', 'sellerContainer'];
                    selectedTemplateManager.contents = data.selected;
                    selectedTemplateManager.contentElementToExpand = ['sellers'];
                    selectedTemplateManager.action = initButtonClick;
                    selectedTemplateManager.loadTemplate();

                    updateElementsWithCartItems(sellerLinks);
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