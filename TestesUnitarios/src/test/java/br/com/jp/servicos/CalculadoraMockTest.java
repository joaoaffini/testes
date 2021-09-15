package br.com.jp.servicos;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
	
	@Test
	public void testeDois() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		//Mockito.when(calc.somar(1, 2)).thenReturn(5);
		//o resultado para esa execucao retorno 0 que eh o valor do mockito para numeros inteiros
		//na linha acima eu ensinei o mock apenas a calular 1 + 2 = 5
		
		ArgumentCaptor<Integer> capt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(capt.capture(), capt.capture())).thenReturn(5);
		
		Assert.assertEquals(5, calc.somar(1, 2));
		
		System.out.println(capt.getAllValues());
		
		
		
	}

}
