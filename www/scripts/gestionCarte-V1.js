  myCont.controller('cartoCTRL', function($scope) {
                console.log( "CTRL OK");
                $scope.myinfo = " carte";
                console.log(" -->"+ $scope.myinfo);
                 $scope.banner=" TEST ";
                
                
                
				
				
                L.mapbox.accessToken = 'pk.eyJ1IjoicnVydW51ZWxhIiwiYSI6ImNpa3kzdjZ1bDAwenZ2Zm0zZWw4MzFsaGYifQ.LLekgU2Sj_3oQQOdLHnZpw';
                
                //limite de navigation
                var southWest = L.latLng(47.10, -1.5),
    northEast = L.latLng(47.3, -1.7),
    bounds = L.latLngBounds(southWest, northEast);
var map = L.mapbox.map('map', 'mapbox.streets',{
  gridControl: true, // Disable default gridControl interactivity.
    maxBounds: bounds,
    maxZoom: 19,
    minZoom: 10
}).setView([47.40, -1.4], 12);//.setView([0, 0], 1);
//map.fitBounds(bounds);
//Load Json Simuation File 

var count = document.getElementById('count');
//ass $http.get
 $.get('img/data-test/jeux1.json').
    success(function(data, status, headers, config) {
        console.log(" ---" + data); ;
        //jsondData= data;
        jsonData = JSON.parse(data);
        count.innerHTML += jsonData.id + "</br>";
        count.innerHTML += jsonData.creator;
        //marker 
        var marker = L.marker( [jsonData.routes[0].points[1],jsonData.routes[0].points[0]], {
    icon: L.mapbox.marker.icon({
      'marker-color': '#f86767'
    })
    });
        
        //route 
        // Attnetion long et lat inversé :( )

for (l=0; l< jsonData.routes.length; l++){        
var geojson = [
  {
    "type": "Feature",
    "geometry": {
      "type": "LineString",
      "coordinates": [
       
      ]
    },
    "properties": {
      "stroke": jsonData.routes[l].color,
      "opacity":1,
      "stroke-width": 3
    }
  }
];

//var geojson = { type: 'LineString', coordinates: [] };

var start =  [jsonData.routes[l].points[0],jsonData.routes[l].points[1]];
  

for (var i =0; i <jsonData.routes[l].points.length; i = i+2 ){
    start[0] =jsonData.routes[l].points[0+i]
    start[1] = jsonData.routes[l].points[1+i]
    geojson[0].geometry.coordinates.push(start.slice());
}



L.geoJson(geojson, { style: L.mapbox.simplestyle.style}).addTo(map);
geojsons[l]=geojson;
}
//marqueur de type car

for (l=0; l< jsonData.cars.length; l++){
  
  
  /*var cssIcon = L.divIcon({
  // Specify a class name we can refer to in CSS.
  className: 'my-icon icon-sf ',

  // Set marker width and height
  iconSize: [10, 10]
});*/
//reference:http://leafletjs.com/examples/custom-icons.html
//var greenIcon = L.icon({
  //  iconUrl: 'img/icon-cars2.png',
   // shadowUrl: 'img/leaf-shadow.png',

  //  iconSize:     [24, 24], // size of the icon
    //shadowSize:   [50, 64], // size of the shadow
    //iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
    //shadowAnchor: [4, 62],  // the same for the shadow
  //  popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
//});
/*var marker = L.marker([0, 0], {
  icon: L.mapbox.marker.icon({
    'marker-color': '#f86767'
  })
}).addTo(map);*/
//var marker=L.marker([0,0], {icon: greenIcon}).addTo(map);
 var marker = L.marker([0, 0], {
  icon: L.mapbox.marker.icon(
{

   // 'marker-symbol': 'car',
        "marker-color": "#3bb2d0", // class name to style
       "marker-symbol": "t", // add content inside the marker
       "stroke":'#3bb2d0',
       //  'marker-size': 'small'// size of icon, use null to set the size in CSS
      }

  )
  }).addTo(map);
 /* marker.setIcon(L.mapbox.marker.icon({
                   // 'marker-color': '#ff8888',
                   // "marker-symbol": ""+l,
                     'class':'account',
                    'marker-size': 'small'}));*/

//var marker = L.marker([0, 0], {icon: cssIcon}).addTo(map);

  
    marker.setLatLng(L.latLng(
    geojsons[l][0].geometry.coordinates[0][1],
    geojsons[l][0].geometry.coordinates[0][0]));
    markers[l]=marker;
    
}
//marker.addTo(map);


        
    }).
    error(function(data, status, headers, config) {
      // log error
    });
// count.innerHTML += ;
 




 

if(!(animationThreadIsStarting)){
  tickAnimation();
}
var d = new Date();
var datepred = d.getTime();
function tickAnimation(){
  tickTime++;
  animationThreadIsStarting=true;
  d = new Date();
  datecrt = d.getTime();
  console.log(" --> " + (datecrt-datepred));
  datepred = d.getTime();

  if (animation){
    //$scope.banner=tickTime+"";
   // $scope.$apply();
    
    num = jsonData.scenario.length;
    pointeur = tickTime%num;
    for (var p=0; p < jsonData.scenario[pointeur].length; p++){
      var veh = jsonData.scenario[pointeur][p][0];
      var route = jsonData.scenario[pointeur][p][1];
      var pos = jsonData.scenario[pointeur][p][2];
   //   console.log(p+"|"+veh + " "+ route + " "+pos);
      markers[veh].setLatLng(L.latLng(
      geojsons[route][0].geometry.coordinates[pos][1],
      geojsons[route][0].geometry.coordinates[pos][0]));
      

    }
   // $scope.$apply();
    //move 
   // markers[0].setLatLng(L.latLng(
   // geojsons[l][0].geometry.coordinates[0][1],
   // geojsons[l][0].geometry.coordinates[0][0]));
    
      setTimeout(tickAnimation, 10);

  }
  else{
    setTimeout(tickAnimation, 100);
  }  

}
//interesting :
//http://jimhoskins.com/2012/12/17/angularjs-and-apply.html
//End controller


  });
