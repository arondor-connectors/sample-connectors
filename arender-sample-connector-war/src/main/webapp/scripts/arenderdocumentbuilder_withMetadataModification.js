/**
 * Register callback methods from the ARender API
 */
function arenderjs_init(arenderjs_)
{
	arenderjs_.registerAllAsyncModulesStartedEvent(function(){
		/**
		 * Callback triggered when a new "DocumentBuilder" view is presented
		 */
		arenderjs_.documentBuilder.registerEditablePictreeNodeEvent(function(docId,
																			 element) {
			armt_nodeEditablePicTree(arenderjs_, docId, element);
		});

		/**
		 * Callback triggered when a document is saved
		 */
		arenderjs_.documentBuilder.registerSubmitAlterDocumentContentEvent(function(obj){armt_onSubmitAlterDocumentContentEvent(arenderjs_,obj);});
	});
}

/**
 * We need to keep an open map of values selected by the user for each document, to populate the document metadata at save time
 */
var selectedValues = [];

function armt_onSubmitAlterDocumentContentEvent(arenderjs_,obj)
{
	/**
	 * Adding some metadata prior to server call for document save
	 */
	var desc = arenderjs_.documentBuilder.getSubmittedAlterDocumentContentDescription(obj);
	var meta = arenderjs_.documentBuilder.getDocumentMetadata(desc, 0);

	/**
	 * Sample : add a property called 'EntityName' filled from a parameter entered in the ARender URL
	 */
	// arenderjs_.documentBuilder.addDocumentMetadata(meta, "EntityName", getParamValue('entityName',location.url));

	/**
	 * Identify which document is being saved
	 */
	var docId = arenderjs_.getDocumentMetadata().getDocumentMetadataValue(meta, "DocumentID");

	/**
	 * Identify which value has been selected from the dropdown
	 */
	var selectedValue = selectedValues[docId];
	if (selectedValue == "No data given") {
		alert("Please select a value in the drop-down list");
		throw new Error("No data given");
	}
	debugger;
	/**
	 * The selected value is provided to the server for document save as the property named 'Document_Types'
	 */
	arenderjs_.getDocumentMetadata().addDocumentMetadata(meta, "Document_Types", selectedValue);
}


function armt_nodeEditablePicTree(arenderjs_, docId, element) {
	var docIdString = "" + docId;

	window.requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame ||
		window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;
	window.requestAnimationFrame( function (){
		var nodeElement = element.querySelectorAll('.gwt-Document-Name');
		var selectElement = document.createElement("select"); // Create select tag
		// For some old browsers, it may be required to stop event propagation at onMouseDown event.
		/*
		 selectElement.onmousedown = function(event) {
		 stopPropagation(event);
		 return true;
		 };
		 */
		// Open the list Box
		queryKeyValuesList(selectElement, docIdString);
		nodeElement[0].parentNode.parentNode.parentNode.parentNode.parentNode.appendChild(selectElement); // Add the list Box

		selectElement.onchange = function() {
			selectedValues[docIdString] = this.options[this.selectedIndex].value;
		}
	});
}

/**
 * Query the drop-down list at server side and call for list-box population upon receive
 */
function queryKeyValuesList(selectElement, docIdString) {

	var xhr = null;

	if (window.XMLHttpRequest)

	{
		xhr = new XMLHttpRequest();

	} else if (window.ActiveXObject)

	{
		xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}

	xhr.onreadystatechange = function() {

		if (xhr.readyState == 4) {
			if (xhr.status == 200) { // if the connexion is successful
				var resultJson = eval(xhr.responseText);
				createListBox(selectElement, resultJson, docIdString);
			} else {

			}

		} else {
		}

	};
	xhr.open("GET", "customOptionsList.json", true);
	xhr.send(null);
}

/**
 * Populate the drop-down list with actual JSON results from the response.
 * Expected JSON format : [{'key':'keyOne','value':'boston'},{'key':'keyTwo','value':'NY'}]
 */
function createListBox(selectElement, resultJson, docIdString) {
	// Visit all the elements of the JsonTable
	for ( var index = 0; index < resultJson.length; index++) {
		var object = resultJson[index];
		selectElement.options[selectElement.options.length] = new Option(object.value, object.key);

		if (index == 0) {
			selectedValues[docIdString] = object.key;
		}
	}
}

/************** Misc Helper Functions **************/

/**
 * Extract parameter value from an URL
 *
 * @param string param
 * nom du param�tre dont on souhaite avoir la valeur
 * @param url
 * url dans laquel on souhaite r�cup�rer le param�tre ou rien si l'on souhaite travailler sur l'url courante
 * @return String
 * @author Labsmedia
 * @see https://protect-us.mimecast.com/s/7GXKBpsEQlHn?domain=labsmedia.com
 * @licence GPL
 */
function getParamValue(param,url)
{
	var u = url == undefined ? document.location.href : url;
	var reg = new RegExp('(\\?|&|^)'+param+'=(.*?)(&|$)');
	matches = u.match(reg);
	return matches[2] != undefined ? decodeURIComponent(matches[2]).replace(/\+/g,' ') : '';
}


function stopPropagation(e) {
	if (!e)
	{
		var e = window.event;
	}

	e.cancelBubble = true;
	e.returnValue = false;

	if (e.stopPropagation)
		e.stopPropagation();

}

function my()
{
	getARenderJS().getDocumentMetadata().getDocumentMetadata(getARenderJS().getCurrentDocumentId(), function(metas){
		console.log(metas.Amount);
		getARenderJS().getDocumentMetadata().addDocumentMetadata(metas, "Amount", "11");
		console.log(metas.Amount);
	});
}