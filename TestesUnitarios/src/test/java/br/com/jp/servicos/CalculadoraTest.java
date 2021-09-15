package br.com.jp.servicos;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.com.jp.exceptions.NaoPodeDividirPorZeroException;
import br.com.jp.runners.ParallelRunner;

@RunWith(ParallelRunner.class)
public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		System.out.println("Iniciando...");
		calc = new Calculadora();
	}
	
	@After
	public void end() {
		System.out.println("Finalizando...");
	}

	@Test
	public void deveSomarDoisValores() {
		
		int a = 5;
		int b = 3;
		
		
		int resultado = calc.somar(a, b);
		
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void subtrairDoisValores() {
		
		int a = 8;
		int b = 5;
		
		int resultado = calc.subtracao(a, b);
		
		Assert.assertEquals(3, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		
		int a = 6;
		int b = 3;
		
		int resultado = calc.dividir(a, b);
		
		Assert.assertThat(resultado, CoreMatchers.is(2));
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		
		int a = 10;
		int b = 0;
		
		calc.dividir(a, b);
	}
}
