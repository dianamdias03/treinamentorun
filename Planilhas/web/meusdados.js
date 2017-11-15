/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('MeusDadosCtrl', function ($scope, $rootScope, $location, $http, cadastros)
{
    $scope.listaRegistros = [];
    $scope.opcoesMenu = [];
    $scope.nomeCliente = cadastros.getNomeCliente();
    $scope.modo = 0;
    $scope.retorno = {
        "retorno": true,
        "erroMsg": ""
    };

    $scope.setModo = function (a_modo) {
        $scope.modo = a_modo;
    }

    $scope.dadosUsuario = function ()
    {
        lParam = {
            "consulta": "usuarios",
            "i_usuarios": $scope.dadosSessao.i_usuarios
        };

        $http.post("consulta.jsp", lParam).then(function (response) {
            $scope.registro = response.data.dados[0];
        });


    };

    $scope.load = function ()
    {

        $http.post("sessao.jsp", {}).then(function (response) {
            $scope.dadosSessao = response.data;
            if (!$scope.dadosSessao.logado) {
                document.location.href = './login.html';
            } else {
                $scope.opcoesMenu = cadastros.getMenus($scope.dadosSessao);
                $scope.dadosUsuario();
            }
        });
    };

    $scope.gravar = function (tipo) {

        lParam = {
            "consulta": "meusdados-gravar",
            "tipo": tipo,
            "dados": $scope.registro
        };

        $http.post("consulta.jsp", lParam).then(function (response) {
            $scope.retorno = response.data;
            if ($scope.retorno.retorno) {
                $scope.setModo(0);
            }
        });
    };

})