window.onload = main;

var pageHandler = new PageHandler();

function main() {
    if (pageHandler.currentState == undefined)
        pageHandler.requestForPage('Home');
}