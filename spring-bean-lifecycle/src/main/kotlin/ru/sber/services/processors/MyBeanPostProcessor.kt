package ru.sber.services.processors

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class MyBeanPostProcessor : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        return bean
    }




}