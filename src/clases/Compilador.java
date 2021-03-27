/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import GUI.VistaCompilador;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Ronaldo
 */
public class Compilador {
    String entrada;
    public void Compilador(String entrada){
        this.entrada = entrada;
    }
    
    public String analizarArchivo(){
        //Metodo para cargar documento con código fuente
        JFileChooser docSeleccionado = new JFileChooser();//Cuadro de dialogo para seleccionar documento
        File documento;
        if (docSeleccionado.showDialog(null, "Abrir el archivo seleccionado") == JFileChooser.APPROVE_OPTION) {
            documento = docSeleccionado.getSelectedFile();// se seleccionar documento
            if (documento.canRead()) {
                if (documento.getName().endsWith("txt") || documento.getName().endsWith("cpp")) {//Solo se cargar documentos de texto o de c++
                    String informacionDocumento = "";
                    FileInputStream datosEntrada = null;
                    try {
                        datosEntrada = new FileInputStream(documento); // Se verifica si se puede abrir
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(VistaCompilador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    int ascci;
                    // se lee linea por linea del documento de texto
                    try {
                        while ((ascci = datosEntrada.read()) != -1) {
                            char tipoDato = (char) ascci;
                            informacionDocumento += tipoDato;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(VistaCompilador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return informacionDocumento;
                    //codigoFuente.setText(informacionDocumento);// El texto que se encuentra en el documento, se muestra en el textArea
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione un documento de texto");
                    return "";
                }
            } else return "";
            
        } else return "";
    }
    
    public String analizarTexto(String info){
        String cad=info.trim();
        String resultado = "";
        int i=0;
        
        //eliminamos los espacios en blanco al inicio o al final (pero no a la mitad)
        while(i<cad.length()){//recorremos la línea
            
            //cad.split(";");
            char t=cad.charAt(i);//vamos leyendo caracter por caracter
            
            if(Character.isDigit(t)){//comprobamos si es un dígito
                String ora="";
                ora+=t;
                int j=i+1;
                while(Character.isDigit(cad.charAt(j))){
                    //mientras el siguiente elemento sea un numero
                    ora+=cad.charAt(j);//concatenamos
                    j++;
                    if(j==cad.length())break;//rompemos si llegamos al final de la línea
                }
                i=j;//movemos a nuestra variable i en la cadena
                resultado +=" NUM [ " + ora + " ], ";
                //resultadoCompilacion.append(" NUM [ " + ora + " ], ");
                //System.out.println("Número-->"+ora);
                continue;//pasamos al siguiente elemento
            }//end if si es Dígito
            else if(Character.isLetter(t)){//comprobamos si es una letra
                String ora="";
                ora+=t;
                int j=i+1;
                while(Character.isLetterOrDigit(cad.charAt(j))){
                    //mientras el siguiente elemento sea una letra o un digito
                    //ya que las variables pueden ser con números
                    ora+=cad.charAt(j);
                    j++;
                    if(j==cad.length())break;
                }
                i=j;
                if(palabraReservada(ora)){//comprobamos si es una palabra reservada
                    resultado += " PR [ " + ora + " ], ";
                    //resultadoCompilacion.append(" PR [ " + ora + " ], ");
                    //System.out.println("Palabra reservada="+ora);
                }
                else{//caso contrario es un identificador o variable
                    resultado += " ID [ " + ora + " ], ";
                    //resultadoCompilacion.append(" ID [ " + ora + " ], ");
                    //System.out.println("Identificador-->"+ora);
                }
                continue;
            }//end if si es variable
            else if(!Character.isLetterOrDigit(t)){
                //si no es letra ni dígito entonces...
                
                
                
                
                if(evaluarCaracter(t)){//¿es separador?
                    
                    if (evaluarSeparador(t)== ';') {
                        resultado += " \n ";
                        //resultadoCompilacion.append(" \n ");//COMPARA SI SE TRATA DE UN OPERADOR DE SINTAXIS
                    } else {
                        resultado += " LT [ " + evaluarSeparador(t) + "], ";
                        //resultadoCompilacion.append(" LT [ " + evaluarSeparador(t) + "], ");
                    }
                    //System.out.println("Separador-->"+evaluarSeparador(t));
                }else{ //¿o es un operador?
                    if (evaluarOperador(t) == ' ') {
                        System.out.println("Es un espacio");
                    } else {
                        resultado += " OP [ " + evaluarOperador(t) + " ], ";
                        //resultadoCompilacion.append(" OP [ " + evaluarOperador(t) + " ], ");
                    }
                    //System.out.println("Operador-->"+evaluarOperador(t));
                }
                i++;
                continue;
            }//end if si es diferente de letra y dígito
        }
        return resultado;
    }
    
    // Verificamos si es una palabra reservada.
    public boolean palabraReservada(String cad){
        if(cad.equalsIgnoreCase("FUNCION")) return true;
        else if(cad.equalsIgnoreCase("SI"))return true;
        else if(cad.equalsIgnoreCase("ENTONCES"))return true;
        else if(cad.equalsIgnoreCase("SINO"))return true;
        else if(cad.equalsIgnoreCase("REPETIR"))return true;
        else if(cad.equalsIgnoreCase("LEER")) return true;
        else if(cad.equalsIgnoreCase("ESCRIBIR")) return true;
        else if(cad.equalsIgnoreCase("int")) return true;
        else if(cad.equalsIgnoreCase("string")) return true;
        else if(cad.equalsIgnoreCase("double")) return true;
        else return cad.equalsIgnoreCase("float");
    }
    
    //Evaluamos si es un separador
    public static char evaluarSeparador(char c){
        char car=' ';
        switch (c) {
            case '(':
                car='(';
                break;
            case ')':
                car=')';
                break;
            case '[':
                car='[';
                break;
            case ']':
                car=']';
                break;
            case '"':
                car='"';
                break;
            case '.':
                car='.';
                break;
            case ':':
                car=':';
                break;
            case ',':
                car=',';
                break;
            case '@':
                car='@';
                break;
            case ';':
                car=';';
                break;
            default:
                break;
        }
        return car;
    }
    
    public static char evaluarSaltoLinea(char c) {
        char car = ' ';
        if (c==';');
        return car;
    }
    
    // Retornamos si es un caracter operador
    public static char evaluarOperador(char c){
        char car=' ';
        switch (c) {
            case '<':
                car='<';
                break;
            case '>':
                car='>';
                break;
            case '#':
                car='#';
                break;
            case '°':
                car='°';
                break;
            case '~':
                car='~';
                break;
            case '$':
                car='$';
                break;
            case '+':
                car='+';
                break;
            case '-':
                car='-';
                break;
            case '*':
                car='*';
                break;
            case '/':
                car='/';
                break;
            case '%':
                car='%';
                break;
            case '=':
                car='=';
                break;
            default:
                break;
        }
        return car;
    }
    
    //Método que evalua nuestro caracter si existe y nos retorna
    //verdadero para los separadores
    //y falso para los operadore
    public static boolean evaluarCaracter(char c){
        switch (c) {
            case '(':
                return true;
            case ')':
                return true;
            case '[':
                return true;
            case ']':
                return true;
            case '.':
                return true;
            case ':':
                return true;
            case ',':
                return true;
            case '"':
                return true;
            case ';':
                return true;
            case '@':
                return true; //comentarios
            case '<':
                return false;
            case '>':
                return false;
            case '#':
                return false; // y logico
            case '°':
                return false; // o logico
            case '~':
                return false; // no logico
            case '$':
                return false; // asignacion
            case '+':
                return false;
            case '-':
                return false;
            case '*':
                return false;
            case '/':
                return false;
            case '%':
                return false; // mod
            default:
                return false;
        }
    }
 
    public static boolean esPalabraReservada(String cad) {
        //EVALUA PALABRAS RESERVADAS PROPIAS DEL LENGUAJE DE PROGRAMACION
        if (cad.equalsIgnoreCase("if")) {
            return true;
        } else if (cad.equalsIgnoreCase("else")) {
            return true;
        } else if (cad.equalsIgnoreCase("endl")) {
            return true;
        } else if (cad.equalsIgnoreCase("for")) {
            return true;
        } else if (cad.equalsIgnoreCase("do")) {
            return true;
        } else if (cad.equalsIgnoreCase("switch")) {
            return true;
        }else if (cad.equalsIgnoreCase("default")) {
            return true;
        } else if (cad.equalsIgnoreCase("case")) {
            return true;
        } else if (cad.equalsIgnoreCase("main")) {
            return true;
        } else if (cad.equalsIgnoreCase("void")) {
            return true;
        } else if (cad.equalsIgnoreCase("public")) {
            return true;
        } else if (cad.equalsIgnoreCase("while")) {
            return true;
        } else if (cad.equalsIgnoreCase("break")) {
            return true;
        } else if (cad.equalsIgnoreCase("continue")) {
            return true;
        } else if (cad.equalsIgnoreCase("include")) {
            return true;
        } else if (cad.equalsIgnoreCase("conio")) {
            return true;
        } else if (cad.equalsIgnoreCase("stdio")) {
            return true;
        } else if (cad.equalsIgnoreCase("iostream")) {
            return true;
        } else if (cad.equalsIgnoreCase("math")) {
            return true;
        } else if (cad.equalsIgnoreCase("cout")) {
            return true;
        } else if (cad.equalsIgnoreCase("cin")) {
            return true;
        } else if (cad.equalsIgnoreCase("true")) {
            return true;
        } else if (cad.equalsIgnoreCase("false")) {
            return true;
        } else if (cad.equalsIgnoreCase("return")) {
            return true;
        } else if (cad.equalsIgnoreCase("using")) {
            return true;
        } else if (cad.equalsIgnoreCase("namespace")) {
            return true;
        } else if (cad.equalsIgnoreCase("std")) {
            return true;
        } else if (cad.equalsIgnoreCase("stdlib")) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean esTipoDeDato(String cad) {
        //EVALUA SI LA PALABRA ENCONTRADA ES UN TIPO DE DATO
        if (cad.equalsIgnoreCase("int")) {
            return true;
        } else if (cad.equalsIgnoreCase("double")) {
            return true;
        } else if (cad.equalsIgnoreCase("float")) {
            return true;
        } else if (cad.equalsIgnoreCase("char")) {
            return true;
        } else if (cad.equalsIgnoreCase("long")) {
            return true;
        } else if (cad.equalsIgnoreCase("short")) {
            return true;
        } else if (cad.equalsIgnoreCase("boolean")) {
            return true;
        } else return cad.equalsIgnoreCase("string");
    }
       
}
