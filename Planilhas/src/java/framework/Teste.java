/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import java.util.Date;

public class Teste {

    public static void main(String[] args) {
        String lsDiaInicio = "01/01/2016";
        String lsDiaFim = "31/12/2017";
        FormatacaoDatas formatacaoDatas;

        Date diaInicio;
        Date diaFim;

        diaInicio = FormatacaoDatas.dmyToDate(lsDiaInicio);
        diaFim = FormatacaoDatas.dmyToDate(lsDiaFim);

        formatacaoDatas = new FormatacaoDatas(diaInicio);

        while (diaFim.after(formatacaoDatas.getDia())) {
            formatacaoDatas.addDia(8);
            formatacaoDatas.setDia(formatacaoDatas.getDia());
            System.out.println(
                    formatacaoDatas.getDataYMD()
                    + " " + formatacaoDatas.getDataDMY()
                    + " " + formatacaoDatas.siglaMes()
                    + " " + formatacaoDatas.siglaSemana()
                    + " " + formatacaoDatas.diaMes()
                    + " >" + formatacaoDatas.getDia().toString()
            );
        }

    }
}
