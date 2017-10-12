var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('IndexCtrl', function ($scope, $http, cadastros) {
        $scope.opcoesMenu = cadastros.getMenus();
        $scope.nomeCliente = cadastros.getNomeCliente();
    }
)