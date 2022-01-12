package com.fs.voldemort.business.fit;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CallerFitlyManager {

    private static boolean isInitialized = false;

    private static final Map<Class<?>,FitArg> callerFitlyAnnotationMap = new ConcurrentHashMap<>();

    private static final String CALL_FIT_ANNOTATION_FIELD_NAME = "f_getArg";

    private static final String SCAN_PKG = "org.reflections.Reflections";

    public static void init() throws NoSuchFieldException, IllegalAccessException {
        isInitialized = true;
        refresh();
    }

    public static void refresh() throws NoSuchFieldException, IllegalAccessException {
        Reflections reflections = new Reflections(SCAN_PKG);
        final Set<Class<?>> callFitlyAnnotationClasses = reflections.getTypesAnnotatedWith(CallerFitlyAnnotation.class);

        callerFitlyAnnotationMap.clear();

        callerFitlyAnnotationMap.putAll(callFitlyAnnotationClasses.stream().collect(Collectors.toMap(annotationClazz->annotationClazz,annotationClazz->{
            try {
                Field field = ContextOnly.class.getDeclaredField(CALL_FIT_ANNOTATION_FIELD_NAME);
                final Object fitArgObj = field.get(null);
                if(fitArgObj != null && fitArgObj instanceof FitArg) {
                    return (FitArg)fitArgObj;
                }
                throw new FitInitException("Fit manager init error, can not find " + CALL_FIT_ANNOTATION_FIELD_NAME);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new FitInitException("Fit manager init error:", e);
            }
        })));
    }

    public static FitArg getFitArg(Class<?> fitAnnotationClazz) {
        if(!isInitialized) {
            throw new FitException("CallerFitlyManager uninitialized, please check!");
        }
        return callerFitlyAnnotationMap.get(fitAnnotationClazz);
    }

    public static Set<Class<?>> getAllFitAnnotation() {
        if(!isInitialized) {
            throw new FitException("CallerFitlyManager uninitialized, please check!");
        }
        return callerFitlyAnnotationMap.keySet();
    }

    public static boolean containFitAnnotation(Class<?> fitAnnotationClazz) {
        return callerFitlyAnnotationMap.containsKey(fitAnnotationClazz);
    }
}
