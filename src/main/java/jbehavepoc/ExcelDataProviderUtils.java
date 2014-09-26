/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jbehavepoc;

import java.lang.reflect.Method;

/**
 *
 * @author TestVM
 */
public class ExcelDataProviderUtils {
    /**
     * Method to check the passed Test Method for illegal arguments,
     * and return the excel file that was passed through the annotation
     * @param testMethod
     * @return 
     */
    public static String resolveExcelDataProvider_fileName(Method testMethod){
        return verifyMethodAndExtractArgs(testMethod).excelFile();
    }
    /**
     * Method to check the passed Test Method for illegal arguments,
     * and return the worksheet name that was passed through the annotation
     * @param testMethod
     * @return 
     */
    public static String resolveExcelDataProvider_worksheet(Method testMethod){
        return verifyMethodAndExtractArgs(testMethod).worksheet();
    }
    /**
     * Method to check the passed Test Method for illegal arguments,
     * and return the ExcelDataProviderArgs annotation with all required fields.
     * @param testMethod
     * @return 
     */
    private static ExcelDataProviderArgs verifyMethodAndExtractArgs(Method testMethod){
        if (testMethod == null)
            throw new IllegalArgumentException("Test Method context cannot be null.");
 
        ExcelDataProviderArgs args = testMethod.getAnnotation(ExcelDataProviderArgs.class);
        if (args == null)
            throw new IllegalArgumentException("Test Method context has no ExcelDataProviderArgs annotation.");
        if (args.excelFile().isEmpty())
            throw new IllegalArgumentException("ExcelDataProviderArgs missing required field: excelFile");
        if(args.worksheet().isEmpty()){
            throw new IllegalArgumentException("ExcelDataProviderArgs missing required field: worksheet");
        }
        return args;
    }
}
