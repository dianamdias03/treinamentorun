package framework;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatacaoDatas {

    private Calendar calendar;
    private boolean valorNulo ;

    public String siglaSemana() {
        String retorno;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                retorno = "DOM";
                break;
            case Calendar.MONDAY:
                retorno = "SEG";
                break;
            case Calendar.TUESDAY:
                retorno = "TER";
                break;
            case Calendar.WEDNESDAY:
                retorno = "QUA";
                break;
            case Calendar.THURSDAY:
                retorno = "QUI";
                break;
            case Calendar.FRIDAY:
                retorno = "SEX";
                break;
            case Calendar.SATURDAY:
                retorno = "SAB";
                break;
            default:
                retorno = "";
        }
        return retorno;
    }

    public String diaSemana() {
        String retorno;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                retorno = "Domingo";
                break;
            case Calendar.MONDAY:
                retorno = "Segunda-feira";
                break;
            case Calendar.TUESDAY:
                retorno = "Terça-feira";
                break;
            case Calendar.WEDNESDAY:
                retorno = "Quarta-feira";
                break;
            case Calendar.THURSDAY:
                retorno = "Quinta-feira";
                break;
            case Calendar.FRIDAY:
                retorno = "Sexta-feira";
                break;
            case Calendar.SATURDAY:
                retorno = "Sábado";
                break;
            default:
                retorno = "";
        }
        return retorno;
    }

    public String siglaMes() {
        String retorno;
        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                retorno = "jan";
                break;
            case Calendar.FEBRUARY:
                retorno = "fev";
                break;
            case Calendar.MARCH:
                retorno = "mar";
                break;
            case Calendar.APRIL:
                retorno = "abr";
                break;
            case Calendar.MAY:
                retorno = "mai";
                break;
            case Calendar.JUNE:
                retorno = "jun";
                break;
            case Calendar.JULY:
                retorno = "jul";
                break;
            case Calendar.AUGUST:
                retorno = "ago";
                break;
            case Calendar.SEPTEMBER:
                retorno = "set";
                break;
            case Calendar.OCTOBER:
                retorno = "out";
                break;
            case Calendar.NOVEMBER:
                retorno = "nov";
                break;
            case Calendar.DECEMBER:
                retorno = "dez";
                break;
            default:
                retorno = "";
        }
        return retorno;
    }

    public String diaMes() {
        return String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "/" + siglaMes();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Date getDia() {
        return calendar.getTime();
    }

    public void setDia(Date dia) {
        calendar = Calendar.getInstance();
        calendar.setTime(dia);
    }

    public void setDia(String dia) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            calendar.setTime(sdf.parse(dia));
        } catch (ParseException ex) {
            Arquivo.gravarLog("Erro em conversao de data: " + dia);
            Arquivo.gravarLog("Erro: " + ex.getMessage());
        }
    }

    public FormatacaoDatas() {
        calendar = Calendar.getInstance();
    }

    public FormatacaoDatas(Date dia) {
        calendar = Calendar.getInstance();
        valorNulo = (dia == null);
        if (!valorNulo) {
            calendar.setTime(dia);
        };
    }

    public void addDia(int dias) {
        calendar.add(Calendar.DATE, dias);
    }

    public static Date ymdToDate(String strData) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dataConvertida = null;

        try {
            dataConvertida = new java.sql.Date(dateFormat.parse(strData).getTime());
        } catch (ParseException ex) {
            Arquivo.gravarLog("Erro em conversao de data: " + strData);
            Arquivo.gravarLog("Erro: " + ex.getMessage());
        }

        return dataConvertida;
    }

    public static Date dmyToDate(String strData) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date dataConvertida = null;

        try {
            dataConvertida = new java.sql.Date(dateFormat.parse(strData).getTime());
        } catch (ParseException ex) {
            Arquivo.gravarLog("Erro em conversao de data: " + strData);
            Arquivo.gravarLog("Erro: " + ex.getMessage());
        }

        return dataConvertida;
    }

    public String getDataYMD() {
        if(valorNulo) return null;
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(getDia());
    }

    public String getDataDMY() {
        if(valorNulo) return null;
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(getDia());
    }

    public String getDataDMY_HMS() {
        if(valorNulo) return null;
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(getDia());
    }

    public void setCurrentDate() {
        setDia(new Date());
    }

    public int getDiaSemanaN() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static void main(String[] args) {
        FormatacaoDatas formatacaoDatas = new FormatacaoDatas();
        formatacaoDatas.setCurrentDate();
        formatacaoDatas.addDia(-formatacaoDatas.getDiaSemanaN() + 2);

        System.out.println("Dia: " + formatacaoDatas.getDataYMD() + " " + formatacaoDatas.getDiaSemanaN());

        formatacaoDatas = new FormatacaoDatas();
        formatacaoDatas.setDia("1978-01-07 10:30:15");
        System.out.println("Data e hora: " + formatacaoDatas.getDataDMY_HMS());

        formatacaoDatas = new FormatacaoDatas();
        formatacaoDatas.setDia("1978-01-07");
        System.out.println("Data: " + formatacaoDatas.getDataDMY());
        System.out.println("Data: " + formatacaoDatas.getDataDMY_HMS());

    }

}
