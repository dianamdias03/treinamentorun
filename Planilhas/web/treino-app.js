
var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('TreinadorCtrl', function ($scope, $rootScope, $location, $http)
{
    $rootScope.activetab = $location.path();
    $scope.pageActive = 'selAtleta';

    $scope.page = 'Pagina do treinador';
    $scope.listaRegistros = [
        {
            id: 1,
            nome: 'Adriano',
            categoria: 'Intermediario',
            status: 1
        },
        {
            id: 2,
            nome: 'Fabiana',
            categoria: 'Iniciante',
            status: 1
        }
    ];

    $scope.tiposTreino = [{"i_clientes": 1, "i_tipos_treinos": 1, "descricao": "Não informado"}, {"i_clientes": 1, "i_tipos_treinos": 2, "descricao": "Continuo"}, {"i_clientes": 1, "i_tipos_treinos": 3, "descricao": "Intervalado"}, {"i_clientes": 1, "i_tipos_treinos": 4, "descricao": "Fartlek"}, {"i_clientes": 1, "i_tipos_treinos": 5, "descricao": "Regenerativo"}, {"i_clientes": 1, "i_tipos_treinos": 6, "descricao": "Ritmo"}, {"i_clientes": 1, "i_tipos_treinos": 7, "descricao": "Longo"}, {"i_clientes": 1, "i_tipos_treinos": 8, "descricao": "Prova"}];

    $scope.execAcao = function (acao, item) {
        $scope.Acao = 'Clicou: ' + acao + '>' + item.nome;
        $scope.Atleta = item;
    }

    $scope.selectPage = function (page, item) {
        $scope.Atleta = item;
        $scope.pageActive = page;

        if ($scope.pageActive === 'criaTreinos') {
            $scope.loadMicroCicloAtleta();
        }
    }

    $scope.loadMicroCicloAtleta = function ()
    {
        $http.post(
                "select.jsp",
                {
                    params: {
                        "tab": "atleta_micro_ciclo",
                        "i_usuarios": $scope.Atleta.i_usuarios,
                    }
                }
        )
                .then(function (response) {
                    $scope.microCicloAtleta = response.data.registros;
//                    console.log('Micro Ciclo Atleta: '+$scope.microCicloAtleta[0].i_micro_ciclo);
                    $scope.loadMicroCicloTreinosAtleta($scope.microCicloAtleta[0].i_micro_ciclo);
                });
    };

    $scope.loadMicroCicloTreinosAtleta = function (i_micro_ciclo)
    {
        $http.post(
                "select.jsp",
                {
                    params: {
                        "tab": "atleta_micro_ciclo_treinos",
                        "i_usuarios": $scope.Atleta.i_usuarios,
                        "i_micro_ciclo": i_micro_ciclo,
                    }
                }
        )
                .then(function (response) {
                    $scope.microCicloTreinosAtleta = response.data.registros;
                });
    };

    $scope.load = function ()
    {
        $http.post("select.jsp", {params: {"tab": "atletas"}}).then(function (response) {
            $scope.dataParam = response.data;
            $scope.listaRegistros = response.data.registros;
        });

        $http.post("select.jsp", {params: {"tab": "tipos_modalidades"}}).then(function (response) {
            $scope.tiposModalidades = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tab": "tipos_treinos"}}).then(function (response) {
            $scope.tiposTreinos = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tab": "tipos_intensidades"}}).then(function (response) {
            $scope.tiposIntensidades = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tab": "tipos_percursos"}}).then(function (response) {
            $scope.tiposPercursos = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tab": "tipos_distancias"}}).then(function (response) {
            $scope.tiposDistancias = response.data.registros;
        });
    };

    $scope.gravar = function (item)
    {
        $http.post("gravar.jsp", {params: {"tab": "tipos_modalidades", "dados": item}}).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                item.ctrl_status = 1;
            }
        });
    };

    $scope.novo = function (mct) {
//        mct.Itens.push({"dia": "ccc", "ctrl_status": 1});

        $http.post("gravar.jsp", {params: {"tab": "tipos_modalidades", "acao": 1}}).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                mct.Itens.push(response.data.registro);
            }
        });
    }

})