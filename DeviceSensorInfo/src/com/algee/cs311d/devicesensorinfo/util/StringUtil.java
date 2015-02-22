/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : StringUtil.java
 *     Tag : CS311D - Extra Credit #1
 * Purpose : A class meant to provide convenience methods to manipulate Strings
 *           and String literals.
 */
package com.algee.cs311d.devicesensorinfo.util;

/**************************** CLASS : StringUtil ******************************/
public class StringUtil
{
    private StringUtil(){}
    
    /************************** capitalizeEachWord ****************************/
    /**
     * Capitalizes each word in a String.
     * @param str
     * @return
     */
    public static final String capitalizeEachWord(String str)
    {
        boolean bCapitalize = true;
        StringBuilder sb = new StringBuilder(str.length());
        for(int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if(Character.isWhitespace(c))
                bCapitalize = true;
            else if(Character.isLetter(c))
                if(bCapitalize)
                {
                    c = Character.toUpperCase(c);
                    bCapitalize = false;
                }

            sb.append(c);
        }
        
        return sb.toString();
    }
}
