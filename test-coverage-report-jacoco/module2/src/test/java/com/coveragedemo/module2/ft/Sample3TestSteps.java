package com.coveragedemo.module2.ft;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.coveragedemo.module2.Sample3;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Sample3TestSteps {
	
	private Sample3 sample3 = new Sample3();

	private int input ;

	private int output;
	
	@Given("^whenever a value is (\\d+)$")
	public void givenInput(int num) {
		this.input = num;
	}
	@When("^getValue method of Sample3.java is called$")
	public void whenBusinessLogicCalled() {
		output = sample3.getValue(input);
	}
	@Then("^It should return (\\d+)")
	public void thenCheckOutput(int response) {
		assertEquals(output, response);
	}

}
