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
}

function search() {
    let searched = document.getElementById('searchField').value;
    website.requestForPage('Search', {'search_string': searched});
}