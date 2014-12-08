angular.module('badApp', ['ngRoute'])

    .config(function($routeProvider) {
        $routeProvider
            .when('/', {
                controller: 'FilmListCtrl',
                templateUrl: '/template/filmlist.html'
            })
            .when('/film/:filmId', {
                controller: 'FilmCtrl',
                templateUrl: '/template/film.html'
            })
            .otherwise({
                redirectTo: '/'
            });
    })

    .controller('FilmListCtrl', function($scope, $http, $location) {
        $http.get('/bad/films').success(function(data) {
            $scope.films = data;
        });

        $scope.go = function(filmId) {
            $location.path('/film/' + filmId);
        };
    })

    .controller('FilmCtrl', function($scope, $http, $routeParams, $location) {
        var filmId = $routeParams.filmId;

        $http.get('/bad/films/' + filmId).success(function(data) {
            $scope.film = data;
        });

        $scope.back = function() {
            $location.path('/');
        };
    })

;