var badApp = angular.module('badApp', []);

badApp.controller('BadCtrl', function($scope, $http) {
    $scope.films = [
        { 'name': 'void', 'score': 3, 'description': 'empty' },
        { 'name': 'hello film', 'score': 5, 'description': 'Yet another boring film' }
    ];

    $http.get('/bad/films').success(function(data) {
        $scope.films = data;
    });
});