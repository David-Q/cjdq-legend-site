/**
 * google map
 */
var map;
var marker;

function initialize(lastlng,lastlat) {
    var startPos = new google.maps.LatLng(lastlat,lastlng);
    var mapOptions = {
        zoom : 15,
        center : startPos,
        mapTypeId : google.maps.MapTypeId.ROADMAP
    };
    var image = '/static/img/marker-mini.png'
    map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
    marker = new google.maps.Marker({
        position : startPos,
        map : map,
        icon: image
    });

    google.maps.event.addListener(map, 'click', function(event) {
        placeMarker(event.latLng);
    });

    /*$('#test').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/suggest/suggest/?",
            dataType: 'json',
            quietMillis: 100,
            data: function(term, page) {
                return {
                    q: term
                };
            },
            results: function(data, page ) {
                return { results: data.records }
            }
        },
        formatResult: function(data) { 
            return "<div class='select2-user-result'>" + data.word + "</div>"; 
        },
        formatSelection: function(data) { 
            return data.word; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"word":elementText});
        }
    });*/


$('#posWord').select2({
        minimumInputLength: 1,
        placeholder: 'Search',
        ajax: {
            url: "/suggest/suggest/?",
            dataType: 'json',
            quietMillis: 100,
            data: function(term, page) {
                return {
                    q: term
                };
            },
            results: function(data, page ) {
                return { results: data.results.exercise }
            }
        },
        formatResult: function(exercise) { 
            return "<div class='select2-user-result'>" + exercise.term + "</div>"; 
        },
        formatSelection: function(exercise) {
            return exercise.term; 
        },
        initSelection : function (element, callback) {
            var elementText = $(element).attr('data-init-text');
            callback({"term":elementText});
        },
        createSearchChoice:function(term, data) {
         if ( $(data).filter( function() {
           return this.term.localeCompare(term)===0;
         }).length===0) {
           return {id:1, term:term};
         }
       }
    });
}

function placeMarker(location) {
    marker.setPosition(location);
    document.getElementById('id_lng').value = location.lng();
    document.getElementById('id_lat').value = location.lat();
};


function setPosition() {
    var pos = new google.maps.LatLng(document.getElementById('id_lat').value,
            document.getElementById('id_lng').value);
    map.setCenter(pos);
    placeMarker(pos);
}

function findPosition(){
    var word = $('#posWord').select2('data').term;
    var req = new XMLHttpRequest();
    //req.open('GET', "https://maps.google.com/maps/api/geocode/json?address="+word+"&sensor=true", false);
    req.open('GET', "http://localhost:8080/geocode/geocode/?q="+word, false);
    req.send(null);
    var responseBody = req.responseText
    //alert(responseBody);
    obj = JSON.parse(responseBody);

    if (obj.results.length > 0)
    {
        lat = obj.results[0].geometry.location.lat;
        lng = obj.results[0].geometry.location.lng;
        document.getElementById('id_lng').value = lat;
        document.getElementById('id_lat').value = lng;

        var pos = new google.maps.LatLng(lat,lng);
        map.setCenter(pos);
        placeMarker(pos);
    }
    else
    {
        alert("未找到该地点");
    }


}

/* for suggest use */
function findSuggest(word)
{
    var req = new XMLHttpRequest();
    req.open('GET', "http://localhost:8080/suggest/suggest/?q="+word, false);
    req.send(null);
    var responseBody = req.responseText
    return responseBody;
}





