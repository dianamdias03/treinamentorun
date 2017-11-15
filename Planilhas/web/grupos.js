/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('GruposCtrl', function ($scope, $rootScope, $location, $http, cadastros)
{
    $scope.listaRegistros = [];
    $scope.opcoesMenu = [];
    $scope.nomeCliente = cadastros.getNomeCliente();

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

        $http.post("consulta.jsp", {"consulta": "grupos"}).then(function (response) {
            $scope.dataParam = response.data;
            $scope.listaRegistros = response.data.dados;
        });

    };

    $scope.editar = function (item)
    {
        $scope.registro = item;
        $('#myModal').modal('show');

    };

    $scope.gravar = function (item)
    {
        $http.post("gravar.jsp", {params: {"tabela": "grupos", "dados": item}}).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                item.ctrl_status = 1;
                if (item.codigo === 0) {
                    item.codigo = response.data.novoCodigo;
                    $scope.listaRegistros.push(item);
                }
                $('#myModal').modal('hide');
            }
        });
    };

    $scope.novo = function () {
        var lParams = {
            params:
                    {
                        "tabela": "grupos",
                        "acao": 1
                    }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                $scope.registro = response.data.registro;
                $('#myModal').modal('show');
            }
        });
    }

    $scope.excluir = function (item) {

        var lParams = {
            params: {
                "tabela": "grupos",
                "codigo": item.codigo,
                "acao": 2,
                "dados": item
            }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                cadastros.excluirDaLista(item, $scope.listaRegistros);
            }
        });
    }

})