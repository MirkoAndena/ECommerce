class Results extends Page
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
                    console.log(data);
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