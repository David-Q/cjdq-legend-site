function schemaToDiv()
{
    var schemaText = $('#id_indexSchema').val();
    //alert(schemaText)
    try
    {
        xmlDoc = $.parseXML( schemaText );
    }
    catch(err)
    {
        return;
    }


    $xml = $( xmlDoc );

    var divHtml = "";
    $field = $xml.find( "fieldlist" ).find("field").each(function(){
        divHtml = divHtml.concat("<tr><td><input type=\"button\" value=\"删除\" class=\"btn btn-danger\" onclick=\"deleteOneField(this)\"></td>");
        //$.each(this.attributes, function(i, attrib){
        //var name = attrib.name;
        //var value = attrib.value;
        //alert(name + "=" +value);
        //divHtml = divHtml.concat("<td>").concat(value).concat("</td>");
        //});

        var $row = $(this);
        //do with name
        var name = $row.attr("name");
        divHtml = divHtml.concat("<td><input style=\"width:200px\" type=\"textfield\" class=\"fieldname\" value=\"")
        divHtml = divHtml.concat(name);
        divHtml = divHtml.concat("\"></input></td>");

        //do with type
        var type = $row.attr("type");
        divHtml = divHtml.concat("<td><select style=\"width:100px\" class=\"fieldtype\">");
        typeOptions = ["布尔类型","整数型","浮点数型","地理坐标","标签类型","文本类型","键值对型","多值整数","多值浮点数","多值地理坐标","多值标签","多值键值对"];
        typeValues = ["int8","int32","float32","geo","string","text","property","multi_int32","multi_float32","multi_geo","multi_string","multi_property"]
        allTypeValues = ["int8","int16","int32","int64","multi_int32","multi_int64",
        "float32","float64","multi_float32","multi_float64","geo","multi_geo","string","multi_string","text","property","multi_property"]
        for(x in typeValues)
        {
            if(typeValues[x]==type)
            {
                divHtml = divHtml.concat("<option selected=\"selected\" value=\""+ typeValues[x] +"\">");
                divHtml = divHtml.concat(typeOptions[x]);
                divHtml = divHtml.concat("</option>");
            }
            else
            {
                divHtml = divHtml.concat("<option value=\""+ typeValues[x] +"\">");
                divHtml = divHtml.concat(typeOptions[x]);
                divHtml = divHtml.concat("</option>");
            }
        }
        divHtml = divHtml.concat("\"></select></td>");

        //do with index, so many copy/paste i know...
        var index = $row.attr("index");
        divHtml = divHtml.concat("<td><select style=\"width:100px\" class=\"fieldindex\">");
        indexOptions = ["是","否"];
        indexValues = ["true","false"];
        for(x in indexOptions)
        {
            if(indexValues[x]==index)
            {
                divHtml = divHtml.concat("<option selected=\"selected\" value=\""+ indexValues[x] +"\">");
                divHtml = divHtml.concat(indexOptions[x]);
                divHtml = divHtml.concat("</option>");
            }
            else
            {
                divHtml = divHtml.concat("<option value=\""+ indexValues[x] +"\">");
                divHtml = divHtml.concat(indexOptions[x]);
                divHtml = divHtml.concat("</option>");
            }
        }
        divHtml = divHtml.concat("\"></select></td>");

        //do with store, so many copy/paste i know...
        var store = $row.attr("store");
        divHtml = divHtml.concat("<td><select style=\"width:100px\" class=\"fieldstore\">");
        storeOptions = ["是","否"];
        storeValues = ["true","false"];
        for(x in storeOptions)
        {
            if(storeValues[x]==store)
            {
                divHtml = divHtml.concat("<option selected=\"selected\" value=\""+ storeValues[x] +"\">");
                divHtml = divHtml.concat(storeOptions[x]);
                divHtml = divHtml.concat("</option>");
            }
            else
            {
                divHtml = divHtml.concat("<option value=\""+ storeValues[x] +"\">");
                divHtml = divHtml.concat(storeOptions[x]);
                divHtml = divHtml.concat("</option>");
            }
        }
        divHtml = divHtml.concat("\"></select></td>");

        //do with docvalues, so many copy/paste i know...
        var docvalues = $row.attr("docvalues");
        divHtml = divHtml.concat("<td><select style=\"width:100px\" class=\"fielddocvalues\">");
        docvaluesOptions = ["是","否"];
        docvaluesValues =  ["true","false"];
        for(x in docvaluesOptions)
        {
            if(docvaluesValues[x]==docvalues)
            {
                divHtml = divHtml.concat("<option selected=\"selected\" value=\""+ docvaluesValues[x] +"\">");
                divHtml = divHtml.concat(docvaluesOptions[x]);
                divHtml = divHtml.concat("</option>");
            }
            else
            {
                divHtml = divHtml.concat("<option value=\""+ docvaluesValues[x] +"\">");
                divHtml = divHtml.concat(docvaluesOptions[x]);
                divHtml = divHtml.concat("</option>");
            }
        }
        divHtml = divHtml.concat("\"></select></td>");


        divHtml = divHtml.concat("</tr>");

    });

    $( "#schemaTable" ).find("tr:gt(0)").remove();
    $( "#schemaTable" ).append(divHtml);
}

function divToSchema()
{
    //var schemaDiv = document.getElementById("schemaDiv").innerHTML;
    var schemaDiv = $('#schemaTableDiv');
    //alert(schemaDiv.html());

    fieldNameArray = getElementsByClassName('input','fieldname')
    fieldTypeArray = getElementsByClassName('select','fieldtype')
    fieldIndexeArray = getElementsByClassName('select','fieldindex')
    fieldStoreArray = getElementsByClassName('select','fieldstore')
    fieldDocvaluesArray = getElementsByClassName('select','fielddocvalues')

    if(fieldNameArray.length==0)
    {
        return;
    }

    var schemaText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<fieldlist>\n"
    for(var k=0;k<fieldNameArray.length;k++)
    {
        var sb = "\t<field ";
        sb = sb.concat("name=\"").concat(fieldNameArray[k].value).concat("\" ");
        sb = sb.concat("type=\"").concat(fieldTypeArray[k].options[fieldTypeArray[k].selectedIndex].value).concat("\" ");
        sb = sb.concat("index=\"").concat(fieldIndexeArray[k].options[fieldIndexeArray[k].selectedIndex].value).concat("\" ");
        sb = sb.concat("store=\"").concat(fieldStoreArray[k].options[fieldStoreArray[k].selectedIndex].value).concat("\" ");
        sb = sb.concat("docvalues=\"").concat(fieldDocvaluesArray[k].options[fieldDocvaluesArray[k].selectedIndex].value).concat("\" ");
        if(fieldTypeArray[k].options[fieldTypeArray[k].selectedIndex].value=='text')
        {
            sb = sb.concat(" analyzer=\"standard\"");
        }
        sb = sb.concat("/>\n");
        schemaText = schemaText.concat(sb);
    }
    schemaText = schemaText.concat("</fieldlist>");
    $("#id_indexSchema").val(schemaText);
    showSchemaText();
}

function showSchemaText()
{
    schemaTextTab = document.getElementById("schemaText");
    schemaDivTab = document.getElementById("schemaDiv");
    schemaTextLi = document.getElementById("schemaTextLi");
    schemaDivLi = document.getElementById("schemaDivLi");

    schemaTextTab.setAttribute('class',"tab-pane active");
    schemaTextLi.setAttribute('class',"active");
    schemaDivTab.setAttribute('class',"tab-pane");
    schemaDivLi.setAttribute('class',"");
}

function showSchemaDiv()
{
    schemaTextTab = document.getElementById("schemaText");
    schemaDivTab = document.getElementById("schemaDiv");
    schemaTextLi = document.getElementById("schemaTextLi");
    schemaDivLi = document.getElementById("schemaDivLi");

    schemaDivTab.setAttribute('class',"tab-pane active");
    schemaDivLi.setAttribute('class',"active");
    schemaTextTab.setAttribute('class',"tab-pane");
    schemaTextLi.setAttribute('class',"");
}

function addOneField()
{
    var divHtml = "<tr><td><input type=\"button\" value=\"删除\" class=\"btn btn-danger\" onclick=\"deleteOneField(this)\"></td>";
    //do with name
    divHtml = divHtml.concat("<td><input style=\"width:200px\" type=\"textfield\" class=\"fieldname\"/></td>");

    //do with type
    divHtml = divHtml.concat("<td><select style=\"width:100px\" class=\"fieldtype\">");
    typeOptions = ["布尔类型","整数型","浮点数型","地理坐标","标签类型","文本类型","键值对型","多值整数","多值浮点数","多值地理坐标","多值标签","多值键值对"];
    typeValues = ["int8","int32","float32","geo","string","text","property","multi_int32","multi_float32","multi_geo","multi_string","multi_property"]
    for(x in typeOptions)
    {
            divHtml = divHtml.concat("<option value=\""+ typeValues[x] +"\">");
            divHtml = divHtml.concat(typeOptions[x]);
            divHtml = divHtml.concat("</option>");
    }
    divHtml = divHtml.concat("\"></select></td>");

    //do with index, so many copy/paste i know...
    divHtml = divHtml.concat("<td><select style=\"width:100px\" class=\"fieldindex\">");
    indexOptions = ["是","否"];
    indexValues = ["true","false"];
    for(x in indexOptions)
    {
            divHtml = divHtml.concat("<option value=\""+ indexValues[x] +"\">");
            divHtml = divHtml.concat(indexOptions[x]);
            divHtml = divHtml.concat("</option>");
    }
    divHtml = divHtml.concat("\"></select></td>");

    //do with store, so many copy/paste i know...
    divHtml = divHtml.concat("<td><select style=\"width:100px\" class=\"fieldstore\">");
    storeOptions = ["是","否"];
    storeValues = ["true","false"];
    for(x in storeOptions)
    {
            divHtml = divHtml.concat("<option value=\""+ storeValues[x] +"\">");
            divHtml = divHtml.concat(storeOptions[x]);
            divHtml = divHtml.concat("</option>");
    }
    divHtml = divHtml.concat("\"></select></td>");

    //do with docvalues, so many copy/paste i know...
    divHtml = divHtml.concat("<td><select style=\"width:100px\" class=\"fielddocvalues\">");
    docvaluesOptions = ["是","否"];
    docvaluesValues = ["true","false"];
    for(x in storeOptions)
    {
            divHtml = divHtml.concat("<option value=\""+ docvaluesValues[x] +"\">");
            divHtml = divHtml.concat(docvaluesOptions[x]);
            divHtml = divHtml.concat("</option>");
    }
    divHtml = divHtml.concat("\"></select></td>");


    divHtml = divHtml.concat("</tr>");
    $( "#schemaTable" ).append(divHtml);
}

function deleteOneField(o) {
     var p=o.parentNode.parentNode;
     p.parentNode.removeChild(p);
}

function handleFiles(files) {
    //for the same file problem of onchange.
    var clone = $( "#file" ).clone(false, false);
    if (files.length) {
        var file = files[0];
        var reader = new FileReader();
        if (/text\/\w+/.test(file.type)) {
            reader.onload = function() {
                    $( "#id_indexSchema" ).val(this.result);
            }
        reader.readAsText(file);
        }
    }
    $( "#file" ).replaceWith(clone);
    showSchemaText();
}

function getElementsByClassName(tagName,className) {
    var all = document.getElementsByTagName(tagName);
    var elements = new Array();
    for ( var e = 0; e < all.length; e++) {
        if (all[e].className == className) {
            elements[elements.length] = all[e];
        }
    }

    return elements;
}
