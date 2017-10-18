-- CREATE DATABASE treinos;
-- USE treinos;

CREATE TABLE clientes (
     i_clientes INT NOT NULL AUTO_INCREMENT,
     nome CHAR(128) NOT NULL,
     PRIMARY KEY (i_clientes)
);

-- DROP TABLE usuarios;
CREATE TABLE usuarios (
  i_usuarios int(11) NOT NULL AUTO_INCREMENT,
  i_clientes int(11) NOT NULL,
  nome char(128) NOT NULL,
  email char(128) NOT NULL,
  senha char(128) NOT NULL,
  admin int(11) NOT NULL,
  cria_planilhas int(11) NOT NULL,
  cria_usuarios int(11) NOT NULL,
  cria_eventos int(11) NOT NULL,
  recebe_planilha int(11) NOT NULL,
  cpf varchar(14) DEFAULT NULL,
  rg varchar(14) DEFAULT NULL,
  endereco varchar(64) DEFAULT NULL,
  cidade varchar(64) DEFAULT NULL,
  estado varchar(2) DEFAULT NULL,
  cep varchar(10) DEFAULT NULL,
  observacoes varchar(2048) DEFAULT NULL,
  data_nascto date DEFAULT NULL,
  telefone_1 varchar(20) DEFAULT NULL,
  telefone_2 varchar(20) DEFAULT NULL,
  PRIMARY KEY (i_usuarios)
);

insert into clientes (nome) values('M3 Acessoria');
insert into clientes (nome) values('Prorunner');

insert into usuarios (i_clientes, nome, email, senha, admin, cria_planilhas, cria_usuarios, cria_eventos, recebe_planilha) values(1, 'Marcelo Olimpio', 'marcelo@m3.com.br', 'marcelo', 0, 1, 1, 1, 1);
insert into usuarios (i_clientes, nome, email, senha, admin, cria_planilhas, cria_usuarios, cria_eventos, recebe_planilha) values(1, 'Ana', 'ana@m3.com.br', 'ana', 0, 0, 1, 1, 1);
insert into usuarios (i_clientes, nome, email, senha, admin, cria_planilhas, cria_usuarios, cria_eventos, recebe_planilha) values(1, 'Filipe', 'filipe@m3.com.br', 'filipe', 1, 0, 0, 0, 1);
insert into usuarios (i_clientes, nome, email, senha, admin, cria_planilhas, cria_usuarios, cria_eventos, recebe_planilha) values(1, 'Adriano', 'adriano@m3.com.br', 'adriano', 1, 0, 0, 0, 1);
insert into usuarios (i_clientes, nome, email, senha, admin, cria_planilhas, cria_usuarios, cria_eventos, recebe_planilha) values(1, 'Fabi', 'fabi@m3.com.br', 'fabi', 0, 0, 0, 0, 1);

create table tipos_modalidades (
    i_tipos_modalidades INT NOT NULL AUTO_INCREMENT,
    i_clientes int not null,
    descricao varchar(64) NOT NULL,
    PRIMARY KEY (i_tipos_modalidades)
);

insert into tipos_modalidades (i_clientes,descricao) values( 1, 'Corrida' );
insert into tipos_modalidades (i_clientes,descricao) values( 1, 'Natação' );
insert into tipos_modalidades (i_clientes,descricao) values( 1, 'Ciclismo' );
insert into tipos_modalidades (i_clientes,descricao) values( 1, 'Folga' );

create table tipos_intensidades (
    i_tipos_intensidades INT NOT NULL AUTO_INCREMENT,
    i_clientes int not null,
    descricao varchar(64) NOT NULL,
    PRIMARY KEY (i_tipos_intensidades)
);

insert into tipos_intensidades (i_clientes,descricao) values( 1, 'Não informado' );
insert into tipos_intensidades (i_clientes,descricao) values( 1, 'Z1 - Leve' );
insert into tipos_intensidades (i_clientes,descricao) values( 1, 'Z2 - Moderado' );
insert into tipos_intensidades (i_clientes,descricao) values( 1, 'Z3 - Firme' );
insert into tipos_intensidades (i_clientes,descricao) values( 1, 'Z4 - Forte' );
insert into tipos_intensidades (i_clientes,descricao) values( 1, 'Z5 - Muito forte' );

create table tipos_percursos (
    i_tipos_percursos INT NOT NULL AUTO_INCREMENT,
    i_clientes int not null,
    descricao varchar(64) NOT NULL,
    PRIMARY KEY (i_tipos_percursos)
);

insert into tipos_percursos (i_clientes,descricao) values( 1, 'Não informado' );
insert into tipos_percursos (i_clientes,descricao) values( 1, 'Plano' );
insert into tipos_percursos (i_clientes,descricao) values( 1, 'Subidas leves' );
insert into tipos_percursos (i_clientes,descricao) values( 1, 'Subidas fortes' );
insert into tipos_percursos (i_clientes,descricao) values( 1, 'Terreno variado com subidas' );

create table tipos_treinos (
    i_tipos_treinos INT NOT NULL AUTO_INCREMENT,
    i_clientes int not null,
    descricao varchar(64) NOT NULL,
    PRIMARY KEY (i_tipos_treinos)
);

insert into tipos_treinos (i_clientes,descricao) values( 1, 'Não informado' );
insert into tipos_treinos (i_clientes,descricao) values( 1, 'Continuo' );
insert into tipos_treinos (i_clientes,descricao) values( 1, 'Intervalado' );
insert into tipos_treinos (i_clientes,descricao) values( 1, 'Fartlek' );
insert into tipos_treinos (i_clientes,descricao) values( 1, 'Regenerativo' );
insert into tipos_treinos (i_clientes,descricao) values( 1, 'Ritmo' );
insert into tipos_treinos (i_clientes,descricao) values( 1, 'Longo' );
insert into tipos_treinos (i_clientes,descricao) values( 1, 'Prova' );

create table tipos_distancias (
    i_tipos_distancias INT NOT NULL AUTO_INCREMENT,
    i_clientes int not null,
    descricao varchar(64) NOT NULL,
    PRIMARY KEY (i_tipos_distancias)
);

insert into tipos_distancias (i_clientes,descricao) values( 1, 'km' );
insert into tipos_distancias (i_clientes,descricao) values( 1, 'metros' );
insert into tipos_distancias (i_clientes,descricao) values( 1, 'milhas' );

create table micro_ciclo (
    i_micro_ciclo INT NOT NULL AUTO_INCREMENT,
    i_clientes INT NOT NULL,
    i_usuarios INT NOT NULL,
    inicio date NOT NULL,
    fim date NOT NULL,
    situacao int NOT NULL, /*0=nao concluido, 1=oncluido, 2=sem treino no ciclo*/
    comentario_treinador varchar(2048) NOT NULL,
    comentario_atleta varchar(2048) NOT NULL,
    PRIMARY KEY (i_micro_ciclo)
);

create table micro_ciclo_treinos (
    i_micro_ciclo_treinos INT NOT NULL AUTO_INCREMENT,
    i_micro_ciclo INT NOT NULL,
    i_clientes INT NOT NULL,
    i_usuarios INT NOT NULL,
    tipo INT NOT NULL, /*1=planejado, 2=feedback do atleta*/
    dia date NOT NULL,
    ordem int NOT NULL,
    i_tipos_modalidades int not null,
    i_tipos_intensidades int not null,
    i_tipos_treinos int not null,
    i_tipos_distancias int not null,
    i_tipos_percursos int not null,
    descricao varchar(2048) NOT NULL,
    tempo_treino_minimo int not null,
    tempo_treino_maximo int not null,
    tempo_treino_realizado int not null,
    fc_media int not null,
    distancia numeric(13,2) not null,
    i_micro_ciclo_treinos_planejado INT NOT NULL, /*link do treino realizado com o treino planejado*/
    PRIMARY KEY (i_micro_ciclo_treinos)
);


INSERT INTO micro_ciclo (i_clientes, i_usuarios, inicio, fim, situacao, comentario_treinador, comentario_atleta) VALUES ('1', '4', '2017-09-18', '2017-09-24', '0', 'treinador', 'atleta');

INSERT INTO micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado) VALUES ('1', '1', '4', '4', '2017-09-18', '1', '1', '1', '1', '1', '1', 'testetetete', '0', '0', '0', '150', '15', '0');
INSERT INTO micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado) VALUES ('1', '1', '4', '4', '2017-09-19', '1', '1', '1', '1', '1', '1', 'testetetete', '0', '0', '0', '150', '15', '0');
INSERT INTO micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado) VALUES ('1', '1', '4', '4', '2017-09-20', '1', '1', '1', '1', '1', '1', 'testetetete', '0', '0', '0', '150', '15', '0');
INSERT INTO micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado) VALUES ('1', '1', '4', '4', '2017-09-21', '1', '1', '1', '1', '1', '1', 'testetetete', '0', '0', '0', '150', '15', '0');
INSERT INTO micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado) VALUES ('1', '1', '4', '4', '2017-09-22', '1', '1', '1', '1', '1', '1', 'testetetete', '0', '0', '0', '150', '15', '0');
INSERT INTO micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado) VALUES ('1', '1', '4', '4', '2017-09-22', '2', '1', '1', '1', '1', '1', 'Segundo treino do dia', '0', '0', '0', '150', '15', '0');
INSERT INTO micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado) VALUES ('1', '1', '4', '4', '2017-09-23', '1', '1', '1', '1', '1', '1', 'testetetete', '0', '0', '0', '150', '15', '0');
INSERT INTO micro_ciclo_treinos (i_micro_ciclo, i_clientes, i_usuarios, tipo, dia, ordem, i_tipos_modalidades, i_tipos_intensidades, i_tipos_treinos, i_tipos_distancias, i_tipos_percursos, descricao, tempo_treino_minimo, tempo_treino_maximo, tempo_treino_realizado, fc_media, distancia, i_micro_ciclo_treinos_planejado) VALUES ('1', '1', '4', '4', '2017-09-24', '1', '1', '1', '1', '1', '1', 'testetetete', '0', '0', '0', '150', '15', '0');


-- 30/09/2017 --------------------------------
create table semana (
    i_dia integer primary key,
    nome char(32),
    sigra char(3)
);
insert into semana values( 1, 'Domingo', 'Dom');
insert into semana values( 2, 'Segunda-feira', 'Seg');
insert into semana values( 3, 'Terça-feira', 'Ter');
insert into semana values( 4, 'Quarta-feira', 'Qua');
insert into semana values( 5, 'Quinta-feira', 'Qui');
insert into semana values( 6, 'Sexta-feira', 'Sex');
insert into semana values( 7, 'Sabado', 'Sab');


-- 08/10/2017 --------------------------------
alter table micro_ciclo_treinos add feedback varchar(2048);

-- 16/10/2017 --------------------------------
create table treinosPreCadastrados(
	i_treinosPreCadastrados  INT NOT NULL AUTO_INCREMENT,
	descricao varchar(2048),
    primary key(i_treinosPreCadastrados)
);
insert into treinosPreCadastrados (descricao) values('- 5min caminhada firme\n- 60min(8min corrida leve + 2min caminhada firme)' );
insert into treinosPreCadastrados (descricao) values('- 5min caminhada firme\n- 5x( 8min corrida leve+1min caminhada)\n- 5min caminhada bem firme');
insert into treinosPreCadastrados (descricao) values('- 6x( 3min caminhada firme+- 5min corrida leve+2min corrida firme)');
insert into treinosPreCadastrados (descricao) values('- 10min caminhada bem firme\n- 50min( 8min corrida leve +2min caminhada)');
insert into treinosPreCadastrados (descricao) values('- 5min caminhada firme\n- 60min(5min corrida leve + 1min caminhada firme)');
insert into treinosPreCadastrados (descricao) values('- 5min caminhada firme\n- 5x( 7min corrida leve+1min caminhada)');
insert into treinosPreCadastrados (descricao) values('- 7x(2min caminhada leve+7min corrida leve)');
insert into treinosPreCadastrados (descricao) values('- 5min caminhada forte\n- 5x(1min corrida bem forte+2min caminhada)');
insert into treinosPreCadastrados (descricao) values('- 10min caminhada firme\n- 40min(4min corrida leve + 1min caminhada firme)');
insert into treinosPreCadastrados (descricao) values('- 5min caminhada firme\n- 45min( 3min corrida leve+2min caminhada)');
insert into treinosPreCadastrados (descricao) values('- 6x(3min caminhada leve+7min corrida leve)');


