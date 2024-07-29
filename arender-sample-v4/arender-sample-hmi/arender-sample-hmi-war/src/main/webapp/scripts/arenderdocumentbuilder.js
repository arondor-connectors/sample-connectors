/**
 * Register callback methods from the ARender API
 */
function arenderjs_init(arenderjs_)
{
    /**
     * Callback triggered when all ARender asynchronous modules are loaded
     */
	arenderjs_.registerAllAsyncModulesStartedEvent(function(){
		/**
		 * Callback triggered when a document is saved
		 */
		arenderjs_.documentBuilder.registerSubmitAlterDocumentContentEvent(function(obj){armt_onSubmitAlterDocumentContentEvent(arenderjs_,obj);});
        arenderjs_.documentBuilder.registerNotifyAlterDocumentContentEvent(function(obj){armt_onNotifyAlterDocumentContentEvent(arenderjs_,obj);});
	});
}

function armt_onSubmitAlterDocumentContentEvent(arenderjs_,obj)
{
    var desc = arenderjs_.documentBuilder.getSubmittedAlterDocumentContentDescription(obj);
    var meta = arenderjs_.documentBuilder.getDocumentMetadata(desc, 0);
    arenderjs_.documentBuilder.addDocumentMetadata(meta, "name", "value");
}

function armt_onNotifyAlterDocumentContentEvent(arenderjs_,obj)
{
    alert("Notify : " + obj);
    var docId = arenderjs_.documentBuilder.getResultDocumentId(obj);
    alert("Notify : " + docId);
    console.log("Notify : docId = " + docId);
}