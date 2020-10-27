package com.ikhiloya.imokhai.retrofitannotationbasedcaching.annotation

import java.lang.annotation.Documented

/**
 * Marks a request for caching
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Cacheable {
}