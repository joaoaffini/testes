package br.com.jp.servicos.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

//o tipo do generics TypeSafeMatcher eh o mesmo tipo que vai no primeiro parametro do matcher
public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {
	
	private Integer diaSemana;
	
	//O parametro do construtor eh do mesmo tipo do segundo parametro do matcher
	//ou seja eh o parametro passado quando eu invocar este matcher
	public DiaSemanaMatcher(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}

	public void describeTo(Description description) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_WEEK, diaSemana);
		String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
		description.appendText(dataExtenso);
	}

	@Override
	protected boolean matchesSafely(Date item) {
		// TODO Auto-generated method stub
		return false;
	}

}
