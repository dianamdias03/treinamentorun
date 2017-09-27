/* global angular */

// Code goes here
var pawork_modulo = angular.module('pawork', ['ngRoute']);
pawork_modulo.service('globais', function () {

    this.findItemArray = function (arrayFind, item) {
        for (var i = 0; i < arrayFind.length; i++) {
            if (arrayFind[i] === item) {
                return i;
            }
        }
        return -1;
    }

    this.formataData = function (data) {
        return ("0" + data.getDate()).substr(-2) + "/"
                + ("0" + (data.getMonth() + 1)).substr(-2) + "/" + data.getFullYear();
    }

    this.isHora = function (hora) {

        if (hora === undefined) {
            return false;
        }

        if (hora.length !== 5) {
            return false;
        }

        return true;
    }

    this.time_diff = function (hora1, hora2) {

        if (!this.isHora(hora1) || !this.isHora(hora2)) {
            return "00:00";
        }
        ;
        var hora_min_1 = this.time_minutes_num(hora1);
        var hora_min_2 = this.time_minutes_num(hora2);
        var diferenca = hora_min_2 - hora_min_1;
        if (diferenca < 0) {
            diferenca = 0;
        }
        return this.time_num_hora(diferenca);
    }

    this.time_minutes_num = function (hora) {
        if (hora === undefined) {
            return 0;
        }
        return Number(hora.substr(0, 2)) * 60 + Number(hora.substr(3, 2));
    }

    this.time_num_hora = function (hora_minutos) {
        var hora = Math.trunc(hora_minutos / 60);
        var minutos = hora_minutos % 60;
        if (isNaN(hora) || isNaN(minutos)) {
            return "00:00";
        }
        return ("0" + hora).substr(-2) + ":" + ("0" + minutos).substr(-2);
    }

    this.time_add = function (hora, minutos) {
        var hora_min_1 = this.time_minutes_num(hora);
        return this.time_num_hora(hora_min_1 + minutos);
    }

});

pawork_modulo.config(['$qProvider', function ($qProvider) {
        $qProvider.errorOnUnhandledRejections(false);
    }]);
pawork_modulo.controller('HomeCtrl', function ($scope, $rootScope, $location, $http)
{
    $rootScope.activetab = $location.path();
    $http.post("config.php", {})
            .then(function (response) {
                if (response.data === "-") {
                    window.location.href = '/login.html';
                }
                ;
            });
})

pawork_modulo.controller('GraficosCtrl', function ($scope, $http, $rootScope, $location)
{
    $rootScope.activetab = $location.path();
    google.charts.load("current", {packages: ["corechart"]});
//      google.charts.setOnLoadCallback(drawChart);
    $scope.drawChart = function (dados) {

        var lista = [];
//        dados = [
//            {"nome": "Primeira atividade", "tempo": "0.8"},
//            {"nome": "0-BBB xxx", "tempo": "7.6"},
//            {"nome": "Assistencia no treino no parque", "tempo": "8.1"},
//            {"nome": "Respondendo duvidas de atleta via whatsapp", "tempo": "4.7"},
//            {"nome": "Orienta\u00e7\u00e3o a atleta", "tempo": "6.0"},
//            {"nome": "Registrando pagamento de mensalidade", "tempo": "4.3"},
//            {"nome": "Avaliando feedback do atleta", "tempo": "1.2"},
//            {"nome": "Cadastrando treinos", "tempo": "1.0"}
//        ];

        lista.push(['Atividades', 'Tempo (horas)']);
        for (var i = 0; i < dados.length; i++) {
            lista.push([dados[i].nome, Number(dados[i].tempo)]);
        }

        var data = google.visualization.arrayToDataTable(lista);
        var options = {
            title: 'Atividades diárias',
            is3D: true,
        };
        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
        chart.draw(data, options);
    }

    $scope.load = function ()
    {
        $http.post(
                "graficoPizzaAtividades.php",
                {
                    params: {},
                    headers: {Authorization: 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='},
                    acao: 4
                }
        )
                .then(function (response) {
                    $scope.drawChart(response.data.registros);
                });
    };
})

// --------------------------------------------------

pawork_modulo.controller('RelatoriosCtrl', function ($scope, $http, $rootScope, $location, globais)
{
    $rootScope.activetab = $location.path();

    $scope.formataData = function (aDia) {
        return globais.formataData(aDia);
    }

    $scope.dataInicio = globais.formataData(new Date());
    $scope.dataFim = $scope.dataInicio;

    $scope.load = function ()
    {
        $http.post(
                "relacaoLancamentos.php",
                {
                    params: {
                        inicio: $scope.dataInicio,
                        fim: $scope.dataFim,
                        listaUsuarios: $scope.listaUsuarios
                    },
                    headers: {Authorization: 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='}
                }
        )
                .then(function (response) {
                    $scope.listaRegistros = response.data.registros;
                    $scope.listaUsuarios = response.data.listaUsuarios;
                    $scope.registrosPorAtividade = response.data.registrosPorAtividade;
                    $scope.registrosPorAtividadeUsuario = response.data.registrosPorAtividadeUsuario;
//                    $scope.dataInicio = response.data.inicio;
//                    $scope.dataFim = response.data.fim;
                });
    };
})

// --------------------------------------------------

pawork_modulo.controller('ClientesCtrl', function ($scope, $http, $rootScope, $location, globais, cadastros) {

    $rootScope.activetab = $location.path();
    cadastros.setScope($scope);
    cadastros.setHttp($http);
    cadastros.setBackEndFile("cad-clientes.php");

    $scope.editar = function (item) {
        cadastros.editar(item);
    }
    $scope.novo = function () {
        var item = {
            id: 0,
            nome: '',
            email: ''
        };
        cadastros.novo(item);
    }

    $scope.load = function ()
    {
        $http.post(
                cadastros.backEndFile,
                cadastros.load_params({}))
                .then(function (response) {
                    $scope.listaRegistros = response.data.registros;
                });

    };

    $scope.gravar = function (item, acao)
    {
        cadastros.gravar_inicio(item, acao);
        $http.post(
                cadastros.backEndFile,
                cadastros.gravar_params(item, acao))
                .then(function (response) {
                    cadastros.gravar_acao_padrao(response, item);
                });
    };
})

// --------------------------------------------------
pawork_modulo.controller('UsuariosCtrl', function ($scope, $http, $rootScope, $location, cadastros) {

    $rootScope.activetab = $location.path();
    cadastros.setScope($scope);
    cadastros.setHttp($http);
    cadastros.setBackEndFile("cad-usuarios.php");

    $scope.editar = function (item) {
        cadastros.editar(item);
    }
    $scope.novo = function () {
        var item = {
            id: 0,
            nome: '',
            senha: ''
        };
        cadastros.novo(item);
    }

    $scope.load = function ()
    {
        $http.post(
                cadastros.backEndFile,
                cadastros.load_params({}))
                .then(function (response) {
                    $scope.listaRegistros = response.data.registros;
                });

    };

    $scope.gravar = function (item, acao)
    {
        cadastros.gravar_inicio(item, acao);
        $http.post(
                cadastros.backEndFile,
                cadastros.gravar_params(item, acao))
                .then(function (response) {
                    cadastros.gravar_acao_padrao(response, item);
                });
    };
})

// --------------------------------------------------
pawork_modulo.controller('AtividadesCtrl', function ($scope, $http, $rootScope, $location, cadastros) {

    $rootScope.activetab = $location.path();
    cadastros.setScope($scope);
    cadastros.setHttp($http);
    cadastros.setBackEndFile("cad-atividades.php");

    $scope.editar = function (item) {
        cadastros.editar(item);
    }
    $scope.novo = function () {
        var item = {
            id: 0,
            nome: '',
            senha: ''
        };
        cadastros.novo(item);
    }

    $scope.load = function ()
    {
        $http.post(
                cadastros.backEndFile,
                cadastros.load_params({}))
                .then(function (response) {
                    $scope.listaRegistros = response.data.registros;
                    cadastros.login();
                });

    };

    $scope.gravar = function (item, acao)
    {
        cadastros.gravar_inicio(item, acao);
        $http.post(
                cadastros.backEndFile,
                cadastros.gravar_params(item, acao))
                .then(function (response) {
                    cadastros.gravar_acao_padrao(response, item);
                });
    };
})

// --------------------------------------------------
pawork_modulo.controller('TreinadorCtrl', function ($scope, $http, $rootScope, $location, cadastros, globais) {

    $rootScope.activetab = $location.path();
    cadastros.setScope($scope);
    cadastros.setHttp($http);
    cadastros.setBackEndFile("cad-treinador.php");

    $scope.setDia = function (aDia) {
        $scope.dia = aDia;
        $scope.diaFormatado = globais.formataData(aDia);
    }

    $scope.InicioSemana = function () {
        var tempDia = new Date();
        while (tempDia.getDay() !== 1) {
            tempDia.setDate(tempDia.getDate() - 1);
        }
        $scope.setDia(tempDia);
    };

    $scope.InicioSemana();

    $scope.diaDaSemana = function (nDia) {
        switch (nDia + 0) {
            case 0:
                return 'Domingo';
            case 1:
                return 'Segunda-feira';
            case 2:
                return 'Terça-feira';
            case 3:
                return 'Quarta-feira';
            case 4:
                return 'Quinta-feira';
            case 5:
                return 'Sexta-feira';
            case 6:
                return 'Sábado';
            default :
                return 'Dia indefinido ' + nDia;
        }
    };

    $scope.movimentaDia = function (arg)
    {
        if (arg === 2) {
            $scope.dia.setDate($scope.dia.getDate() + 7);
        } else {
            $scope.dia.setDate($scope.dia.getDate() - 7);
        }
        $scope.setDia($scope.dia);
        $scope.load();
    }

    $scope.changeTreino = function (item) {
        item.treino = item.descricao.split("\n");
        item.treinoLinhas = item.treino.length;
        if(item.treinoLinhas<4) item.treinoLinhas=4;
    };

    $scope.editar = function (item) {
        cadastros.editar(item);
    }
    $scope.novo = function () {
        var item = {
            id: 0,
            nome: '',
            senha: ''
        };
        cadastros.novo(item);
    }

    $scope.load = function ()
    {
        $http.post(
                cadastros.backEndFile,
                cadastros.load_params({}))
                .then(function (response) {
                    $scope.listaRegistros = response.data.registros;
                    cadastros.login();
                    for (i = 0; i < 7; i++) {
                        $scope.changeTreino($scope.listaRegistros[i]);
                    }
                });

    };

    $scope.Gravar = function () {
        var item;
        for (i = 0; i < 7; i++) {
            item = $scope.listaRegistros[i];
            this.editar(item);
            $scope.gravar(item, 1);
        }
    }

    $scope.gravar = function (item, acao) {
        cadastros.gravar_inicio(item, acao);
        $http.post(
                cadastros.backEndFile,
                cadastros.gravar_params(item, acao))
                .then(function (response) {
                    cadastros.gravar_acao_padrao(response, item);
                    item.ctrl = 1;
                });
    };
})

//--------------------------------------------------------------///

pawork_modulo.controller('LogAtividadesCtrl', function ($scope, $http, $rootScope, $location, globais, cadastros) {

    $rootScope.activetab = $location.path();
    cadastros.setScope($scope);
    cadastros.setHttp($http);
    cadastros.setBackEndFile("cad-logAtividades.php");

    $scope.setDia = function (aDia) {
        $scope.dia = aDia;
        $scope.diaFormatado = globais.formataData(aDia);
    }

    $scope.setDia(new Date());

    $scope.movimentaDia = function (arg)
    {
        if (arg === 2) {
            $scope.dia.setDate($scope.dia.getDate() + 1);
        } else {
            $scope.dia.setDate($scope.dia.getDate() - 1);
        }
        $scope.setDia($scope.dia);
        $scope.load();
    }

    $scope.editar = function (item) {
        cadastros.editar(item);
    }
    $scope.novo = function () {
        var item = {
            id: 0,
            dia: $scope.diaFormatado,
            inicio: '00:00',
            fim: '00:00',
            tempo: '00:00',
            atividade: '',
            descricao: '',
            listaAtividades: $scope.listaAtividades
        };
        cadastros.novo(item);
    }

    $scope.load = function ()
    {
        $http.post(
                cadastros.backEndFile,
                cadastros.load_params(
                        {
                            dia: $scope.diaFormatado
                        }
                ))
                .then(function (response) {
                    $scope.listaRegistros = response.data.registros;
                    $scope.listaAtividades = response.data.atividades;
                });

    };

    $scope.gravar = function (item, acao)
    {
        cadastros.gravar_inicio(item, acao);
        $http.post(
                cadastros.backEndFile,
                cadastros.gravar_params(item, acao))
                .then(function (response) {
                    cadastros.gravar_acao_padrao(response, item);
                });
    };

    $scope.change = function (item, coluna)
    {
        if (coluna === 'inicio' || coluna === 'fim') {
            item.tempo = globais.time_diff(item.inicio, item.fim);
        }
    };

})


//--------------------------------------------------------------///

pawork_modulo.controller('pawork_ctrl', function ($scope, $http, $location, $rootScope)
{

    $scope.verificarUsuarioSenha = function ()
    {
        $http.post(
                "login.php",
                {
                    params: {},
                    headers: {Authorization: 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='},
                    usuario: $scope.usuario,
                    senha: $scope.senha,
                    acao: 4
                }
        )
                .then(function (response) {
                    $scope.validacaoUsuario = response.data.resultado + " => " + response.data.codResultado;
                    if (response.data.codResultado === 1) {
                        $rootScope.usuarioValidado = $scope.usuario;
                        window.location.href = '/';
                    }
                    if (response.data.codResultado === undefined) {
                        $scope.validacaoUsuario = "Erro retornado:" + response.data;
                    }
                });
    };
})
//-------------------------------------------------

pawork_modulo.controller('pawork_logoff_ctrl', function ($scope, $http, $location, $rootScope)
{
    $rootScope.activetab = $location.path();

    $scope.desconectar = function ()
    {
        $http.post(
                "logoff.php",
                {
                    params: {},
                    headers: {Authorization: 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='}
                }
        )
                .then(function (response) {
                    $scope.validacaoUsuario = response.data.resultado + " => " + response.data.codResultado;
                    window.location.href = '/login.html';
                });

    };
})

// --------------------------------------------------

pawork_modulo.controller('ListaUserCtrl', function ($scope, $http) {

    $scope.load = function ()
    {
        $http.post(
                "lista-user.php",
                {
                    params: {}
                }
        )
                .then(function (response) {
                    $scope.listaRegistros = response.data.registros;
                });
    };

})


pawork_modulo.directive('ngAddrow', function () {
    return function (scope, element, attrs) {
        element.on("keydown", function (event) {
            if (event.which === 9) {
                scope.$apply(function () {
                    if (scope.$last) {

                        scope.$eval(attrs.ngAddrow);
                    }
                });
            }
        });
    };
});
