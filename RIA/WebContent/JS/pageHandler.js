var PageHandler = function() {
    this.currentState = undefined;
    this.pages = {'Home': 'HomePage', 'Cart': 'CartPage', 'Search': 'ResultPage', 'Order': 'OrderPage'};

    this.requestForPage = function(page) {
        httpRequest("GET", page, null, result => this.handleResponse(result));
    }

    this.handleResponse = function(result) {
        this.currentState = result['page'];
        this.enablePage(this.currentState);
        this.compilePageWithResults(result);
    }

    this.enablePage = function(page) {
        Object.keys(this.pages).forEach(key => {
            if (key == page) document.getElementById(this.pages[key]).style.display = 'block';
            else document.getElementById(this.pages[key]).style.display = 'none';
        });
    }

    this.compilePageWithResults = function(results) {
        switch(this.currentState) {
            case 'Home':
                this.compileHome(results);
                break;
        }
    }

    this.compileHome = function(results) {

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
            'freeShippingThreshold': {'id': 'sellerFreeShippingThreshold', 'formatter': value => value == 0 ? 'gratuita' : 'gratuita con spesa superiore a ' + value + ' €'},
            'shipmentRanges': {'id': 'sellerShipmentRanges'},
            'price': {'id': 'sellerPrice', 'formatter': value => value + " €"},
            'articlesAddedToCart': {'id': 'sellerArticlesAddedToCart'},
            'totalValue': {'id': 'sellerTotalValue', 'formatter': value => value + " €"}
        };

        let initButtonClick = function(content, indexes, node) {
            if (indexes.length != 2) return;

            let params = {};
            params['article'] = content[indexes[0]].id;
            params['seller'] = content[indexes[0]].sellers[indexes[1]].id;
            params['price'] = content[indexes[0]].sellers[indexes[1]].price;
            
            node.getElementById('quantity').id = 'quantity' + params['article'] + params['seller'];
            node.getElementById('addToCart').onclick = () => {
                params['quantity'] = parseInt(document.getElementById('quantity' + params['article'] + params['seller']).value);
                httpPostRequest('CartInsert', params, res => { 
                    debugger;
                    console.log(res);
                });
            };
        }

        let templateManager = new TemplateManager();
        templateManager.templates = [ results['articleTemplate'], results['sellerTemplate']];
        templateManager.domElementIds = [ articleLinks, sellerLinks];
        templateManager.containers = ['homeContainer', 'sellerContainer'];
        templateManager.contents = results['content']
        templateManager.action = initButtonClick;
        templateManager.buildPage();
    }
}