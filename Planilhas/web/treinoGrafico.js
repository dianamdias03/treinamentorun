/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('TreinoGraficoCtrl', function ($scope, $rootScope, $location, $http, cadastros)
{
    $scope.listaRegistros = [];
    $scope.opcoesMenu = [];
    $scope.nomeCliente = cadastros.getNomeCliente();
    
    $scope.nomeImagem="c:\\temp\\01\\qui.png";

    $scope.load = function ()
    {

        $http.post("sessao.jsp", {}).then(function (response) {
            $scope.dadosSessao = response.data;
            if (!$scope.dadosSessao.logado) {
                document.location.href = './login.html';
            } else {
                $scope.opcoesMenu = cadastros.getMenus($scope.dadosSessao);
            }
        });

//        $http.post("consulta.jsp", {"consulta": "grupos"}).then(function (response) {
//            $scope.dataParam = response.data;
//            $scope.listaRegistros = response.data.dados;
//        });

    };

})