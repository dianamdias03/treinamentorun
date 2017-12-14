package treinos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class TreinoGraficoGerarImagem {

    ArrayList<ParteTreino> treinos;
    Graphics2D g2;
    int contaInstrucoes;
    boolean lbDayOff;

    public void setTreino(String textoTreino) {
        treinos = new ArrayList<>();
//        treinos.add("2km leve");
//        treinos.add("2x3km pace 4'30''");
//        treinos.add("2x2 min intervalo");
//        treinos.add("2km leve");

        lbDayOff = textoTreino.equals("Dia OFF");

        contaInstrucoes = 0;
        String linhas[] = textoTreino.split("\n");

        for (int i = 0; i < linhas.length; i++) {

            if (linhas[i].startsWith("-")) {
                linhas[i] = linhas[i].substring(1);
            }

            Pattern p = Pattern.compile("[0-9]+x");
            Matcher m = p.matcher(linhas[i]);

            if (m.find()) {

                String padrao = m.group(0);

                linhas[i] = linhas[i].substring(padrao.length());

                linhas[i] = linhas[i].replaceAll("^ *\\( *", "");
                linhas[i] = linhas[i].replaceAll("\\)$", "");

                String partes[] = linhas[i].split("(\\+|/)");
                ParteTreino parteTreino = new ParteTreino(padrao);

                for (String parte : partes) {

                    parte = parte.replaceAll("^ *\\( *", "");
                    parte = parte.replaceAll("\\)$", "");

                    parteTreino.addConteudo(parte);
                    contaInstrucoes++;
                }
                treinos.add(parteTreino);
            } else {
                ParteTreino parteTreino = new ParteTreino("");
                parteTreino.addConteudo(linhas[i]);
                treinos.add(parteTreino);
                contaInstrucoes++;
            }

//            linhas[i] = linhas[i];
//            treinos.add(linhas[i]);
        }

    }

    public void drawRotate(double x, double y, int angle, String text, float tamanho) {
        g2.translate((float) x, (float) y);
        g2.rotate(Math.toRadians(angle));
        //g2.drawString(text, 0, 0);
        drawText(x, y, text, tamanho);
        g2.rotate(-Math.toRadians(angle));
        g2.translate(-(float) x, -(float) y);
    }

    public void drawText(double x, double y, String texto, float tamanho) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\temp\\01\\Caviar_Dreams_Bold.ttf"));
            font = font.deriveFont(tamanho);
            g2.setFont(font);
//            g2.drawString(texto, 30, 75);
            g2.drawString(texto, 0, 0);
            FontMetrics fontMetrics = g2.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(texto);
            int stringHeight = fontMetrics.getAscent();
            System.out.println(texto + "=" + stringWidth + "; " + stringHeight);

        } catch (FontFormatException ex) {
            Logger.getLogger(TreinoGraficoGerarImagem.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TreinoGraficoGerarImagem.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeFont(int tamanho) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\temp\\01\\Caviar_Dreams_Bold.ttf"));
            font = font.deriveFont(tamanho);
            g2.setFont(font);

        } catch (FontFormatException ex) {
            Logger.getLogger(TreinoGraficoGerarImagem.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TreinoGraficoGerarImagem.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    static public void main(String args[]) throws Exception {
        TreinoGraficoGerarImagem writeImageType = new TreinoGraficoGerarImagem();

        writeImageType.test();
    }

    public BufferedImage diaSemana(String dia) {

        int x = 1;
        int y = 1;
        int w = 500;
        int h;
        int xEspacoSemana = 60;
        int espaco = 10;
        int xTreino = x + xEspacoSemana;
        int wTreino = w - xEspacoSemana;
        int hTreino = 50;
        int yTreino = y;

        if (contaInstrucoes < 2) {
            hTreino *= 2;
        }

        int width = x + w + 2;
        int height = contaInstrucoes * (hTreino + espaco) + y + espaco + 2;
        height += espaco * 2;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        changeFont(30);

        //---- Treino - Inicio ----------------
        xTreino = xTreino + espaco;
        wTreino = wTreino - espaco * 2;

        int xTreinoRecuo = xTreino + 70;

        int xInstrucao;
        int wInstrucao;
        int yInicioGrupo;

        yTreino += espaco;
        for (ParteTreino treino : treinos) {

            yInicioGrupo = yTreino;

            for (String instrucao : treino.getConteudo()) {
//                System.out.println("     " + instrucao);
//                yTreino += hTreino + espaco;

                if (treino.getNome().equals("")) {
                    xInstrucao = xTreino;
                    wInstrucao = wTreino;
                } else {
                    xInstrucao = xTreinoRecuo;
                    wInstrucao = wTreino - (xTreinoRecuo - xTreino);
                }

                drawParteTreinoInstrucao(xInstrucao, yTreino, wInstrucao, hTreino, espaco, instrucao);
                yTreino += hTreino + espaco;

            }

            if (!treino.getNome().equals("")) {
                drawParteTreinoGrupo(
                        xTreino,
                        yInicioGrupo,
                        xTreinoRecuo - xTreino - espaco,
                        yTreino - yInicioGrupo - espaco,
                        treino.getNome()
                );
            }

        }
        yTreino -= espaco;
        h = yTreino - y + espaco;
        //---- Treino - Fim ----------------

        // ---- Semana - inicio ----------
        if (!lbDayOff) {
            g2.setPaint(new Color(249, 192, 7));
        } else {
            g2.setPaint(new Color(192, 192, 192));
        }
        g2.setStroke(new BasicStroke(2.0F));
        g2.drawRoundRect(x, y, w, h, 10, 10);

        g2.fill(new RoundRectangle2D.Double(x, y, xEspacoSemana, h, 10, 10));
        g2.fill(new Rectangle2D.Double(x + xEspacoSemana - 5, y, 5, h));

        g2.setPaint(new Color(84, 69, 20));

        Fonte fonte = new Fonte(g2);
        fonte.setDef(x, y, 50, -90, dia);
        fonte.setX(fonte.getX() + fonte.getTamanho());
        fonte.setY(fonte.getY() + fonte.getTextWidth() / 2 + h / 2);
        fonte.drawText();
        // ---- Semana - fim ----------

        return bi;

    }

    public int drawParteTreinoInstrucao(int xTreino, int yTreino, int wTreino, int hTreino, int espaco, String texto) {
        if (!lbDayOff) {
            g2.setPaint(new Color(77, 213, 72));
        } else {
            g2.setPaint(new Color(150, 150, 150));
        }
        g2.fill(new RoundRectangle2D.Double(xTreino, yTreino, wTreino, hTreino, 10, 10));

        g2.setPaint(new Color(255, 255, 255));
        Fonte fonte = new Fonte(g2);
        fonte.setDef(xTreino + espaco, yTreino, 25, 0, texto);
        fonte.setY(fonte.getY() + fonte.getTextHeight() / 2);
        fonte.setY(fonte.getY() + hTreino / 2);
        fonte.drawText();

        return 0;
    }

    public void drawParteTreinoGrupo(int x, int y, int w, int h, String texto) {

        Quadro quadroRecuo = new Quadro(x, y, w, h);

//        g2.setPaint(Color.WHITE);
//        g2.fill(new Rectangle2D.Double(quadroRecuo.getX(), quadroRecuo.getY(), quadroRecuo.getW(), quadroRecuo.getH()));
        g2.setPaint(new Color(100, 100, 100));

        Fonte fonte = new Fonte(g2);
        fonte.setDef(quadroRecuo.getMiddleX(), quadroRecuo.getMiddleY(), 25, 0, texto);
        fonte.centerMiddle();
        fonte.drawText();

        g2.fill(new Rectangle2D.Double(quadroRecuo.getX() + quadroRecuo.getW(), quadroRecuo.getY(), 5, quadroRecuo.getH()));

    }

    public void gerarDiaSemana(String dia, String textoTreino, String arquivo) throws Exception {
        try {

            setTreino(textoTreino);
            BufferedImage bi = diaSemana(dia);

//            for (ParteTreino treino : treinos) {
//                System.out.println(treino.getNome());
//                for (String instrucao : treino.getConteudo()) {
//                    System.out.println("     " + instrucao);
//                }
//            }
            ImageIO.write(bi, "PNG", new File(arquivo));

        } catch (IOException ie) {
            ie.printStackTrace();

        }

    }

    public void test() throws Exception {

        String textoTreino;

        textoTreino = "Dia OFF";
        gerarDiaSemana("SEG", textoTreino, "c:\\temp\\01\\seg.png");

        textoTreino = "-5min corrida leve\n"
                + "-3x( 5min corrida leve+1min caminhada+3min corrida bem firme+2min caminhada)\n"
                + "T:35min";

        gerarDiaSemana("TER", textoTreino, "c:\\temp\\01\\ter.png");

        textoTreino = "Dia OFF";

        gerarDiaSemana("QUA", textoTreino, "c:\\temp\\01\\qua.png");

        textoTreino = "-2km leve\n"
                + "-2x3km pace 4'30''+( 2'i)\n"
                + "-2km leve\n"
                + "T:10km";

        gerarDiaSemana("QUI", textoTreino, "c:\\temp\\01\\qui.png");

        textoTreino = "-2km leve\n"
                + "-3x2km (abaixo de 8min40seg)/interv freq baixar 120bpm\n"
                + "-2km solto\n"
                + "T:10km";

        gerarDiaSemana("SAB", textoTreino, "c:\\temp\\01\\sab.png");

        textoTreino = "Dia OFF";

        gerarDiaSemana("DOM", textoTreino, "c:\\temp\\01\\dom.png");

    }

}

class Fonte {

    Graphics2D g2;
    private String text;
    private double x;
    private double y;
    private int angle;
    private float tamanho;

    public Fonte(Graphics2D g2) {
        this.g2 = g2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public float getTamanho() {
        return tamanho;
    }

    public void setTamanho(float tamanho) {
        this.tamanho = tamanho;
    }

    public double getTextWidth() {
        return getFontMetrics().stringWidth(getText());
    }

    public int getTextHeight() {
        return getFontMetrics().getAscent();
    }

    public FontMetrics getFontMetrics() {
        defineFonte();
        return g2.getFontMetrics();
    }

    public void drawText() {

        defineFonte();
//        if (getAngle() != 0) {
        g2.translate((float) x, (float) y);
        g2.rotate(Math.toRadians(angle));

        g2.drawString(text, 0, 0);
        g2.rotate(-Math.toRadians(angle));
        g2.translate(-(float) x, -(float) y);
//        } else {
//            g2.drawString(text, (float) x, (float) y);
//        }
    }

    public void setDef(float x, float y, float tamanho, int angle, String text) {
        setAngle(angle);
        setTamanho(tamanho);
        setText(text);
        setX(x);
        setY(y);
    }

    public void defineFonte() {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\temp\\01\\Caviar_Dreams_Bold.ttf"));
            font = font.deriveFont(getTamanho());
            g2.setFont(font);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Fonte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void centerMiddle() {
        setY(getY() + getTextHeight() / 2);
        setX(getX() - getTextWidth() / 2);
    }
}

class Quadro {

    private int x;
    private int y;
    private int w;
    private int h;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Quadro(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getMiddleX() {
        return getX() + getW() / 2;
    }

    public int getMiddleY() {
        return getY() + getH() / 2;
    }
}

class ParteTreino {

    private String nome;
    private ArrayList<String> conteudo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getConteudo() {
        return conteudo;
    }

    public void setConteudo(ArrayList<String> conteudo) {
        this.conteudo = conteudo;
    }

    public void addConteudo(String instrucao) {
        this.conteudo.add(instrucao);
    }

    public ParteTreino(String nome) {
        this.nome = nome;
        this.conteudo = new ArrayList<>();
    }
}
