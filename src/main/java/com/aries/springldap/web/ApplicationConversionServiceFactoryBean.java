package com.aries.springldap.web;

import com.aries.springldap.SimpleCrud;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
        
/**
 * A central place to register application Converters and Formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}
	

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(new SimpleCrudConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }

	static class SimpleCrudConverter implements Converter<SimpleCrud, String>  {
        public String convert(SimpleCrud simpleCrud) {
        return new StringBuilder().append(simpleCrud.getMessage()).toString();
        }
        
    }
}
