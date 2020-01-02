/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theapexframework.source.internal;

import java.io.File;

/**
 *
 * @author owenboseley
 */
public class FileProbe {
    public static File findDirectory(String dir){
        return new File(dir);
    }
}
