/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ntdun
 */
public class ValidateJavaIdentifier {

   

    public static void main(String[] args) {
        


String ID_PATTERN = "([a-z][a-z_0-9]*\\.)*[A-Z_]($[A-Z_]|[\\w_])*";
Pattern FQCN = Pattern.compile(ID_PATTERN);
        String str="RROR] > Training the classifier failed with the error: weka.classifiers.functions.SimpleLogistic: Cannot handle numeric class!";
        Matcher matcher = FQCN.matcher(str);

        int count = 0;
        while(matcher.find()) {
            count++;
            System.out.println("found: " + count + " : "
                    + matcher.start() + " - " + matcher.end());
            System.out.println(matcher.toString());
        }
    }

}
