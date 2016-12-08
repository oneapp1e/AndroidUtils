/*
 * File Name: ReflectUtils.java 
 * History:
 * Created by Siyang.Miao on 2011-7-14
 */
package com.mlr.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ReflectUtils {
    //==========================================================================
    // Constants
    //==========================================================================
    
    //==========================================================================
    // Fields
    //==========================================================================
    
    //==========================================================================
    // Constructors
    //==========================================================================
    
    //==========================================================================
    // Getters
    //==========================================================================
    
    //==========================================================================
    // Setters
    //==========================================================================
    
    //==========================================================================
    // Methods
    //==========================================================================
	public static <T> T invokeStatic(Class<T> returnType, Class<?> receiverType, String methodName, 
			Class<?>[] paramTypes, Object[] params) {
		return invoke(returnType, receiverType, methodName, paramTypes, receiverType, params, false);
	}

	public static <T> T invokeStatic(Class<T> returnType, Class<?> receiverType, String methodName, 
			Class<?>[] paramTypes, Object[] params, boolean requestAccessiblity) {
		return invoke(returnType, receiverType, methodName, paramTypes, receiverType, params, requestAccessiblity);
	}
	
	public static <T> T invoke(Class<T> returnType, Class<?> receiverType, String methodName, Class<?>[] paramTypes, 
			Object receiver, Object[] params) {
		return invoke(returnType, receiverType, methodName, paramTypes, receiver, params, false);
	}
	
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Class<T> returnType, Class<?> receiverType, String methodName, Class<?>[] paramTypes, 
    		Object receiver, Object[] params, boolean requestAccessiblity) {
        T res = null;
        try {
            Method method = receiverType.getDeclaredMethod(methodName, paramTypes);
            if (requestAccessiblity) {
            	method.setAccessible(true);
            }
            res = (T) method.invoke(receiver, params);
        } catch (SecurityException e) {
            LogUtils.e(e);
        } catch (NoSuchMethodException e) {
            LogUtils.e(e);
        } catch (IllegalArgumentException e) {
            LogUtils.e(e);
        } catch (IllegalAccessException e) {
            LogUtils.e(e);
        } catch (InvocationTargetException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        } catch (Throwable e){
            LogUtils.e(e);
        }
        
        return res;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Class<T> fieldType, Class<?> receiverType, String fieldName, Object receiver, 
            boolean requestAccessiblity) {
        T res = null;
        try {
            Field f = receiverType.getDeclaredField(fieldName);
            if (requestAccessiblity) {
                f.setAccessible(true);
            }
            res = (T) f.get(receiver); 
        } catch (SecurityException e) {
            LogUtils.e(e);
        } catch (NoSuchFieldException e) {
            LogUtils.e(e);
        } catch (IllegalArgumentException e) {
            LogUtils.e(e);
        } catch (IllegalAccessException e) {
            LogUtils.e(e);
        } catch (Throwable e){
            LogUtils.e(e);
        }
        
        return res;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T setValue(Class<T> fieldType, Class<?> receiverType, String fieldName, Object receiver, T value,
            boolean requestAccessiblity) {
        T res = null;
        try {
            Field f = receiverType.getDeclaredField(fieldName);
            if (requestAccessiblity) {
                f.setAccessible(true);
            }
            f.set(receiver, value);
        } catch (SecurityException e) {
            LogUtils.e(e);
        } catch (NoSuchFieldException e) {
            LogUtils.e(e);
        } catch (IllegalArgumentException e) {
            LogUtils.e(e);
        } catch (IllegalAccessException e) {
            LogUtils.e(e);
        } catch (Throwable e){
            LogUtils.e(e);
        }
        
        return res;
    }
    //==========================================================================
    // Inner/Nested Classes
    //==========================================================================
}
