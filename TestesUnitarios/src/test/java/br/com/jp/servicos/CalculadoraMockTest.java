package br.com.jp.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {
	
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		//Mockito.when(calc.somar(1, 2)).thenReturn(5);
		//o resultado para esa execucao retorno 0 que eh o valor do mockito para numeros inteiros
		//na linha acima eu ensinei o mock apenas a calular 1 + 2 = 5
		
		//quando eu fizer a soma de 1 + qualquer numero inteiro, o resultado deve ser 5
		Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
		
		System.out.println(calc.somar(1, 2));
	}

}
