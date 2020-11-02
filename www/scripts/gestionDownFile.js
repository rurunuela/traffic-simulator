/*
Ouverture fichier KML 
generation information chemin
*/
        myCont.controller('kmlCTRL', function($scope) {
            $scope.myinfo = "TEST ouverture KML";
           
          var runLayer = omnivore.kml('img/it1.kml')
    .on('ready', function() {
       $scope.myinfo =  $scope.myinfo + runLayer;
    });
    
           
            
            
        }); 