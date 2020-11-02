// Generate a GeoJSON line. You could also load GeoJSON via AJAX
// or generate it some other way.
var geojson = { type: 'LineString', coordinates: [] };
var start = [10, 20];
var momentum = [3, 3];

for (var i = 0; i < 300; i++) {
    start[0] += momentum[0];
    start[1] += momentum[1];
    if (start[1] > 60 || start[1] < -60) momentum[1] *= -1;
    if (start[0] > 170 || start[0] < -170) momentum[0] *= -1;
    geojson.coordinates.push(start.slice());
}
var output='';
/*var runLayer = omnivore.gpx('img/route2gesvres.gpx')
    .on('error', function(data) {
        console.log('error' + data+ " "+ this);
        for (var property in data) {
  output += property + ': ' + data[property]+'; ';
}
console.log('error ---' + output);
    })
    .toGeoJSON();
    console.log(runLayer);
*/
// merde pour convertir GPX to JSON

// Add this generated geojson object to the map.
L.geoJson(geojson).addTo(map);


// Create a counter with a value of 0.
var j = 0;

// Create a marker and add it to the map.
var marker = L.marker([0, 0], {
  icon: L.mapbox.marker.icon({
    'marker-color': '#f86767'
  })
}).addTo(map);

tick();
function tick() {
    // Set the marker to be at the same point as one
    // of the segments or the line.
    marker.setLatLng(L.latLng(
        geojson.coordinates[j][1],
        geojson.coordinates[j][0]));
       


    // Move to the next point of the line
    // until `j` reaches the length of the array.
    if (++j < geojson.coordinates.length) setTimeout(tick, 100);
}
                
                
        }
        );