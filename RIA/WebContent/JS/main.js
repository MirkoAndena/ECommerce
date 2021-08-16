window.onload = main;

var website = new WebSite();
var cart = undefined;

function main() {

    cart = getCartOrCreate();

    // Load Home
    if (website.currentState == undefined)
        website.requestForPage('Home');

    // Links
    document.getElementById('homelink').addEventListener('click', () => { website.requestForPage('Home'); });
    document.getElementById('cartlink').addEventListener('click', () => { website.requestForPage('Cart'); });
    document.getElementById('orderlink').addEventListener('click', () => { website.requestForPage('Orders'); });

    // Search    
    document.getElementById("btnSearch").addEventListener('click', () => { search(); });
    document.getElementById('searchField').addEventListener('onkeydown', e => { if (e.key == 'Enter') { search(); e.preventDefault(); } });
}

function search() {
    let searched = document.getElementById('searchField').value;
    website.requestForPage('Search', {search_string: searched});
}

function getCartOrCreate() {
    return window.sessionStorage.getItem('cart') ?? new Cart();
}