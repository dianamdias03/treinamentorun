
var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('PlanilhaSemanalCtrl', function ($scope, $rootScope, $location, $http, cadastros)
{
    $scope.planilhaSemanal = [];
    $scope.opcoesMenu = cadastros.getMenus();
    $scope.nomeCliente = cadastros.getNomeCliente();
    cadastros.setScope($scope);
    $scope.dia = "2017-10-09";

    $scope.loadPlanilhaSemana = function (navegacao)
    {
        $http.post(
                "gravar.jsp",
                {
                    params: {
                        "acao-gravar": "consultaSQL",
                        "consulta": "planilhaSemanal",
                        "i_clientes": 1,
                        "i_usuarios": 4,
                        "navegacao": navegacao,
                        "dia": $scope.dia
                    }
                }
        )
                .then(function (response) {
                    $scope.planilhaSemanal = response.data.dados;
                    $scope.dia = response.data.dia;
                });
    };


    $scope.loadPlanilhaSemana(0);


});
