/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import static sun.tools.jconsole.Messages.OPERATING_SYSTEM;

/**
 *
 * @author samueltcsantos 
 * https://github.com/samueltcsantos
 */
public class OSDetector {
	
	/**
 * Check if the operating system is Windows.
 * 
 * @return
 *  true or false
 */
public static boolean isWindows() {
	String os = OPERATING_SYSTEM.toLowerCase();
	return (os.indexOf("win") >= 0);
}

/**
 * Check if the operating system is Unix.
 * 
 * @return
 *  true or false
 */
public static boolean isUnix() {
	String os = OPERATING_SYSTEM.toLowerCase();
	return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
}
/**
 * Check if the operating system is Mac.
 * 
 * @return
 *  true or false
 */
public static boolean isMac() {
	String os = OPERATING_SYSTEM.toLowerCase();
	return (os.indexOf("mac") >= 0);
}

/**
 * Check if the operating system is Solaris.
 * 
 * @return
 *  true or false
 */
public static boolean isSolaris() {
	String os = OPERATING_SYSTEM.toLowerCase();
	return (os.indexOf("sunos") >= 0);
}  
	
}
