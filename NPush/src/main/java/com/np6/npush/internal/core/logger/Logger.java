package com.np6.npush.internal.core.logger;

public interface Logger {

    <T> void info(com.np6.npush.internal.models.log.common.Info<T> log);

    <T> void error(com.np6.npush.internal.models.log.common.Error<T> log);

    <T>  void warning(com.np6.npush.internal.models.log.common.Warning<T> log);

}
