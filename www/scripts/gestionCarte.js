myCont.controller('cartoCTRL', function($scope) {
  console.log( "CTRL OK");
  $scope.myinfo = " carte";
  console.log(" -->"+ $scope.myinfo);
  $scope.banner=" TEST ";
var featureLayer = L.mapbox.featureLayer();
var featureLayers=[];
  
  var d = new Date();
  var datepred = d.getTime();
 
  var tickTime=0;
  var pointeur = 0;
  //Fonstion timer
  function tickAnimation(){
  tickTime++;
  animationThreadIsStarting=true;
  d = new Date();
  datecrt = d.getTime();
  //console.log(" --> " + (datecrt-datepred));
  
  datepred = d.getTime();

  if (animation){
      //Information 
   count.innerHTML =  "tick: " + pointeur + "</br>";
   tickTime++;
   //
    
    

    num = jsonData.scenario.length;
    pointeur = tickTime%num;
    //RAZ carte et des routes 
    for  (i=0; i<featureLayers.length; i++){
        featureLayers[i].clearLayers() ;  
    }
   
  for (l=0; l< jsonData.roads.length; l++){
   // for (l=0; l<2 ; l++){        
  var geojson = [
  {
    "type": "Feature",
    "geometry": {
      "type": "LineString",
      "coordinates": [
       
      ]
    },
    "properties": {
      "stroke": jsonData.roads[l].color,
      "stroke-opacity":0.5,
      "stroke-width": 6
    }
  }
];
//See 
/*
http://gis.stackexchange.com/questions/90385/which-are-the-geojson-css-style-properties-for-mapbox-js
*/

//var geojson = { type: 'LineString', coordinates: [] };

var start =  [jsonData.roads[l].points[0],jsonData.roads[l].points[1]];
  var start2 =  [jsonData.roads[l].points[0],jsonData.roads[l].points[1]];

//for (var i =0; i <jsonData.routes[l].points.length; i = i+2 ){
  //VEhicule 1 route 1 
  //pos = jsonData.scenario[pointeur][p][1]

  for (var i =0; i <=jsonData.scenario[pointeur][l][2]; i = i+1 ){
    //console.log(" --> " +JSON.stringify(jsonData.roads[l].points[1+i]));
   // console.log(" --> " +(jsonData.roads[l].points[1+i]));
    start[0] =jsonData.roads[l].points[(2*i)];
    start[1] = jsonData.roads[l].points[(2*i)+1];
   // start2[0] =jsonData.roads[l].points[0+i] +0.0001;
   // start2[1] = jsonData.roads[l].points[1+i] +0.0001;
    
    
    // console.log(start[0] + " --- "+ start[1]);
    geojson[0].geometry.coordinates.push(start.slice());
   }
   //console.log(JSON.stringify(jsonData.scenario[pointeur][l]) + " " ); 
   //jsonData.scenario[pointeur][l][4]);
   
    start[0] =jsonData.scenario[pointeur][l][3];
    start[1] =jsonData.scenario[pointeur][l][4];
    geojson[0].geometry.coordinates.push(start.slice());
    
//
featureLayer = L.mapbox.featureLayer(geojson)
    .addTo(map);
//L.geoJson(geojson, { style: L.mapbox.simplestyle.style}).addTo(map);
geojsons[l]=geojson;
featureLayers[l]=featureLayer;



}




      

    
    
      setTimeout(tickAnimation, 10);

  }
  else{
    setTimeout(tickAnimation, 50);
  }  

}


  //MAPBOX definition 
  L.mapbox.accessToken = 'pk.eyJ1IjoicnVydW51ZWxhIiwiYSI6ImNpa3kzdjZ1bDAwenZ2Zm0zZWw4MzFsaGYifQ.LLekgU2Sj_3oQQOdLHnZpw';

  //limite de navigation
  var southWest = L.latLng(47.10, -1.5),
  northEast = L.latLng(47.3, -1.7),
  bounds = L.latLngBounds(southWest, northEast);
  var map = L.mapbox.map('map', 'mapbox.streets',{
        gridControl: true, // Disable default gridControl interactivity.
       // maxBounds: bounds,
        maxZoom: 19,
        minZoom: 10
        //}).setView([48.8333300	, 2.235], 15);//.setView([0, 0], 1);
        }).setView([47.2181694, -1.5515394], 15);
        
  //2.225250508849751,48.84119904431763
  //Recupération du fichier JSON
  $.get('img/data-test/jeux4.json').
    success(function(data, status, headers, config) {
    jsonData = JSON.parse(data);
   // count.innerHTML += jsonData.id + "</br>";
   // count.innerHTML += jsonData.creator;
        //marker 
      
        
        //route 
        // Attnetion long et lat inversé :( )
  
//marqueur d





        
    }).//End succes
    error(function(data, status, headers, config) {
      // log error
      console.log (" ERROR with JSON DATA FILE");
      alert ( " BUG JSON DATA");
    });





 

if(!(animationThreadIsStarting)){
  tickAnimation();
}

//interesting :
//http://jimhoskins.com/2012/12/17/angularjs-and-apply.html
//End controller


  });
