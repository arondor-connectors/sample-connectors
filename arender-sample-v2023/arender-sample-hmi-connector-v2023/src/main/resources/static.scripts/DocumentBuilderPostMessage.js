function arenderjs_init(arenderjs_)
{
    arenderjs_.registerAllAsyncModulesStartedEvent(function() {
        arenderjs_.documentBuilder.registerNotifyAlterDocumentContentEvent(function (obj) {
            armt_onNotifyAlterDocumentContentEvent(arenderjs_, obj);
        });
    });
}

var docId;
var documentTitle;

function armt_onNotifyAlterDocumentContentEvent(arenderjs_,obj)
{
    docId = arenderjs_.documentBuilder.getResultDocumentId(obj);
    console.log("Notify : docId = " + docId);
    arenderjs_.getDocumentLayout().getDocumentLayout(docId, function(layout) {
        documentTitle = layout.getChildren()[0].getDocumentTitle();
        console.log("Notify : documentTitle = " + documentTitle);
        docId = layout.getChildren()[0].getDocumentId();
        popupDocumentBuilderGed();
    });
}

function popupDocumentBuilderGed() {
    console.log("docId is: " + docId);
    if (docId) {
        console.log("Sending PostMessage: indexDocument= " + docId + "&documentTitle=" + documentTitle);
        sendPostMessage("indexDocument="+docId+"&documentTitle="+documentTitle);
    }
    // popup("popupDocumentBuilder");
}

function sendPostMessage(data) {
    if (window.opener)
    {
        console.log("Entering window.opener");
        window.opener.top.postMessage(data, "*");
    }
    else
    {
        console.log("Entering window.opener else");
        window.parent.postMessage(data, "*");
    }

}
