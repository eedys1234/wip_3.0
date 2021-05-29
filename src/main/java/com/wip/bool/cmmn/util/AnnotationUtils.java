package com.wip.bool.cmmn.util;

import com.wip.bool.security.Permission;

import java.util.Objects;

public final class AnnotationUtils {

    private AnnotationUtils() {
        throw new AssertionError();
    }

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
