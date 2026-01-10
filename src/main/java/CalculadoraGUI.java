import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculadoraGUI extends JFrame {
    private JPanel mainPanel;
    private JButton ceBoton;
    private JButton ceroBoton;
    private JButton comaBoton;
    private JButton unoBoton;
    private JButton dosBoton;
    private JButton tresBoton;
    private JButton cuatroBoton;
    private JButton cincoBoton;
    private JButton seisBoton;
    private JButton sieteBoton;
    private JButton ochoBoton;
    private JButton nueveBoton;
    private JButton porcBoton;
    private JButton divBoton;
    private JButton multBoton;
    private JButton menosBoton;
    private JButton masBoton;
    private JButton button7;
    private JTextField resulText;
    private JLabel resulLabel;
    private char signo;
    private double resultado;

    //Variable que sirve para saber si se ha modificado el valor inicial de la calculadora (apretado algun botón)
    private boolean seModifico = false;


    public CalculadoraGUI() {
        inicializarForma();
        ceroBoton.addActionListener(e -> agregarNumero(0));
        unoBoton.addActionListener(e -> agregarNumero(1));
        dosBoton.addActionListener(e -> agregarNumero(2));
        tresBoton.addActionListener(e -> agregarNumero(3));
        cuatroBoton.addActionListener(e -> agregarNumero(4));
        cincoBoton.addActionListener(e -> agregarNumero(5));
        seisBoton.addActionListener(e -> agregarNumero(6));
        sieteBoton.addActionListener(e -> agregarNumero(7));
        ochoBoton.addActionListener(e -> agregarNumero(8));
        nueveBoton.addActionListener(e -> agregarNumero(9));
        masBoton.addActionListener(e -> suma());
        menosBoton.addActionListener(e -> resta());
        multBoton.addActionListener(e -> multiplicacion());
        divBoton.addActionListener(e -> division());
        ceBoton.addActionListener(e -> limpiarCalculadora());
        comaBoton.addActionListener(e -> agregarComa());
        button7.addActionListener(e -> resultadoFinal());
    }

    //Función para modificar la vista de la cuenta ingresada con su signo respectivo (ej: 10 + 1)
    private void modificarLabel(double numeroIngresado, char signo) {


        if (signo == '=') {
            this.resulLabel.setText(resulLabel.getText()  + " " + numeroIngresado + " = ");

        }else {
            this.resulLabel.setText(numeroIngresado + " " + signo);
        }
    }


    private double resolverCuentaPendiente() {

        //Obtenemos la cuenta completa extraída del label (ej: 10 + 10)
        String operacion = (resulLabel.getText() + resulText.getText()).replace(" ", "");   //pasamos toda la operacion a string

        //Establecemos el patron por el cual queremos guardar cada valor (cualquier signo, 2 o mas dígitos, que tenga decimales de 2 o mas dígitos)
        Pattern pattern = Pattern.compile("[+\\-*/]?\\d+(?:\\.\\d+)?");
        Matcher matcher = pattern.matcher(operacion);
        double resul = 0;

        //Variable para determinar cual es el primer valor
        boolean esPrimerValor = true;

        //Recorremos la operación de 2 valores
        while (matcher.find()) {

            //Al primer valor lo guardamos en la variable de resul
            if(esPrimerValor) {
                resul = Double.parseDouble(matcher.group());
                esPrimerValor = false;
                continue;
            }

            //Se busca que tipo de operacion es (suma, resta, multiplicación o división)
            if (matcher.group().charAt(0) == '*') {

                resul *=  Double.parseDouble(matcher.group().substring(1));

            } else if (matcher.group().charAt(0) == '/') {

                //Verificamos que no sea una division por 0
                if (matcher.group().substring(1) == "0.0") {

                    this.resulText.setText("No se puede dividir entre 0");

                } else {

                    resul /= Double.parseDouble(matcher.group().substring(1));

                }
            } else if (matcher.group().charAt(0) == '+' || matcher.group().charAt(0) == '-' ){
                resul += Double.parseDouble(matcher.group());
            }
        }
        return resul;
    }

    //Métodos para agregar su signo correspondiente resolviendo una cuenta pendiente en caso de que lo haya ()
    private void suma() {
        //Obtenemos el signo correspondiente a la operación
        this.signo = '+';

        //Si se ha presionado algún botón y hay una cuenta pedndiente se resuelve y se muestra
        if (seModifico && !(resulLabel.getText().isEmpty())) {
            this.resultado = resolverCuentaPendiente();
            modificarLabel(this.resultado, signo);
            this.resulText.setText(String.valueOf(this.resultado));
            seModifico = false;
        } else {

            //Si es la primera vez que se presiona un operando se agrega junto con el número ingresado
            modificarLabel(Double.parseDouble(resulText.getText()), signo);
            seModifico = false;
        }
    }

    private void resta() {
        this.signo = '-';
        if (seModifico && !(resulLabel.getText().isEmpty())) {
            this.resultado = resolverCuentaPendiente();
            modificarLabel(this.resultado, signo);
            this.resulText.setText(String.valueOf(this.resultado));
            seModifico = false;
        } else {
            modificarLabel(Double.parseDouble(resulText.getText()), signo);
            seModifico = false;
        }
    }

    private void multiplicacion() {
        this.signo = '*';
        if (seModifico && !(resulLabel.getText().isEmpty())) {
            this.resultado = resolverCuentaPendiente();
            modificarLabel(this.resultado, signo);
            this.resulText.setText(String.valueOf(this.resultado));
            seModifico = false;
        } else {
            modificarLabel(Double.parseDouble(resulText.getText()), signo);
            seModifico = false;
        }
    }

    private void division() {

        this.signo = '/';
        if (seModifico && !(resulLabel.getText().isEmpty())) {
            this.resultado = resolverCuentaPendiente();
            modificarLabel(this.resultado, signo);
            this.resulText.setText(String.valueOf(this.resultado));
            seModifico = false;
        } else {
            modificarLabel(Double.parseDouble(resulText.getText()), signo);
            seModifico = false;
        }
    }

    //Método para agregar una coma al número ingresado anteriormente
    private void agregarComa() {
        this.resulText.setText(resulText.getText() + ".");
    }

    //Método para reiniciar los valores y limpiar la consola
    private void limpiarCalculadora() {
        this.resulText.setText("0");
        this.resultado = 0;
        this.resulLabel.setText("");
        seModifico = false;
    }


    //Método para mostrar resultado final
    private void resultadoFinal() {

        //Si se ha modificado la consola y no está vacía se resuelve la cuenta pendiente y se nuestra en la consola y en el label
        if (seModifico && !resulLabel.getText().isEmpty()) {
            String aux = resulText.getText();
            modificarLabel(Double.parseDouble(aux), '=');
            this.resultado = resolverCuentaPendiente();


            this.resulText.setText(String.valueOf(this.resultado));

            seModifico = false;

            //Si no se ha modificado la consola o no hay ninguna cuenta pendiente solo se imprime el número que está en la consola con el último signo presionado
        } else {
            modificarLabel(Double.parseDouble(resulText.getText()), signo);
            seModifico = false;
        }

    }

    //Funcion para agregar un número
    private void agregarNumero(int num) {

        //Si no se ha agregado ningún número se agrega el correspondiente
        if (!seModifico) {

            this.resulText.setText(String.valueOf(num));
            seModifico = true;
        } else {

            //Si ya se ha agregado un número se agrega al lado del ya ingresado anteriormente
            this.resulText.setText(this.resulText.getText() + num);
        }

    }

    private void inicializarForma() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 330);
        setLocationRelativeTo(null);
    }


    public static void main(String[] args) {
        CalculadoraGUI calculadoraGUI = new CalculadoraGUI();
        calculadoraGUI.setVisible(true);
    }

}
