package org.mockito.configuration;

import org.mockito.ReturnValues;
import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.stubbing.Answer;

public class MockitoConfiguration implements IMockitoConfiguration {

	@Override
	public ReturnValues getReturnValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Answer<Object> getDefaultAnswer() {
		// TODO Auto-generated method stub
		return new ReturnsEmptyValues();

	}

	@Override
	public AnnotationEngine getAnnotationEngine() {
		// TODO Auto-generated method stub
		return new InjectingAnnotationEngine();
	}

	@Override
	public boolean cleansStackTrace() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean enableClassCache() {
		// TODO Auto-generated method stub
		return false;
	}

}
