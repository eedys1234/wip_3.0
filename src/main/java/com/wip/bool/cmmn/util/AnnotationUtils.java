package com.wip.bool.cmmn.util;

import com.wip.bool.security.Permission;

import java.util.Objects;

public class AnnotationUtils {

    public static Permission getPermissionPriority(Permission classAnnotation, Permission methodAnnotation) {

        if(Objects.isNull(classAnnotation) && Objects.isNull(methodAnnotation)) {
            throw new IllegalStateException();
        }

        if(Objects.isNull(methodAnnotation)) {
            return classAnnotation;
        }

        return methodAnnotation;
    }
}
