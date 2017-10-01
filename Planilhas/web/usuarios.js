/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('ClientesCtrl', function ($scope, $rootScope, $location, $http, cadastros)
{
    $rootScope.activetab = $location.path();
    $scope.pageActive = 'selAtleta';

    $scope.page = 'Pagina do treinador';
    $scope.listaRegistros = [];
    $scope.microCicloAtleta = [];
    
    $scope.listaEstadosCidades = cadastros.getEstadosCidades();
    
    $('#myModal').modal('show');


    $scope.load = function ()
    {
        $http.post("select.jsp", {params: {"tab": "usuarios"}}).then(function (response) {
            $scope.dataParam = response.data;
            $scope.listaRegistros = response.data.registros;
        });

    };
    
    $scope.editar = function (item)
    {
        $scope.registro=item;

    };

    $scope.gravar = function (item)
    {
        $http.post("gravar.jsp", {params: {"tab": "tipos_modalidades", "dados": item}}).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                item.ctrl_status = 1;
                item.codigo = response.data.novoCodigo;
            }
        });
    };

    $scope.novo = function (mc, mct) {
        var lParams = {
            params:
                    {
                        "tab": "tabela",
                        "acao": 1,
                        "dia": mct.dia,
                        "i_clientes": mc.i_clientes,
                        "i_usuarios": mc.i_usuarios,
                        "i_micro_ciclo": mc.i_micro_ciclo

                    }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                mct.Itens.push(response.data.registro);
            }
        });
    }

    $scope.excluir = function (mct_dia, mct) {

        var lParams = {
            params: {
                "tab": "tabela",
                "codigo": mct_dia.codigo,
                "acao": 2,
                "dados": mct_dia
            }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                cadastros.excluirDaLista(mct_dia, mct.Itens);
            }
        });
    }

})