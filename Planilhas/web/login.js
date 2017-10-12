/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('LoginCtrl', function ($scope, $http, $location)
{

    $scope.retorno = {
        "resultado": true,
        "msg": ''
    };

    $scope.$location = $location;
//    $scope.ur = $scope.$location.url();
    $scope.loc1 = $scope.$location.path();
//    if ($location.url().indexOf('keyword') > -1) {
//        $scope.loc = $location.url().split('=')[1];
//        $scope.loc = $scope.loc.split("#")[0]
//    }

    $scope.entrar = function (item)
    {
        $http.post("login.jsp", {"inputEmail": item.inputEmail, "inputPassword": item.inputPassword}).then(function (response) {
            $scope.retorno = response.data;
            $scope.resultado = response.data.resultado;
            if ($scope.resultado === true) {
                $scope.usuario = response.data.usuario;
                document.location.href = './index.html';
            }
        });

    };

})