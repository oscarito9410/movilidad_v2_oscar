package com.gruposalinas.elektra.movilidadgs.utils;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by adrian on 26/01/2017.
 */

public class GuiTools
{
    /**
     * Ancho de la pantalla base.
     */
    private static double BaseWidth = 320.0;

    /**
     * Factor de escala para la pantalla.
     */
    private static double ScaleFactor = 1.0;

    /**
     * Bandera de inicializaci�n.
     */
    private static boolean initialized = false;

    /**
     * Unused.
     */
    private static boolean virtualButtonsEnabled = false;

    /**
     * M�tricas de la pantalla actual.
     */
    private static DisplayMetrics metrics = null;

    /**
     * La �nica instancia de la clase.
     */
    private static GuiTools current = null;

    /**
     * Obtiene la �nica instancia de la clase.
     * @return La instancia.
     */
    public static GuiTools getCurrent() {
        if(null == current)
            current = new GuiTools();
        return current;
    }

    /**
     * Contructor privado para respetar el patron Singleton.
     */
    private GuiTools() {
        BaseWidth = 320.0;
        ScaleFactor = 1.0;
    }

    /**
     * Inicializacion de la instancia para detectar el factor de escala correspondiente.
     * @param manager El manejador de pantallas de donde se obtienen las caracteristicas de la pantalla.
     */
    public void init(WindowManager manager) {
        if(null == manager)
            return;

        metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        ScaleFactor = ((double)metrics.widthPixels) / BaseWidth;
        initialized = true;

        ScreenWidth = metrics.widthPixels;
        ScreenHeigth = metrics.heightPixels;

        Log.d("GuiTools", "Screen resolution (WxH): " + metrics.widthPixels + "X" + metrics.heightPixels);
        Log.d("GuiTools", "Scale Factor: " + ScaleFactor);
    }

    public static SpannableString underlineText(String str){
        SpannableString content = new SpannableString(str);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    /**
     * Obtiene la bandera que indica si el elemento esta inicializado.
     * @return True si la instancia ha sido inicializada, False de otro modo.
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Obtiene el factor de escala para pantalla actual.
     * @return El factor de escala.
     */
    public static double getScaleFactor() {
        return ScaleFactor;
    }

    /**
     * Obtiene la equivalencia en pixeles para la pantalla actual.
     * @param baseMeasure La medida inicial.
     * @return La medida tras aplicr el factor de escala correspondiente.
     */
    public int getEquivalenceInPixels(double baseMeasure) {
        if(baseMeasure < 0.0)
            return 0;
        return (int)(baseMeasure * ScaleFactor);
    }

    /**
     * Obtiene la equivalencia en pixeles para la pantalla actual.
     * @param baseMeasure La medida inicial.
     * @return La medida tras aplicr el factor de escala correspondiente.
     */
    public int getEquivalenceInPixels(int baseMeasure) {
        if(baseMeasure < 0.0)
            return 0;
        return (int)((double)baseMeasure * ScaleFactor);
    }

    /**
     * Obtiene la equivalencia en pixeles para la pantalla actual.
     * @param baseMeasure La medida inicial.
     * @return La medida tras aplicr el factor de escala correspondiente.
     */
    public int getEquivalenceFromScaledPixels(double baseMeasure) {
        if(baseMeasure < 0.0)
            return 0;
        return getEquivalenceInPixels(pxToDip((int)baseMeasure));
    }

    /**
     * Obtiene la equivalencia en pixeles para la pantalla actual.
     * @param baseMeasure La medida inicial.
     * @return La medida tras aplicr el factor de escala correspondiente.
     */
    public int getEquivalenceFromScaledPixels(int baseMeasure) {
        if(baseMeasure < 0.0)
            return 0;
        return getEquivalenceInPixels(pxToDip(baseMeasure));
    }





    /**
     * Escala y establece cada valor de relleno(padding) del elemento.
     * @param view La vista a escalar.
     */
    public void scalePaddings(View view){
        if(null == view)
            return;

        int[] paddings = new int[4];

        paddings[0] = getEquivalenceInPixels(pxToDip(view.getPaddingLeft()));
        paddings[1] = getEquivalenceInPixels(pxToDip(view.getPaddingTop()));
        paddings[2] = getEquivalenceInPixels(pxToDip(view.getPaddingRight()));
        paddings[3] = getEquivalenceInPixels(pxToDip(view.getPaddingBottom()));

        view.setPadding(paddings[0], paddings[1], paddings[2], paddings[3]);
    }

    /**
     * Escala y establece los parametros LayoutParams de la vista.
     * @param view La vista a escalar.
     */
    public void scaleLayoutParams(View view) {
        if(null == view)
            return;

        ViewGroup.LayoutParams params = view.getLayoutParams();

        if(params instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams)params;
            scaleLinearLayoutParams(view, linearLayoutParams);
        } else if(params instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams frameLayoutParams = (FrameLayout.LayoutParams)params;
            scaleFrameLayoutParams(view, frameLayoutParams);
        } else if(params instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams relativeLayoutParams = (RelativeLayout.LayoutParams)params;
            scaleRelativeLayoutParams(view, relativeLayoutParams);
        }
    }

    /**
     * Escala y establece un objeto LayoutParams para un LinearLayout.
     * @param params Los parametros de layout del elemento a escalar.
     */
    private void scaleLinearLayoutParams(View view, LinearLayout.LayoutParams params) {
        if(null == view || null == params || !initialized)
            return;

        params.leftMargin 	= getEquivalenceInPixels(pxToDip(params.leftMargin));
        params.rightMargin 	= getEquivalenceInPixels(pxToDip(params.rightMargin));
        params.topMargin 	= getEquivalenceInPixels(pxToDip(params.topMargin));
        params.bottomMargin = getEquivalenceInPixels(pxToDip(params.bottomMargin));

        if(metrics.widthPixels != params.width && -1 != params.width && -2 != params.width)
            params.width = getEquivalenceInPixels(pxToDip(params.width));
        if(metrics.heightPixels != params.height && -1 != params.height && -2 != params.height)
            params.height = getEquivalenceInPixels(pxToDip(params.height));

        view.setLayoutParams(params);
    }

    /**
     * Escala y establece un objeto LayoutParams para un RelativeLayout.
     * @param params Los parametros de layout del elemento a escalar.
     */
    private void scaleRelativeLayoutParams(View view, RelativeLayout.LayoutParams params) {
        if(null == view || null == params || !initialized)
            return;

        params.leftMargin 	= getEquivalenceInPixels(pxToDip(params.leftMargin));
        params.rightMargin 	= getEquivalenceInPixels(pxToDip(params.rightMargin));
        params.topMargin 	= getEquivalenceInPixels(pxToDip(params.topMargin));
        params.bottomMargin = getEquivalenceInPixels(pxToDip(params.bottomMargin));

        if(metrics.widthPixels != params.width && -1 != params.width && -2 != params.width)
            params.width = getEquivalenceInPixels(pxToDip(params.width));
        if(metrics.heightPixels != params.height && -1 != params.height && -2 != params.height)
            params.height = getEquivalenceInPixels(pxToDip(params.height));

        view.setLayoutParams(params);
    }

    /**
     * Escala y establece un objeto LayoutParams para un FrameLayout.
     * @param params Los parametros de layout del elemento a escalar.
     */
    private void scaleFrameLayoutParams(View view, FrameLayout.LayoutParams params) {
        if(null == view || null == params || !initialized)
            return;

        params.leftMargin 	= getEquivalenceInPixels(pxToDip(params.leftMargin));
        params.rightMargin 	= getEquivalenceInPixels(pxToDip(params.rightMargin));
        params.topMargin 	= getEquivalenceInPixels(pxToDip(params.topMargin));
        params.bottomMargin = getEquivalenceInPixels(pxToDip(params.bottomMargin));

        if(metrics.widthPixels != params.width && -1 != params.width && -2 != params.width)
            params.width = getEquivalenceInPixels(pxToDip(params.width));
        if(metrics.heightPixels != params.height && -1 != params.height && -2 != params.width)
            params.height = getEquivalenceInPixels(pxToDip(params.height));

        view.setLayoutParams(params);
    }

    /**
     * Convierte una medida en pixeles a dip.
     * @param px Medida en pixeles.
     * @return Equivalente en dip.
     */
    private double pxToDip(int px) {
        double dblPx = (double)px;
        double equivalent = dblPx / metrics.density;
        return equivalent;
        //return ((double)px / metrics.density);
    }

    /**
     * Escala el tama�o de letra de un elemento TextView.
     * @param view El TextView a escalar.
     */
    public void tryScaleText(TextView view) {
        try {
            double textScaleChangeFactor = 1.0;
            //double textScaleChangeFactor = 1.0 / ScaleFactor;
            //view.setScaleX((float)ScaleFactor);
            //view.setScaleY((float)ScaleFactor);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(view.getTextSize() * (ScaleFactor * textScaleChangeFactor)));
            //view.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)(view.getTextSize() * (1.0)));
        }catch(Exception ex) {
            Log.e("GuiTools", "Trying to scale text on an invalid view.", ex);
        }
    }

    /**
     * Escala los parametros de posicionamiento (LayoutParams y Padings).
     * @param view La vista a escalar.
     */
    public void scale(View view) {
        //scale(view, false);
        if(null == view)
            return;

        scaleLayoutParams(view);
        scalePaddings(view);

        if(view instanceof TextView)
            tryScaleText((TextView)view);
    }

    /**
     * Escala los parametros de posicionamiento (LayoutParams y Padings) y tama�o de texto si es necesario.
     * @param view La vista a escalar.
     * @param isTextView Bandera que indica si la vista hereda o es una instancia de TextView y se debe escalar el tama�o de texto.
     */
    public void scale(View view, boolean isTextView) {
        if(null == view)
            return;

        scaleLayoutParams(view);
        scalePaddings(view);

        if(isTextView)
            tryScaleText((TextView)view);
    }

    public void scaleAll(ViewGroup viewGroup) {
        if(null == viewGroup)
            return;


        int childCount = viewGroup.getChildCount();


        for(int counter = 0; counter < childCount; counter++) {
            View view = viewGroup.getChildAt(counter);

            scale(view);

            if(view instanceof ViewGroup)
                scaleAll((ViewGroup)view);
        }
    }







	/*
	public static String getMoneyString(String data){
		if(data != null){
			BigDecimal val = new BigDecimal(0);
			try{
				val = new BigDecimal(data);
			}catch(NumberFormatException e){
				Log.e("GuiTools", "Numberformat");
			}

			DecimalFormat myFormatter = new DecimalFormat("$###,##0.00");
			return myFormatter.format(val);
		}else{
			return "";
		}
	}*/


    public static String getMoneyString(String amount) {

        if ((amount == null) || (amount.length() == 0)) {

            return "0.00";

        }

        StringBuffer decimal = new StringBuffer();

        StringBuffer integer = new StringBuffer();



        // format the decimal part

        int decimalPos = amount.indexOf(".");

        if ((decimalPos < 0) || (decimalPos == amount.length() - 1)) {

            // there is not decimal point or it is the last character of the string

            decimal.append("00");

        } else {

            // get the decimal positions

            String auxDecimal = amount.substring(decimalPos + 1);

            if (auxDecimal.length() >= 2) {

                decimal.append(auxDecimal.substring(0, 2));

            } else {

                // add 0s to complete 2 decimals

                decimal.append(auxDecimal);

                for (int i = decimal.length(); i < 2; i++) {

                    decimal.append("0");

                }

            }

        }



        // format the integer part

        String auxInteger = amount;

        if (decimalPos == 0) {

            auxInteger = "0";

        } else if (decimalPos > 0) {

            auxInteger = amount.substring(0, decimalPos);

        }

        int curPos;

        for (int i = auxInteger.length(); i > 0; i = i - 3) {

            curPos = i - 3;

            if (curPos < 0) {

                curPos = 0;

            }

            if (curPos > 0) {

                integer = new StringBuffer(",").

                        append(auxInteger.substring(curPos, i)).

                        append(integer);

            } else {

                integer = new StringBuffer(auxInteger.

                        substring(0, i)).append(integer);

            }

        }

        StringBuffer result = integer.append(".").append(decimal.toString());


        return "$"+result.toString();

    }

    public static String getTDCString(String data){
        if(data != null){
            if(data.length()>=5){
                Integer index = data.length()-5;

                return "*"+data.substring(index);
            }else{
                return "*"+data;
            }
        }else{
            return "";
        }
    }

    public static String getTDCString2Asteriscos(String data){
        if(data != null){
            if(data.length()>=5){
                Integer index = data.length()-4;

                return "*"+data.substring(index);
            }else{
                return "*"+data;
            }
        }else{
            return "";
        }
    }

    public static Integer getAmountThFormatted(Integer data){
        Integer min = 1000;
        String ret = min.toString();

        if(data > min){
            ret = data.toString();
            ret = ret.substring(0, (ret.length()-3));
            ret = ret+"000";
        }

        return Integer.valueOf(ret);
    }

    public int ScreenWidth = 0;

    public int ScreenHeigth = 0;


}
