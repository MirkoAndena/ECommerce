window.onload = main;

var website = new WebSite();
var cart = undefined;

function main() {

    cart = Cart.getSessionCartOrCreate();

    // Load Home
    if (website.currentState == undefined)
        website.requestForPage('Home');

    // Links
    document.getElementById('homelink').addEventListener('click', () => { website.requestForPage('Home'); });
    document.getElementById('cartlink').addEventListener('click', () => { website.requestForPage('Cart'); });
    document.getElementById('orderlink').addEventListener('click', () => { website.requestForPage('Order'); });

    // Search    
    document.getElementById("btnSearch").addEventListener('click', () => { search(); });
    document.getElementById('searchField').addEventListener('keydown', e => { if (e.key == 'Enter') { e.preventDefault(); search(); } });

    // Abilitazione del popper tramite JQuery
    $('[data-toggle="popover"').popover({container: 'body'});
    $('[data-toggle="popover"').mouseover(() => $('[data-toggle="popover"').popover('show'));
    $('[data-toggle="popover"').mouseout(() => $('[data-toggle="popover"').popover('hide'));
}

function search() {
    let searched = document.getElementById('searchField').value;
    website.requestForPage('Search', {'search_string': searched});
}