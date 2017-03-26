var map;
function initMap() {
    const current_lat = geoplugin_latitude();
    const current_lon = geoplugin_longitude();

    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 8,
        center: new google.maps.LatLng(current_lat, current_lon),
        mapTypeId: 'roadmap'
    });

    map.addListener('dragend', function () {
        // 3 seconds after the center of the map has changed, pan back to the
        // marker.
        window.setTimeout(function () {
            readData(map.center);
        }, 3000);

    });
    map.addListener('zoom_changed', function () {
        // 3 seconds after the center of the map has changed, pan back to the
        // marker.
        window.setTimeout(function () {
            readData(map.center);
        }, 3000);

    });

    readData(new google.maps.LatLng(current_lat, current_lon));
}

function readData(point) {
    // Create a <script> tag and set the USGS URL as the source.
    var script = document.createElement('script');
    var params = `callback=eqfeed_callback&lat=${point.lat()}&lon=${point.lng()}`;
    script.src = `http://localhost:8080/groups/search/jsonp?${params}`;
    document.getElementsByTagName('head')[0].appendChild(script);
}

function clearData() {
    map.data.forEach(function(feature) {
        map.data.remove(feature);
    });
}

// Loop through the results array and place a marker for each
// set of coordinates.
window.eqfeed_callback = function (response) {
    clearData();
    map.data.addGeoJson(response);
}
