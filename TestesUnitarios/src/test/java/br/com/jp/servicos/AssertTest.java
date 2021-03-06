package br.com.jp.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.com.jp.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void teste() {
		
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals(1, 1);
		//Quando for comparar numeros com casas decimais, o ultimo parametro deve ser informado
		//ele representa a margem de erro que pode ter nessa comparacao
		Assert.assertEquals(0.51234, 0.512, 0.001);
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		
		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "casa");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;
		Assert.assertEquals(u1, u2);
		
		//Para comparar se sao a mesma instancia
		Assert.assertSame(u2, u2);
		Assert.assertNotSame(u1, u2);
		
		Assert.assertNull(u3);
		
	}
	
}
