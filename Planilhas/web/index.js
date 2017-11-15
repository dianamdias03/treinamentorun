var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('IndexCtrl', function ($scope, $http, cadastros) {
    $scope.opcoesMenu = [];
    $scope.nomeCliente = cadastros.getNomeCliente();


    $http.post("sessao.jsp", {}).then(function (response) {
        $scope.dadosSessao = response.data;
        if (!$scope.dadosSessao.logado) {
            document.location.href = './login.html';
        } else {
            $scope.opcoesMenu = cadastros.getMenus($scope.dadosSessao);
        }
    });
}
)