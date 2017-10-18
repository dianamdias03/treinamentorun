
var treinoApp = angular.module('TreinoApp', ['ngRoute', 'ngSanitize']);

treinoApp.controller('TreinadorCtrl', function ($scope, $rootScope, $location, $http, cadastros, $sanitize)
{
    $scope.listaRegistros = [];
    $scope.microCicloAtleta = [];
    $scope.treinosPreCadastrados = [];
    $scope.opcoesMenu = cadastros.getMenus();
    $scope.nomeCliente = cadastros.getNomeCliente();
    cadastros.setScope($scope);

    $scope.selecionarAtleta = function (item) {
        $scope.atletaSelecionado = true;
        $scope.atleta = item;
        $scope.loadMicroCicloAtleta(0);
    };

    $scope.newLineToBR = function (text) {
        var lista = [];
        var arrayStr = [];

        if (text === undefined) {
            lista.push({"linha": 0, "descricao": ''});
        } else {
            arrayStr = text.split("\n");

            for (var i = 0; i < arrayStr.length; i++) {
                lista.push({"linha": i, "descricao": arrayStr[i]});
            }
        }

        return lista;
    };

    $scope.loadMicroCicloAtleta = function (navegacao)
    {
        var diaInicio = "2017-09-18";
        var codigo_atleta;

        if ($scope.atletaSelecionado) {
            codigo_atleta = $scope.atleta.codigo;
        } else {
            codigo_atleta = 1;
        }

        if ($scope.microCicloAtleta.length > 0) {
            diaInicio = $scope.microCicloAtleta[0].inicio;
        }

        $http.post(
                "select.jsp",
                {
                    params: {
                        "tabela": "atleta_micro_ciclo",
                        "i_usuarios": codigo_atleta,
                        "dia": diaInicio,
                        "navegacao": navegacao
                    }
                }
        )
                .then(function (response) {
                    if (response.data.resultado) {
                        $scope.microCicloAtleta = response.data.registros;
                        if ($scope.atletaSelecionado) {
                            $scope.loadMicroCicloTreinosAtleta($scope.microCicloAtleta[0].i_micro_ciclo);
                        } else {
                            $scope.microCicloTreinosAtleta = [];
                            $scope.loadAtletas();
                        }
                    }
                });
    };

    $scope.loadMicroCicloTreinosAtleta = function (i_micro_ciclo)
    {
        $http.post(
                "select.jsp",
                {
                    params: {
                        "tabela": "atleta_micro_ciclo_treinos",
                        "i_usuarios": $scope.atleta.codigo,
                        "i_micro_ciclo": i_micro_ciclo,
                        "dia": $scope.microCicloAtleta[0].inicio
                    }
                }
        )
                .then(function (response) {
                    $scope.microCicloTreinosAtleta = response.data.registros;
                });
    };

    $scope.loadAtletas = function ()
    {
        if ($scope.microCicloAtleta.length > 0) {
            dia = $scope.microCicloAtleta[0].inicio;
        } else {
            dia = '2017-10-02';
        }
        $http.post("select.jsp", {params: {"tabela": "atletas", "dia": dia}}).then(function (response) {
            $scope.dataParam = response.data;
            $scope.listaRegistros = response.data.registros;
            $scope.conta = 0;
            $scope.contaOk = 0;
            for (i = 0; i < $scope.listaRegistros.length; i++) {
                if ($scope.listaRegistros[i].Dia2 !== -1 &&
                        $scope.listaRegistros[i].Dia3 !== -1 &&
                        $scope.listaRegistros[i].Dia4 !== -1 &&
                        $scope.listaRegistros[i].Dia5 !== -1 &&
                        $scope.listaRegistros[i].Dia6 !== -1 &&
                        $scope.listaRegistros[i].Dia7 !== -1 &&
                        $scope.listaRegistros[i].Dia8 !== -1) {
                    $scope.contaOk++;
                }
                $scope.conta++;
            }
            $scope.progresso = Math.round($scope.contaOk / $scope.conta * 100);
//            if (!$scope.ehTreinador){
//                $scope.selecionarAtleta($scope.listaRegistros[0]);
//            }
        });
    }

    $scope.load = function ()
    {
        $http.post("sessao.jsp", {}).then(function (response) {
            $scope.dadosSessao = response.data;
            if(!$scope.dadosSessao.logado){
                document.location.href = './login.html';
            };
        });

        $scope.loadMicroCicloAtleta(0);

        $http.post("select.jsp", {params: {"tabela": "tipos_modalidades"}}).then(function (response) {
            $scope.tiposModalidades = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tabela": "tipos_treinos"}}).then(function (response) {
            $scope.tiposTreinos = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tabela": "tipos_intensidades"}}).then(function (response) {
            $scope.tiposIntensidades = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tabela": "tipos_percursos"}}).then(function (response) {
            $scope.tiposPercursos = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tabela": "tipos_distancias"}}).then(function (response) {
            $scope.tiposDistancias = response.data.registros;
        });
    };

    $scope.gravar = function (item)
    {
        $http.post("gravar.jsp", {params: {"tabela": "micro_ciclo_treinos", "dados": item}}).then(function (response) {
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
                        "tabela": "micro_ciclo_treinos",
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

    $scope.enviarPlanilha = function (item) {
        var lParams = {
            params:
                    {
                        "enviarPlanilha": $scope.microCicloAtleta[0],
                        "atleta": $scope.atleta

                    }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
        });
    }

    $scope.TreinoFixos = function (mc, mct, tipo) {
        var lParams = {
            params:
                    {
                        "tabela": "micro_ciclo_treinos",
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
                response.data.registro.ctrl_status = 1;
                if (tipo === 1) {
                    response.data.registro.tipos_modalidades.codigo = 4;
                }
                if (tipo === 2) {
                    response.data.registro.tipos_modalidades.codigo = 1;
                    response.data.registro.descricao = 'Treino coletivo as 19:30 no Parque das Nacoes';
                }
                $scope.xxx = response.data.registro;
                mct.Itens.push(response.data.registro);
                $scope.gravar(response.data.registro);
            }
        });
    }

    $scope.loadTreinosPreCadastrados = function () {
        $http.post(
                "gravar.jsp",
                {
                    params: {
                        "acao-gravar": "consultaSQL",
                        "consulta": "treinosPreCadastrados"
                    }
                }
        )
                .then(function (response) {
                    if (response.data.resultado === true) {
                        $scope.treinosPreCadastrados = response.data.dados;
                    }
                });
    };

    $scope.excluir = function (mct_dia, mct) {

        var lParams = {
            params: {
                "tabela": "micro_ciclo_treinos",
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

    $scope.desSelecionarAtleta = function () {
        $scope.atletaSelecionado = false;
        $scope.loadMicroCicloAtleta(0);
    };

    $scope.desSelecionarAtleta();
    $scope.loadTreinosPreCadastrados();

    $scope.showTreinosPreCadastrados = function (item) {
        $scope.registroEmEdicao = item;
        $('#treinosPreCadastrados').modal('show');
    }

    $scope.selecionaTreinosPreCadastrados = function (item) {
        $scope.registroEmEdicao.descricao = item.descricao;
        $('#treinosPreCadastrados').modal('hide');
    }




})