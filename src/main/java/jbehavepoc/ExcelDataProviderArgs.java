/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jbehavepoc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Custom annotation for Dealing with the EXCEL dataProvider within jbehavepoc.
 * @author Alan Holt
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDataProviderArgs {
    /**
     * The name of the excel file within your project
     * that is to be used as a DataProvider.
     * @return 
     */
    String excelFile() default "";
    
    /**
     * The name of the excel worksheet within the specified excel file
     * within your project that is to be used as a DataProvider.
     * @return 
     */
    String worksheet() default "";
}
