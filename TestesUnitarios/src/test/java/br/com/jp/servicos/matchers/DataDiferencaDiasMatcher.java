package br.com.jp.servicos.matchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.com.jp.utils.DataUtils;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {
	
	private Integer qtdeDias;

	public DataDiferencaDiasMatcher(Integer qtdeDias) {
		this.qtdeDias = qtdeDias;
	}
	
	//descricao que sera exibida qdo o teste falhar
	public void describeTo(Description description) {
		Date dataEsperada = DataUtils.obterDataComDiferencaDias(qtdeDias);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		description.appendText(df.format(dataEsperada));

	}

	//Aqui vai a verificacao
	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(qtdeDias));
	}

}
