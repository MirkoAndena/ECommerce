window.onload = main;

var website = new WebSite();
var cart = new Cart();

function main() {

    // Load Home
    if (website.currentState == undefined)
        website.requestForPage('Home');

    document.getElementById("btnSearch").addEventListener('click', () => { search(); });
    document.getElementById('searchField').onkeydown = e => { if (e.key == 'Enter') { search(); e.preventDefault(); } };
}

function search() {
    let searched = document.getElementById('searchField').value;
    website.requestForPage('Search', {search_string: searched});
}