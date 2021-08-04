window.onload = main;

var pageHandler = new PageHandler();
var cart = new Cart();

function main() {
    if (pageHandler.currentState == undefined)
        pageHandler.requestForPage('Home');
}