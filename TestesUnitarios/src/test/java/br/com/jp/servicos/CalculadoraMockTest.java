package br.com.jp.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {
	
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devoMostrarADiferencaEntroMockESpy() {
		//A diferenca entre mock e spy
		//qdo executa o metodo com o valor esperado, eles retornam o mesmo resultado
		//qdo vc passa um valor que nao eh esperado na chamada do metodo, o mock sempre retorna um valor
		//default que no caso de Integer o valor eh 0
		//o spy efetua a execucao do metodo e retorna um valor calculado que foi executado pelo metodo
		//o spy so funciona para classes concretas
		Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
		//chamando desta forma o spy sempre executa o metodo e depois grava a expectativa de retorno
		//para evitar a execucao do metodo, devemos alterar a chamada para
		Mockito.doReturn(8).when(calcSpy).somar(1, 2);
		// Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
		
		System.out.println("*****VALORES ESPERADOS******");
		System.out.println("Mock:" + calcMock.somar(1, 2));
		System.out.println("Spy:" + calcSpy.somar(1, 2));
		
		System.out.println("*****VALORES NAO ESPERADOS******");
		System.out.println("Mock:" + calcMock.somar(1, 5));
		System.out.println("Spy:" + calcSpy.somar(1, 5));
		
		System.out.println("*****MOCK chamando o metodo real******");
		Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
		Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
		
		System.out.println("Mock:" + calcMock.somar(1, 2));
		System.out.println("Spy:" + calcSpy.somar(1, 2));
		
		//se eu nao quiser que o spy execute um metodo
		Mockito.doNothing().when(calcSpy).imprime();
		calcMock.imprime();
		calcSpy.imprime();
		
		
	}
	
	
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
